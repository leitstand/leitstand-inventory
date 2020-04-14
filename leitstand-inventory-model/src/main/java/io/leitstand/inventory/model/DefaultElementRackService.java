/*
 * Copyright 2020 RtBrick Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.leitstand.inventory.model;

import static io.leitstand.commons.messages.MessageFactory.createMessage;
import static io.leitstand.commons.model.ObjectUtil.isDifferent;
import static io.leitstand.commons.model.ObjectUtil.optional;
import static io.leitstand.inventory.model.ElementGroup_Rack.findByRackName;
import static io.leitstand.inventory.model.ElementGroup_Rack_Element.findRackByElement;
import static io.leitstand.inventory.model.ElementGroup_Rack_Element.findRackElement;
import static io.leitstand.inventory.service.ElementRack.newElementRack;
import static io.leitstand.inventory.service.RackItem.newRackItem;
import static io.leitstand.inventory.service.ReasonCode.IVT0380I_ELEMENT_RACK_LOCATION_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0381I_ELEMENT_RACK_LOCATION_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0800E_RACK_NOT_FOUND;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.commons.model.Service;
import io.leitstand.commons.tx.SubtransactionService;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementRack;
import io.leitstand.inventory.service.ElementRackLocation;
import io.leitstand.inventory.service.ElementRackService;
import io.leitstand.inventory.service.RackItem;

@Service
public class DefaultElementRackService implements ElementRackService{

	private static final Logger LOG = Logger.getLogger(DefaultElementRackService.class.getName());
	
	@Inject
	private ElementProvider elements;
	
	@Inject
	@Inventory
	private Repository repository;
	
	@Inject
	@Inventory
	private SubtransactionService flow;
	
	@Inject
	private Messages messages;
	
	@Override
	public ElementRack getElementRack(ElementId elementId) {
		Element element = elements.fetchElement(elementId);
		return getElementRack(element);
	}

	@Override
	public ElementRack getElementRack(ElementName elementName) {
		Element element = elements.fetchElement(elementName);
		return getElementRack(element);
	}

	private ElementRack getElementRack(Element element) {
		ElementGroup_Rack rack = repository.execute(findRackByElement(element));
		ElementGroup group = element.getGroup();
		if(rack == null) {
			LOG.fine(() -> format("%s: Rack mount point for element %s (%s) in %s group %s is not specified.", 
								  IVT0800E_RACK_NOT_FOUND.getReasonCode(),
								  element.getElementName(),
								  element.getElementId(),
								  element.getGroup().getGroupType(),
								  element.getGroup().getGroupName()));
			throw new EntityNotFoundException(IVT0800E_RACK_NOT_FOUND, 
											  element.getElementId(), 
											  element.getElementName());
		}
		
		List<RackItem> items = rack.getElements()
	   			  				   .stream()
	   			  				   .map(re -> newRackItem()
	   			  							  .withElementId(re.getElementId())
	   			  							  .withElementName(re.getElementName())
	   			  							  .withElementAlias(re.getElementAlias())
	   			  							  .withElementRole(re.getElementRoleName())
	   			  							  .withPlatformId(optional(re.getPlatform(),Platform::getPlatformId))
	   			  							  .withPlatformName(optional(re.getPlatform(), Platform::getPlatformName))
	   			  							  .withUnit(re.getUnit())
	   			  							  .withHeight(re.getHeight())
	   			  							  .withHalfRack(re.isHalfRack())
	   			  							  .withHalfRackPosition(re.getHalfRackPosition())
	   			  							  .build())
	   			  					.collect(toList());
		
		return newElementRack()
			   .withGroupId(group.getGroupId())
			   .withGroupName(group.getGroupName())
			   .withGroupType(group.getGroupType())
			   .withElementId(element.getElementId())
			   .withElementRole(element.getElementRoleName())
			   .withElementName(element.getElementName())
			   .withElementAlias(element.getElementAlias())
			   .withRackName(rack.getRackName())
			   .withRackUnits(rack.getUnits())
			   .withLocation(rack.getLocation())
			   .withDescription(rack.getDescription())
			   .withRackItems(items)
			   .build();
	}
	
	@Override
	public void storeElementRackMountPoint(ElementId elementId, 
								 		   ElementRackLocation settings) {
		Element element = elements.fetchElement(elementId);
		storeElementRack(element,
						 settings);
		
	}

	private void storeElementRack(Element element, 
								  ElementRackLocation settings) {
		ElementGroup_Rack_Element rackElement = repository.execute(findRackElement(element));
		if(rackElement != null && isDifferent(rackElement.getRack().getRackName(),
											  settings.getRackName())){
			
			LOG.fine(() -> format("%s: Rack mount point for element %s %s (%s) in %s group %s removed (Rack %s, unit %d)", 
		  			  			  IVT0381I_ELEMENT_RACK_LOCATION_REMOVED.getReasonCode(),
		  			  			  element.getElementRoleName(),
		  			  			  element.getElementName(),
		  			  			  element.getElementId(),
		  			  			  element.getGroup().getGroupType(),
		  			  			  element.getGroup().getGroupName(),
		  			  			  settings.getRackName(),
		  			  			  settings.getUnit()));
			
			repository.remove(rackElement);
			rackElement = null;
		}
		
		if(rackElement == null) {
			ElementGroup_Rack rack = repository.execute(findByRackName(element.getGroup(),
														  			   settings.getRackName()));
			if(rack == null) {
				ElementGroup group = element.getGroup();
				rack = repository.execute(findByRackName(group, 
													    settings.getRackName()));	
				
			}
			rackElement = new ElementGroup_Rack_Element(rack, element);
			repository.add(rackElement);
		} 
		rackElement.setUnit(settings.getUnit());
		rackElement.setHalfRackPosition(settings.getPosition());
		LOG.fine(() -> format("%s: Rack mount point for element %s %s (%s) in %s group %s stored. Rack %s, unit %d", 
				  			  IVT0380I_ELEMENT_RACK_LOCATION_STORED.getReasonCode(),
				  			  element.getElementRoleName(),
				  			  element.getElementName(),
				  			  element.getElementId(),
				  			  element.getGroup().getGroupType(),
				  			  element.getGroup().getGroupName(),
				  			  settings.getRackName(),
				  			  settings.getUnit()));

		messages.add(createMessage(IVT0380I_ELEMENT_RACK_LOCATION_STORED, 
								   element.getElementName(),
								   settings.getRackName(),
								   settings.getUnit()));
	}

	@Override
	public void storeElementRackLocation(ElementName elementName, ElementRackLocation settings) {
		Element element = elements.fetchElement(elementName);
		storeElementRack(element,settings);
	}

	@Override
	public void removeElementRackLocation(ElementId elementId) {
		Element element = elements.fetchElement(elementId);
		removeElementRackLocation(element);
	}

	@Override
	public void removeElementRackLocation(ElementName elementName) {
		Element element = elements.fetchElement(elementName);
		removeElementRackLocation(element);		
	}
	
	private void removeElementRackLocation(Element element) {
		ElementGroup_Rack_Element rackElement = repository.execute(findRackElement(element));
		if(rackElement != null) {
			repository.remove(rackElement);
			LOG.fine(() -> format("%s: Rack mount point for element %s %s (%s) in %s group %s removed (Rack %s, unit %d)", 
								  IVT0381I_ELEMENT_RACK_LOCATION_REMOVED.getReasonCode(),
								  element.getElementRoleName(),
								  element.getElementName(),
								  element.getElementId(),
								  element.getGroup().getGroupType(),
								  element.getGroup().getGroupName(),
								  rackElement.getRackName(),
								  rackElement.getUnit()));
			
			messages.add(createMessage(IVT0380I_ELEMENT_RACK_LOCATION_STORED, 
					   				   element.getElementName(),
					   				   rackElement.getRackName(),
					   				   rackElement.getUnit()));
		}
	}

}

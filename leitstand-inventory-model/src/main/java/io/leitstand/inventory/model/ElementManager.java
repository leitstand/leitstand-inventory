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
import static io.leitstand.inventory.event.ElementOperationalStateChangedEvent.newElementOperationalStateChangedEvent;
import static io.leitstand.inventory.event.ElementRemovedEvent.newElementRemovedEvent;
import static io.leitstand.inventory.model.Element.findElementsByAssetId;
import static io.leitstand.inventory.model.Element.findElementsByManagementIP;
import static io.leitstand.inventory.model.Element.findElementsByName;
import static io.leitstand.inventory.model.Element.findElementsByNameOrTag;
import static io.leitstand.inventory.model.Element.findElementsBySerialNumber;
import static io.leitstand.inventory.model.Element_Config.removeAllConfigurations;
import static io.leitstand.inventory.model.Element_ContainerInterface.removeIfcs;
import static io.leitstand.inventory.model.Element_DnsRecordSet.removeDnsRecordSets;
import static io.leitstand.inventory.model.Element_Environment.removeEnvironments;
import static io.leitstand.inventory.model.Element_Module.removeModules;
import static io.leitstand.inventory.model.Element_PhysicalInterface.removeIfps;
import static io.leitstand.inventory.model.Element_PhysicalInterface.removeNeighbors;
import static io.leitstand.inventory.model.Element_Service.removeServices;
import static io.leitstand.inventory.model.Element_ServiceContext.removeServiceContexts;
import static io.leitstand.inventory.service.ReasonCode.IVT0302I_ELEMENT_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0303E_ELEMENT_NOT_REMOVABLE;
import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.event.ElementEvent;
import io.leitstand.inventory.event.ElementRemovedEvent;
import io.leitstand.inventory.service.ElementSettings;
import io.leitstand.inventory.service.OperationalState;

@Dependent
public class ElementManager {

	private static final Logger LOG = Logger.getLogger(ElementManager.class.getName());
	
	private Repository repository;
	private Event<ElementEvent> sink;
	private Messages messages;
	
	@Inject
	public ElementManager(@Inventory Repository repository,
						  Event<ElementEvent> sink,
						  Messages messages) {
		this.repository = repository;
		this.sink = sink;
		this.messages = messages;
	}
	
	protected ElementManager() {
		//CDI
	}



	public void remove(Element element) {
		if(element.isActive()){
			throw new ConflictException(IVT0303E_ELEMENT_NOT_REMOVABLE, 
										element.getElementName());
		}
		repository.remove(element);
		
		LOG.fine(()->format("%s: Removed %s element %s (%s)", 
							IVT0302I_ELEMENT_REMOVED.getReasonCode(),
							element.getElementRoleName(),
							element.getElementName(),
							element.getElementId()));
		
		
		messages.add(createMessage(IVT0302I_ELEMENT_REMOVED,
				     element.getElementName()));

		// Only retired or new elements are allowed to be removed from the inventory.
		// - New elements might have been accidentally added and need to be removed
		//   in order to fix this.
		// - Retired elements can be removed from the inventory, once the information 
		//   is not required by management processes any longer.
		ElementRemovedEvent event = newElementRemovedEvent()
									.withGroupId(element.getGroupId())
									.withGroupName(element.getGroupName())
									.withGroupType(element.getGroupType())
									.withElementId(element.getElementId())
									.withElementName(element.getElementName())
									.withElementAlias(element.getElementAlias())
									.withElementRole(element.getElementRoleName())
									.withAdministrativeState(element.getAdministrativeState())
									.withOperationalState(element.getOperationalState())
									.withDateModified(element.getDateModified())
									.build();

		sink.fire(event);
	}
	
	public void forceRemove(Element element) {

		if(element.isActive()){
			throw new ConflictException(IVT0303E_ELEMENT_NOT_REMOVABLE, 
										element.getElementName());
		}
		
		
		int removedConfigs = repository.execute(removeAllConfigurations(element));
		LOG.fine(()->format("Removed %d configurations of %s %s (%s)",
						    removedConfigs,
						    element.getElementRoleName(),
						    element.getElementName(),
						    element.getElementId()));
		
		int removedEnvs = repository.execute(removeEnvironments(element));
		LOG.fine(()->format("Removed %d configurations of %s %s (%s)",
						    removedEnvs,
						    element.getElementRoleName(),
						    element.getElementName(),
						    element.getElementId()));
		
		int removedDnsRecords = repository.execute(removeDnsRecordSets(element));
		LOG.fine(()->format("Removed %d DNS records of %s %s (%s)",
							removedDnsRecords,
						    element.getElementRoleName(),
						    element.getElementName(),
						    element.getElementId()));

		int removedIfls = repository.execute(Element_LogicalInterface.removeIfls(element));
		LOG.fine(()->format("Removed %d logical interfaces of %s %s (%s)",
						    removedIfls,
						    element.getElementRoleName(),
						    element.getElementName(),
						    element.getElementId()));

		int removedNeighbors = repository.execute(removeNeighbors(element));
		
		LOG.fine(()->format("Removed %d neighbors of %s %s (%s)",
							removedNeighbors,
							element.getElementRoleName(),
							element.getElementName(),
							element.getElementId()));
		
		int removedIfps = repository.execute(removeIfps(element));
		LOG.fine(()->format("Removed %d physical interfaces of %s %s (%s)",
						    removedIfps,
						    element.getElementRoleName(),
						    element.getElementName(),
						    element.getElementId()));
		
		int removedIfcs = repository.execute(removeIfcs(element));
		LOG.fine(()->format("Removed %d container interfaces of %s %s (%s)",
						    removedIfcs,
						    element.getElementRoleName(),
						    element.getElementName(),
						    element.getElementId()));
		
		int removedServiceContexts = repository.execute(removeServiceContexts(element));
		LOG.fine(()->format("Removed %d service contexts of %s %s (%s)",
							removedServiceContexts,
						    element.getElementRoleName(),
						    element.getElementName(),
						    element.getElementId()));
		
		int removedServices = repository.execute(removeServices(element));
		LOG.fine(()->format("Removed %d services of %s %s (%s)",
						    removedServices,
						    element.getElementRoleName(),
						    element.getElementName(),
						    element.getElementId()));
		
		int removedModules = repository.execute(removeModules(element));
		LOG.fine(()->format("Removed %d modules of %s %s (%s)",
						    removedModules,
						    element.getElementRoleName(),
						    element.getElementName(),
						    element.getElementId()));
		
		int removedImages = repository.execute(Element_Image.removeElementImages(element));
        LOG.fine(()->format("Removed %d images of %s %s (%s)",
                            removedImages,
                            element.getElementRoleName(),
                            element.getElementName(),
                            element.getElementId()));
		
		
		
		Rack_Item rackItem = repository.execute(Rack_Item.findRackItem(element));
		if(rackItem != null) {
			LOG.fine(()->format("Remove rack item %d from rack %s (%s)",
								rackItem.getPosition(),
								rackItem.getRack().getRackName(),
								rackItem.getRack().getRackId()));
		}
		
		
		LOG.fine(()->format("%s: Removed %s %s (%s)", 
							IVT0302I_ELEMENT_REMOVED.getReasonCode(),
							element.getElementRoleName(),
							element.getElementName(),
							element.getElementId()));
		
		remove(element);
	}
	
	
	public void updateElementOperationalState(Element element, 
											  OperationalState state) {
		OperationalState old = element.setOperationalState(state);
		if(isDifferent(state, old)) {
			sink.fire(newElementOperationalStateChangedEvent()
					  .withGroupId(element.getGroupId())
					  .withGroupName(element.getGroupName())
					  .withGroupType(element.getGroupType())
					  .withElementId(element.getElementId())
					  .withElementName(element.getElementName())
					  .withElementAlias(element.getElementAlias())
					  .withElementRole(element.getElementRoleName())
					  .withAdministrativeState(element.getAdministrativeState())
					  .withOperationalState(state)
					  .withPreviousState(old)
					  .withDateModified(element.getDateModified())
					  .build());
		}
	}

	public List<ElementSettings> filterElementsByName(String filter, int offset, int items) {
		return unmodifiableList(repository.
								executeMapListItem(findElementsByName(filter,
																	  offset,
																	  items),
								ElementSettingsManager::settingsOf));
	
	}

	public List<ElementSettings> filterElementsByNameOrTag(String filter, int offset, int items) {
		return unmodifiableList(repository.
				executeMapListItem(findElementsByNameOrTag(filter,
													  offset,
													  items),
				ElementSettingsManager::settingsOf));
	}

	public List<ElementSettings> filterElementsByAssetId(String filter, int offset, int items) {
		return unmodifiableList(repository.
				executeMapListItem(findElementsByAssetId(filter,
													     offset,
													     items),
				ElementSettingsManager::settingsOf));	
	}

	public List<ElementSettings> filterElementsBySerialNumber(String filter, int offset, int items) {
		return unmodifiableList(repository.
				executeMapListItem(findElementsBySerialNumber(filter,
													  		  offset,
													  		  items),
				ElementSettingsManager::settingsOf));
	}

	public List<ElementSettings> filterElementsByManagementIP(String filter, int offset, int items) {
		return unmodifiableList(repository.
				executeMapListItem(findElementsByManagementIP(filter,
													  		  offset,
													  		  items),
				ElementSettingsManager::settingsOf));	
	}

}

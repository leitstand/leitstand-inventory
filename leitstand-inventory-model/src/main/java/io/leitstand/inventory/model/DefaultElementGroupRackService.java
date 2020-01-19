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
import static io.leitstand.inventory.model.ElementGroup_Rack.findByRackName;
import static io.leitstand.inventory.model.ElementGroup_Rack.findRacksOfGroup;
import static io.leitstand.inventory.service.ElementGroupRack.newElementGroupRack;
import static io.leitstand.inventory.service.ElementGroupRacks.newElementGroupRacks;
import static io.leitstand.inventory.service.RackItem.newRackItem;
import static io.leitstand.inventory.service.RackSettings.newRackSettings;
import static io.leitstand.inventory.service.ReasonCode.IVT0800E_RACK_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0801I_RACK_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0802I_RACK_REMOVED;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupRack;
import io.leitstand.inventory.service.ElementGroupRackService;
import io.leitstand.inventory.service.ElementGroupRacks;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.RackName;
import io.leitstand.inventory.service.RackSettings;

@Service
public class DefaultElementGroupRackService implements ElementGroupRackService {
	
	private static final Logger LOG = Logger.getLogger(DefaultElementGroupRackService.class.getName());
	
	private static RackSettings settingsOf(ElementGroup_Rack rack) {
		return newRackSettings()
			   .withRackName(rack.getRackName())
			   .withLocation(rack.getLocation())
			   .withDescription(rack.getDescription())
			   .withUnits(rack.getUnits())
			   .build();
	}
	
	@Inject
	@Inventory
	private Repository repository;
	
	@Inject
	private ElementGroupProvider groups;
	
	@Inject
	private Messages messages;
	
	protected DefaultElementGroupRackService() {
		// CDI
	}
	
	@Inject
	protected DefaultElementGroupRackService(@Inventory Repository repository,
											 ElementGroupProvider groups,
											 Messages messages) {
		this.repository = repository;
		this.groups = groups;
		this.messages = messages;
	}

	@Override
	public ElementGroupRacks findRacks(ElementGroupId groupId) {
		ElementGroup group = groups.fetchElementGroup(groupId);
		return findRacks(group);
	}

	@Override
	public ElementGroupRacks findRacks(ElementGroupType groupType, 
									   ElementGroupName groupName) {
		ElementGroup group = groups.fetchElementGroup(groupType, 
											   		  groupName);
		return findRacks(group);
	}
	
	private ElementGroupRacks findRacks(ElementGroup group){
		List<RackSettings> racks = repository.execute(findRacksOfGroup(group))
						 					  .stream()
						 					  .map(rack -> settingsOf(rack))
						 					  .collect(toList());
		
		
		return newElementGroupRacks()
			   .withGroupId(group.getGroupId())
			   .withGroupName(group.getGroupName())
			   .withGroupType(group.getGroupType())
			   .withRacks(racks)
			   .build();
	}
	
	@Override
	public ElementGroupRack getRack(ElementGroupId groupId, 
									RackName rackName) {
		ElementGroup group = groups.fetchElementGroup(groupId);
		return getRackSettings(group,
							   rackName);
	
	}

	@Override
	public ElementGroupRack getRack(ElementGroupType groupType, 
									ElementGroupName groupName, 
									RackName rackName) {
		ElementGroup group = groups.fetchElementGroup(groupType, 
											   		  groupName);
		return getRackSettings(group,
							   rackName);
	}

	private ElementGroupRack getRackSettings(ElementGroup group,
											 RackName rackName) {
		ElementGroup_Rack rack = repository.execute(findByRackName(group, 
													  rackName));
		if(rack == null) {
			LOG.fine( () -> format("%s rack %s not found.", 
								   IVT0800E_RACK_NOT_FOUND.getReasonCode(),
								   rackName));
			throw new EntityNotFoundException(IVT0800E_RACK_NOT_FOUND,
											  rackName);
		}
		
		
		
		return newElementGroupRack()
			   .withGroupId(group.getGroupId())
			   .withGroupType(group.getGroupType())
			   .withGroupName(group.getGroupName())
			   .withRack(settingsOf(rack))
			   .withElements(rack.getElements()
					   			 .stream()
					   			 .map(e -> newRackItem()
					   					   .withElementId(e.getElementId())
					   					   .withElementName(e.getElementName())
					   					   .withElementAlias(e.getElementAlias())
					   					   .withElementRole(e.getElementRoleName())
					   					   .withHalfRack(e.isHalfRack())
					   					   .withHeight(e.getHeight())
					   					   .withHalfRackPosition(e.getHalfRackPosition())
					   					   .withPlatform(e.getPlatform())
					   					   .build())
					   			 .collect(toList()) )
			   .build();	
	}
	
	@Override
	public boolean storeRack(ElementGroupId groupId,
							 RackName rackName,
							 RackSettings settings) {
		ElementGroup group = groups.fetchElementGroup(groupId);	
		return storeRack(group,
						 rackName,
						 settings);	
	}

	@Override
	public boolean storeRack(ElementGroupType groupType,
							 ElementGroupName groupName,
						     RackName rackName,
							 RackSettings settings) {
		ElementGroup group = groups.fetchElementGroup(groupType,
											   		  groupName);	
		return storeRack(group,
				  		 rackName,
				  		 settings);
	}
	
	private boolean storeRack(ElementGroup group,
							  RackName rackName,
							  RackSettings settings) {
		ElementGroup_Rack rack = repository.execute(findByRackName(group,rackName));
		boolean created = false;
		if(rack == null) {
			rack = new ElementGroup_Rack(group,
										 rackName);
			repository.add(rack);
			created = true;
			LOG.fine(() -> format("%s Created new rack %s in %s group %s (%s)",
								  IVT0801I_RACK_STORED.getReasonCode(), 
								  rackName, 
								  group.getGroupType(), 
								  group.getGroupName(),
								  group.getGroupId()));
		} 
		rack.setGroup(group);
		rack.setRackName(settings.getRackName());
		rack.setDescription(settings.getDescription());
		rack.setUnits(settings.getUnits());
		rack.setLocation(settings.getLocation());
		LOG.fine(() -> format("%s Updated rack %s updated in %s group named %s (%s)",
				  			  IVT0801I_RACK_STORED.getReasonCode(), 
				  			  rackName, 
				  			  group.getGroupType(), 
				  			  group.getGroupName(),
				  			  group.getGroupId()));

		

		messages.add(createMessage(IVT0801I_RACK_STORED, 
				   				   group.getGroupType(), 
				   				   group.getGroupName(), 
				   				   rackName));
		return created;
	}

	@Override
	public void removeRack(ElementGroupId groupId, 
						   RackName rackName) {
		ElementGroup group = groups.fetchElementGroup(groupId);
		removeRack(group,
				   rackName);
	}

	@Override
	public void removeRack(ElementGroupType groupType, 
						   ElementGroupName groupName, 
						   RackName rackName) {
		ElementGroup group = groups.fetchElementGroup(groupType,
											   		  groupName);
		removeRack(group,
				   rackName);	
	}

	private void removeRack(ElementGroup group, 
							RackName rackName) {
		ElementGroup_Rack rack = repository.execute(findByRackName(group, 
													  rackName));
		if(rack != null) {
			repository.remove(rack);
			LOG.fine(() -> format("%s Remove rack %s in %s group %s (%s) and all associated mount points!",
								  IVT0801I_RACK_STORED.getReasonCode(), 
								  rackName, 
								  group.getGroupType(), 
								  group.getGroupName(),
								  group.getGroupId()));
			messages.add(createMessage(IVT0802I_RACK_REMOVED, 
					group.getGroupType(), 
					group.getGroupName(), 
					rackName));
		}
	}
	
}

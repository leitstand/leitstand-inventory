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
import static io.leitstand.inventory.service.ElementGroupExport.newElementGroupExport;
import static io.leitstand.inventory.service.ElementGroupsExport.newInventoryExport;
import static io.leitstand.inventory.service.ElementRackLocation.newElementRackLocation;
import static io.leitstand.inventory.service.RackSettings.newRackSettings;
import static io.leitstand.inventory.service.ReasonCode.IVT0101I_GROUP_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0301I_ELEMENT_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0401I_ELEMENT_ROLE_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0801I_RACK_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0901I_PLATFORM_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT1000E_IMPORT_ERROR;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ElementGroupElementsService;
import io.leitstand.inventory.service.ElementGroupExport;
import io.leitstand.inventory.service.ElementGroupExportService;
import io.leitstand.inventory.service.ElementGroupRack;
import io.leitstand.inventory.service.ElementGroupRackService;
import io.leitstand.inventory.service.ElementGroupService;
import io.leitstand.inventory.service.ElementGroupSettings;
import io.leitstand.inventory.service.ElementGroupSettingsService;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.ElementGroupsExport;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementRack;
import io.leitstand.inventory.service.ElementRackService;
import io.leitstand.inventory.service.ElementRoleService;
import io.leitstand.inventory.service.ElementRoleSettings;
import io.leitstand.inventory.service.ElementSettings;
import io.leitstand.inventory.service.ElementSettingsService;
import io.leitstand.inventory.service.PlatformService;
import io.leitstand.inventory.service.PlatformSettings;
import io.leitstand.inventory.service.RackItem;
import io.leitstand.inventory.service.RackSettings;

@ApplicationScoped
public class DefaultElementGroupExportService implements ElementGroupExportService {
	
	private static final Logger LOG = Logger.getLogger(DefaultElementGroupExportService.class.getName());

	@Inject
	private ElementGroupService groupService;

	@Inject
	private ElementGroupSettingsService groupSettingsService;

	@Inject
	private ElementGroupElementsService elementsService;
	
	@Inject
	private ElementSettingsService elementSettingsService;
	
	@Inject
	private PlatformService platformService;
	
	@Inject
	private ElementRoleService roleService;

	@Inject
	private ElementGroupRackService rackService;
	
	@Inject
	private ElementRackService elementRackService;
	
	@Inject
	private Messages messages;
	
	@Override
	public ElementGroupsExport exportElementGroups(ElementGroupType groupType,
												   String filter) {
		// Fetch all groups matching the filter
		List<ElementGroupExport> groups = new LinkedList<>();
		List<ElementGroupRack> racks = new LinkedList<>();
		for(ElementGroupSettings group : groupService.findGroups(groupType,
																 filter, 
																 0, 
																 MAX_VALUE)) {
			groups.add(newElementGroupExport()
					   .withGroup(group)
					   .withElements(loadElementSettings(group))
					   .build());
			
			for(RackSettings rack : rackService.findRacks(group.getGroupId()).getRacks()) {
				racks.add(rackService.getRack(group.getGroupId(), rack.getRackName()));
			}
			
			
		}
		
		return newInventoryExport()
			   .withDateCreated(new Date())
			   .withGroups(groups)
			   .withElementRoles(roleService.getElementRoles())
			   .withPlatforms(platformService.getPlatforms())
			   .build();
	}

	protected List<ElementSettings> loadElementSettings(ElementGroupSettings group) {
		// For each group, fetch all element settings
		List<ElementSettings> export = new LinkedList<>();
		for(ElementId elementId : elementsService.getGroupElements(group.getGroupId())
											 	 .getElements()
											 	 .stream()
											 	 .map(ElementSettings::getElementId)
											 	 .collect(toList())) {
			
			ElementSettings element = elementSettingsService.getElementSettings(elementId);
			export.add(element);
		}
		
		return export;
	}

	@Override
	public void importElementGroups(ElementGroupsExport export) {
		
		importPlatforms(export);
		importElementRoles(export);
		importGroups(export);
		importRacks(export);
		
	}

	private void importGroups(ElementGroupsExport export) {
		for(ElementGroupExport group : export.getGroups()) {
			try {
				groupSettingsService.storeElementGroupSettings(group.getGroup());
				LOG.info(() -> format("%s: Element group %s added (Type: %s)",
									  IVT0101I_GROUP_STORED.getReasonCode(),
						  			  group.getGroup().getGroupName(),
						  			  group.getGroup().getGroupType()));
				messages.add(createMessage(IVT0101I_GROUP_STORED,
										   group.getGroup().getGroupType(),
										   group.getGroup().getGroupName()));
				for(ElementSettings element : group.getElements()) {
					try {
						elementSettingsService.storeElementSettings(element);
						LOG.info(() -> format("%s: Element %s added (Role: %s)",
											  IVT0301I_ELEMENT_STORED.getReasonCode(),
											  element.getElementName(),
											  element.getElementRole()));
						messages.add(createMessage(IVT0301I_ELEMENT_STORED, 
												  element.getElementName()));
					} catch (Exception e) {
						LOG.warning(() -> format("%s: Element %s cannot be stored: %s",
												 IVT1000E_IMPORT_ERROR.getReasonCode(),
								  				 element.getElementName(),
								  				 e.getMessage()));
						messages.add(createMessage(IVT1000E_IMPORT_ERROR, 
									   			   "element",
												   element.getElementName(),
												   e.getMessage()));	
					}
	
				}
			} catch (Exception e) {
				LOG.warning(() ->  format("%s: Element group %s cannot be stored (Type: %s): %s",
						  				  IVT1000E_IMPORT_ERROR.getReasonCode(),
						  				  group.getGroup().getGroupName(),
						  				  group.getGroup().getGroupType(),
						  				  e.getMessage()));
				messages.add(createMessage(IVT1000E_IMPORT_ERROR, 
										   "element "+group.getGroup().getGroupType()+" group",
										   group.getGroup().getGroupName(),
										   e.getMessage()));
			}
		}
	}

	private void importRacks(ElementGroupsExport export) {
		for(ElementRack rack : export.getRacks()) {
			RackSettings settings = newRackSettings()
									.withRackName(rack.getRackName())
									.withUnits(rack.getUnits())
									.withLocation(rack.getLocation())
									.withDescription(rack.getDescription())
									.build();
			try {
				rackService.storeRack(rack.getGroupType(),
									  rack.getGroupName(),
									  settings.getRackName(),
									  settings);
				LOG.info(() -> format ("%s: Rack %s stored",
									   IVT0801I_RACK_STORED.getReasonCode(),
									   rack.getRackName()));
				messages.add(createMessage(IVT0801I_RACK_STORED, 
										   rack.getRackName()));
				for(RackItem item : rack.getElements()) {
					try {
						elementRackService.storeElementRackLocation(item.getElementName(), 
																	  newElementRackLocation()
																	  .withRackName(rack.getRackName())
																	  .withUnit(item.getUnit())
																	  .withPosition(item.getHalfRackPosition())
																	  .build());
						LOG.info(() -> format ("Element %s rack location %s unit %s stored",
								   				item.getElementName(),
												rack.getRackName(),
												item.getHeight()));
					} catch (Exception e) {
						LOG.warning(() ->  format("%s: Element %s rack location %s cannot be stored: %s",
				  				  				  IVT1000E_IMPORT_ERROR.getReasonCode(),
				  				  				  item.getElementName(),
				  				  				  rack.getRackName(),
				  				  				  item.getHeight()));
						messages.add(createMessage(IVT1000E_IMPORT_ERROR, 
								   				   "element "+item.getElementName()+" rack location",
								   				   rack.getRackName()+" "+item.getHeight(),
								   				   e.getMessage()));
					}
				}
			} catch(Exception e) {
				LOG.warning(() ->  format("%s: Rack %s cannot be stored: %s",
						  				  IVT1000E_IMPORT_ERROR.getReasonCode(),
						  				  "rack",
						  				  rack.getRackName(),
						  				  e.getMessage()));
				messages.add(createMessage(IVT1000E_IMPORT_ERROR, 
										   "rack",
										   rack.getRackName(),
										   e.getMessage()));
			}
		}
	}

	private void importElementRoles(ElementGroupsExport export) {
		for(ElementRoleSettings role : export.getRoles()) {
			try {
				roleService.storeElementRole(role);
				LOG.info(() -> format("%s: Element role %s added (Plane: %s, Manageable: %s)",
									  IVT0401I_ELEMENT_ROLE_STORED.getReasonCode(),
									  role.getRoleName(),
									  role.getPlane(),
									  role.isManageable()));
			
				messages.add(createMessage(IVT0401I_ELEMENT_ROLE_STORED, 
										   role.getRoleName()));
			} catch (Exception e) {
				LOG.warning(() ->  format("%s: Element role %s cannot be stored (Plane: %s, Manageable: %s): %s",
										  IVT1000E_IMPORT_ERROR.getReasonCode(),
						  				  role.getRoleName(),
						  				  role.getPlane(),
						  				  role.isManageable(),
						  				  e.getMessage()));
				messages.add(createMessage(IVT1000E_IMPORT_ERROR, 
										   "element role",
						                   role.getRoleName(),
						                   e.getMessage()));
				
			}

		}
	}

	private void importPlatforms(ElementGroupsExport export) {
		for(PlatformSettings platform : export.getPlatforms()) {
			try {
				platformService.storePlatform(platform);
				LOG.info(() -> format("%s: Platform %s %s added",
									  IVT0901I_PLATFORM_STORED.getReasonCode(),
								  	  platform.getVendorName(),
								  	  platform.getModelName()));
				messages.add(createMessage(IVT0901I_PLATFORM_STORED, 
										   platform.getVendorName(),
										   platform.getModelName()));
			} catch (Exception e) {
				LOG.warning(() -> format("%s: Platform %s %s cannot be stored: %s",
										 IVT1000E_IMPORT_ERROR.getReasonCode(),
										 platform.getVendorName(),
										 platform.getModelName(),
										 e.getMessage()));
				
				messages.add(createMessage(IVT1000E_IMPORT_ERROR, 
						   				   "platform",
										   platform.getVendorName()+" "+platform.getModelName(),
										   e.getMessage()));
			}
		}
	}
	
}

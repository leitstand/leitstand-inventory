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

import static io.leitstand.commons.UniqueKeyConstraintViolationException.key;
import static io.leitstand.commons.messages.MessageFactory.createMessage;
import static io.leitstand.commons.model.ObjectUtil.isDifferent;
import static io.leitstand.commons.model.ObjectUtil.optional;
import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.inventory.event.ElementMovedEvent.newElementMovedEvent;
import static io.leitstand.inventory.event.ElementOperationalStateChangedEvent.newElementOperationalStateChangedEvent;
import static io.leitstand.inventory.event.ElementRenamedEvent.newElementRenamedEvent;
import static io.leitstand.inventory.event.ElementRoleChangedEvent.newElementRoleChangedEvent;
import static io.leitstand.inventory.service.ElementGroupSettings.newElementGroupSettings;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementSettings.newElementSettings;
import static io.leitstand.inventory.service.ReasonCode.IVT0301I_ELEMENT_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0307E_ELEMENT_NAME_ALREADY_IN_USE;
import static java.lang.String.format;

import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import io.leitstand.commons.UniqueKeyConstraintViolationException;
import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.event.ElementEvent;
import io.leitstand.inventory.event.ElementMovedEvent;
import io.leitstand.inventory.event.ElementOperationalStateChangedEvent;
import io.leitstand.inventory.event.ElementRenamedEvent;
import io.leitstand.inventory.service.ElementAlias;
import io.leitstand.inventory.service.ElementGroupSettings;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ElementSettings;
import io.leitstand.inventory.service.OperationalState;

@Dependent
public class ElementSettingsManager {

	private static final Logger LOG = Logger.getLogger(ElementSettingsManager.class.getName());

	protected static ElementSettings settingsOf(Element element) {
		return newElementSettings()
			   .withGroupId(element.getGroupId())
			   .withGroupName(element.getGroupName())
			   .withGroupType(element.getGroup().getGroupType())
			   .withElementId(element.getElementId())
			   .withElementName(element.getElementName())
			   .withElementAlias(element.getElementAlias())
			   .withElementRole(element.getElementRoleName())
			   .withAdministrativeState(element.getAdministrativeState())
			   .withOperationalState(element.getOperationalState())
			   .withPlane(element.getElementRole().getPlane())
			   .withSerialNumber(element.getSerialNumber())
			   .withManagementInterfaceMacAddress(element.getManagementInterfaceMacAddress())
			   .withManagementInterfaces(element.getManagementInterfaces())
			   .withDescription(element.getDescription())
			   .withTags(element.getTags())
			   .withPlatformId(optional(element.getPlatform(), Platform::getPlatformId))
			   .withPlatformName(optional(element.getPlatform(), Platform::getPlatformName))
			   .build();
	}
	
	
	private Repository repository;
	private ElementProvider elements;
	private ElementGroupProvider groups;
	private PlatformProvider platforms;
	private ElementRoleProvider roles;
	private Event<ElementEvent> event;

	private Messages messages;
	
	protected ElementSettingsManager() {
		// CDI
	}
	
	@Inject
	protected ElementSettingsManager(@Inventory Repository repository,
									 ElementGroupProvider groups,
									 ElementRoleProvider roles,
									 PlatformProvider platforms,
									 ElementProvider elements,
									 Messages messages,
	                                 Event<ElementEvent> event){
		this.repository = repository;
		this.groups = groups;
		this.roles = roles;
		this.platforms = platforms;
		this.elements = elements;
		this.messages = messages;
		this.event = event;
	}
	
	public void createElement(ElementSettings settings){
		ElementGroup group = null;
		if(settings.getGroupId() != null) {
			group = groups.fetchElementGroup(settings.getGroupId());
		} else {
			group = groups.fetchElementGroup(settings.getGroupType(),
											 settings.getGroupName());
		}
		ElementRole role = roles.fetchElementRole(settings.getElementRole());
		
		assertNoNameAliasConflict(settings.getElementId(),
								  settings.getElementName(), 
								  settings.getElementAlias());
		
		Element element = new Element(group,
									  role,
									  settings.getElementId(),
									  settings.getElementName());
		element.setElementAlias(settings.getElementAlias());
		element.setDescription(settings.getDescription());
		element.setSerialNumber(settings.getSerialNumber());
		element.setManagementInterfaceMacAddress(settings.getManagementInterfaceMacAddress());
		element.setElementManagementInterfaces(settings.getManagementInterfaces().values());
		element.setTags(settings.getTags());
		element.setAdministrativeState(settings.getAdministrativeState());
		element.setOperationalState(settings.getOperationalState());
		element.setPlatform(platforms.findOrCreatePlatform(settings.getPlatformId(), settings.getPlatformName()));
		
		repository.add(element);
		messages.add(createMessage(IVT0301I_ELEMENT_STORED, 
								   element.getElementName()));
		
	}
	
	

	
	
	public void storeElementSettings(Element element, ElementSettings settings) {
		if(!element.hasElementId(settings.getElementId())){
			LOG.fine(() -> format("%s: Element ID is immutable. Request to change it from %s to %s for %s element %s rejected",
								  VAL0003E_IMMUTABLE_ATTRIBUTE.getReasonCode(),
								  element.getElementId(),
								  settings.getElementId(),
								  element.getElementRoleName(),
								  element.getElementName()));
			throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE,
												   "element_id",
												   element.getElementId(),
												   settings.getElementId());
		}
		
		assertNoNameAliasConflict(settings.getElementId(),
								  settings.getElementName(), 
								  settings.getElementAlias());
		
		ElementName previousElementName = element.setElementName(settings.getElementName());
		element.setElementAlias(settings.getElementAlias());
		OperationalState previousOperationalState = element.setOperationalState(settings.getOperationalState());
		element.setDescription(settings.getDescription());
		element.setSerialNumber(settings.getSerialNumber());
		element.setElementManagementInterfaces(settings.getManagementInterfaces().values());
		element.setManagementInterfaceMacAddress(settings.getManagementInterfaceMacAddress());
		element.setTags(settings.getTags());
		element.setAdministrativeState(settings.getAdministrativeState());
		element.setOperationalState(settings.getOperationalState());
		if(isDifferent(element.getElementRoleName(), settings.getElementRole())) {
			ElementRoleName oldRole = element.getElementRoleName();
			ElementRole newRole = roles.fetchElementRole(settings.getElementRole());
			LOG.fine(() -> format("Element %s role changed from %s to %s",
								   element.getElementName(),
								   element.getElementRoleName(),
								   settings.getElementRole()));
			element.setElementRole(newRole);
			event.fire(newElementRoleChangedEvent()
					   .withGroupId(element.getGroupId())
					   .withGroupName(element.getGroupName())
					   .withGroupType(element.getGroupType())
					   .withElementId(element.getElementId())
					   .withElementName(element.getElementName())
					   .withElementAlias(element.getElementAlias())
					   .withElementRole(element.getElementRoleName())
					   .withPreviousElementRole(oldRole)
					   .build());
		}
		
		element.setPlatform(platforms.findOrCreatePlatform(settings.getPlatformId(), 
														   settings.getPlatformName()));	

		
		if(isDifferent(settings.getElementName(), previousElementName)){
			LOG.fine(() -> format("Fire %s for %s element %s -> %s",
								   ElementRenamedEvent.class.getSimpleName(),
								   element.getElementRole(),
								   previousElementName,
								   element.getElementName()));
			event.fire(newElementRenamedEvent()
					   .withGroupId(element.getGroupId())
					   .withGroupName(element.getGroupName())
					   .withGroupType(element.getGroupType())
					   .withElementId(element.getElementId())
					   .withElementName(element.getElementName())
					   .withElementAlias(element.getElementAlias())
					   .withElementRole(element.getElementRoleName())
					   .withPreviousName(previousElementName)
					   .build());
		}
		
		if(isDifferent(settings.getOperationalState(), previousOperationalState)){
			LOG.fine(() -> format("Fire %s for %s element %s: %s -> %s",
								   ElementOperationalStateChangedEvent.class.getSimpleName(),
								   element.getElementRole(),
								   element.getElementName(),
								   previousOperationalState,
								   element.getOperationalState()));
			event.fire(newElementOperationalStateChangedEvent()
					   .withGroupId(element.getGroupId())
					   .withGroupName(element.getGroupName())
					   .withGroupType(element.getGroupType())
					   .withElementId(element.getElementId())
					   .withElementName(element.getElementName())
					   .withElementAlias(element.getElementAlias())
					   .withElementRole(element.getElementRoleName())
					   .withPreviousState(previousOperationalState)
					   .withOperationalState(element.getOperationalState())
					   .build());
		}
		ElementGroup current = element.getGroup();
		ElementGroup expected = groups.fetchElementGroup(settings.getGroupId());
		if(isDifferent(current, expected)){
			LOG.fine(() -> format("Fire %s for %s element %s: %s -> %s",
					   			  ElementMovedEvent.class.getSimpleName(),
					   			  element.getElementRole(),
					   			  element.getElementName(),
					   			  current.getGroupName(),
					   			  element.getGroupName()));
			current.remove(element);
			element.setGroup(expected);
			expected.add(element);
			event.fire(newElementMovedEvent()
					   .withGroupId(element.getGroupId())
					   .withGroupName(element.getGroupName())
					   .withGroupType(element.getGroupType())
					   .withElementId(element.getElementId())
					   .withElementName(element.getElementName())
					   .withElementAlias(element.getElementAlias())
					   .withElementRole(element.getElementRoleName())
					   .withFrom(groupSettings(current))
					   .withTo(groupSettings(expected))
					   .build());
		}

		LOG.fine(() -> format("%s: %s element %s stored.",
							  IVT0301I_ELEMENT_STORED.getReasonCode(),
							  element.getElementRoleName(),
							  element.getElementName()));
		
		
		messages.add(createMessage(IVT0301I_ELEMENT_STORED,
								   element.getElementRoleName(),
								   element.getElementName()));
		
	}

	private ElementGroupSettings groupSettings(ElementGroup current) {
		return newElementGroupSettings()
				   .withGroupId(current.getGroupId())
				   .withGroupType(current.getGroupType())
				   .withGroupName(current.getGroupName())
				   .withTags(current.getTags())
				   .build();
	}

	public ElementSettings getElementSettings(Element element) {
		return settingsOf(element);
	}

	void assertNoNameAliasConflict(ElementId elementId, 
								   ElementName elementName, 
								   ElementAlias elementAlias){
		// Check whether the element alias conflicts with another's element name.
		Element element = elements.tryFetchElement(elementName);
		if(element != null && isDifferent(elementId, element.getElementId())) {
			throw new UniqueKeyConstraintViolationException(IVT0307E_ELEMENT_NAME_ALREADY_IN_USE, 
															key("element_name", elementName));
		}
		// Check whether the element name conflicts with another's element alias.
		element = elements.tryFetchElement(elementName(elementAlias));
		if(element != null && isDifferent(elementId, element.getElementId())) {
			throw new UniqueKeyConstraintViolationException(IVT0307E_ELEMENT_NAME_ALREADY_IN_USE, 
															key("element_alias", elementAlias));

		}
		
	}

	
	
}

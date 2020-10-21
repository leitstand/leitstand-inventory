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

import static io.leitstand.inventory.event.ElementAddedEvent.newElementAddedEvent;
import static io.leitstand.inventory.event.ElementSettingsUpdatedEvent.newElementSettingsUpdatedEvent;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.model.Service;
import io.leitstand.inventory.event.ElementEvent;
import io.leitstand.inventory.event.ElementEvent.ElementEventBuilder;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementSettings;
import io.leitstand.inventory.service.ElementSettingsService;

@Service
public class DefaultElementSettingsService implements ElementSettingsService {
	//TODO Add messages and logging
	@Inject
	private ElementProvider elements;
	
	@Inject
	private ElementSettingsManager inventory;

	@Inject
	private Event<ElementEvent> sink;
	
	public DefaultElementSettingsService() {
		// EJB constructor
	}
	
	DefaultElementSettingsService(ElementSettingsManager inventory, 
								  ElementProvider elements,
								  Event<ElementEvent> sink) {
		this.inventory = inventory;
		this.elements = elements;
		this.sink = sink;
	}

	@Override
	public ElementSettings getElementSettings(ElementId id) {
		Element element = elements.fetchElement(id);
		return inventory.getElementSettings(element);
	}

	@Override
	public ElementSettings getElementSettings(ElementName name) {
		Element element = elements.fetchElement(name);
		return inventory.getElementSettings(element);
	}

	@Override
	public boolean storeElementSettings(ElementSettings settings) {
		try{
			Element element = elements.fetchElement(settings.getElementId());
			inventory.storeElementSettings(element, 
										   settings);
			fire(newElementSettingsUpdatedEvent(),
				 settings);
			return false;
		} catch(EntityNotFoundException e){
			inventory.createElement(settings);
			fire(newElementAddedEvent(),
				 settings);
			return true;
		}
	}
	
	
	private <E extends ElementEvent,B extends ElementEventBuilder<E,B>> void fire(B event, ElementSettings settings) {
		sink.fire(event.withGroupId(settings.getGroupId())
					   .withGroupName(settings.getGroupName())
					   .withGroupType(settings.getGroupType())
					   .withElementId(settings.getElementId())
					   .withElementName(settings.getElementName())
					   .withElementAlias(settings.getElementAlias())
					   .withElementRole(settings.getElementRole())
					   .withAdministrativeState(settings.getAdministrativeState())
					   .withOperationalState(settings.getOperationalState())
					   .withDateModified(settings.getDateModified())
					   .build());
	}
	
}

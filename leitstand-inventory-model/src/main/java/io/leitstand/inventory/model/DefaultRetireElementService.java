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
import static io.leitstand.inventory.event.ElementRetiredEvent.newElementRetiredEvent;
import static io.leitstand.inventory.model.Element_PhysicalInterface.findIfps;
import static io.leitstand.inventory.model.Element_Service.findElementServices;
import static io.leitstand.inventory.service.AdministrativeState.RETIRED;
import static io.leitstand.inventory.service.ReasonCode.IVT0305I_ELEMENT_RETIRED;
import static java.lang.String.format;
import static javax.persistence.LockModeType.OPTIMISTIC_FORCE_INCREMENT;

import java.util.logging.Logger;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.commons.model.Service;
import io.leitstand.inventory.event.ElementEvent;
import io.leitstand.inventory.service.AdministrativeState;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.OperationalState;
import io.leitstand.inventory.service.RetireElementService;

@Service
public class DefaultRetireElementService implements RetireElementService {
	
	private static final Logger LOG = Logger.getLogger(DefaultRetireElementService.class.getName());
	
	@Inject
	private ElementProvider elements;
	
	@Inject
	@Inventory
	private Repository repository;
	
	@Inject
	private Messages messages;

	@Inject
	private Event<ElementEvent> sink;

	@Override
	public void retireElement(ElementId elementId) {
		retireElement(elements.fetchElement(elementId));
	}

	@Override
	public void retireElement(ElementName elementName) {
		retireElement(elements.fetchElement(elementName));
	}
	
	protected void retireElement(Element element) {
		repository.lock(element, OPTIMISTIC_FORCE_INCREMENT);
		
		element.setOperationalState(OperationalState.DOWN);
		element.setAdministrativeState(RETIRED);
		for(Element_PhysicalInterface ifp : repository.execute(findIfps(element))) {
			ifp.setAdministrativeState(AdministrativeState.DOWN);
			ifp.setOperationalState(OperationalState.DOWN);
			ifp.removeNeighbor();
		}
		for(Element_Service service : repository.execute(findElementServices(element))) {
			service.setOperationalState(OperationalState.DOWN);
		}
		LOG.fine(() -> format("Element %s (%s) retired.",
				  			  element.getElementName(), 
				  			  element.getElementId()));
		messages.add(createMessage(IVT0305I_ELEMENT_RETIRED, 
								   element.getElementName()));
		
		sink.fire(newElementRetiredEvent()
				  .withGroupId(element.getGroup().getGroupId())
				  .withGroupName(element.getGroup().getGroupName())
				  .withGroupType(element.getGroup().getGroupType())
				  .withElementId(element.getElementId())
				  .withElementName(element.getElementName())
				  .withElementRole(element.getElementRoleName())
				  .build());
	}

}

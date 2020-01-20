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
import static io.leitstand.inventory.model.Element_ContainerInterface.findIfcByName;
import static io.leitstand.inventory.model.Element_LogicalInterface.findIflByName;
import static io.leitstand.inventory.model.Element_LogicalInterface.findIflsOfElement;
import static io.leitstand.inventory.service.ElementLogicalInterface.newLogicalInterface;
import static io.leitstand.inventory.service.ElementLogicalInterfaceData.newElementLogicalInterfaceData;
import static io.leitstand.inventory.service.ElementLogicalInterfaces.newLogicalInterfaces;
import static io.leitstand.inventory.service.PhysicalInterface.newPhysicalInterfaceInfo;
import static io.leitstand.inventory.service.ReasonCode.IVT0360E_ELEMENT_IFL_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0361I_ELEMENT_IFL_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0362I_ELEMENT_IFL_REMOVED;
import static java.lang.String.format;
import static javax.persistence.LockModeType.OPTIMISTIC_FORCE_INCREMENT;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.event.ElementLogicalInterfaceEvent;
import io.leitstand.inventory.service.ElementLogicalInterface;
import io.leitstand.inventory.service.ElementLogicalInterfaceData;
import io.leitstand.inventory.service.ElementLogicalInterfaceSubmission;
import io.leitstand.inventory.service.ElementLogicalInterfaces;
import io.leitstand.inventory.service.InterfaceName;
import io.leitstand.inventory.service.PhysicalInterface;

@Dependent
public class ElementLogicalInterfaceManager {
	private static final Logger LOG = Logger.getLogger(ElementLogicalInterfaceManager.class.getName());
	
	private Repository repository;
	
	private ElementProvider elements;
	
	private Messages messages;
	
	private Event<ElementLogicalInterfaceEvent> event;
	
	@Inject
	protected ElementLogicalInterfaceManager(@Inventory Repository repository,
									 ElementProvider elements,
									 Messages messages, 
									 Event<ElementLogicalInterfaceEvent> event){
		this.repository = repository;
		this.elements = elements;
		this.messages = messages;
		this.event = event;
	}

	private Element_ContainerInterface getContainerInterface(Element element, InterfaceName name){
		Element_ContainerInterface ifc = repository.execute(findIfcByName(element,name));
		if(ifc == null){
			ifc = new Element_ContainerInterface(element,name);
			repository.add(ifc);
		}
		return ifc;
		
	}

	public ElementLogicalInterface getLogicalInterface(Element element, InterfaceName iflName) {
		Element_LogicalInterface ifl = repository.execute(findIflByName(element,iflName));
		if(ifl == null) {
			LOG.fine(()->format("%s: Logical interface %s at %s does not exist",
								IVT0360E_ELEMENT_IFL_NOT_FOUND.getReasonCode(),
								iflName,
								element.getElementName()));
			throw new EntityNotFoundException(IVT0360E_ELEMENT_IFL_NOT_FOUND, 
											 element.getElementName(), 
											 iflName);
		}
		
		Set<PhysicalInterface> ifps = new TreeSet<>();
		
		for(Element_PhysicalInterface ifp : ifl.getContainerInterface().getPhysicalInterfaces()) {
			ifps.add(newPhysicalInterfaceInfo()
					 .withIfpName(ifp.getIfpName())
					 .withMacAddress(ifp.getMacAddress())
					 .withMtuSize(ifp.getMtuSize())
					 .withOperationalState(ifp.getOperationalState())
					 .withAdministrativeState(ifp.getAdministrativeState())
					 .build());
		}
		
		
		
		ElementLogicalInterfaceData data = newElementLogicalInterfaceData()
										   .withInterfaceName(ifl.getInterfaceName())
										   .withRoutingInstance(ifl.getRoutingInstance())
										   .withVlans(ifl.getVlans())
										   .withOperationalState(ifl.getOperationalState())
										   .withAdministrativeState(ifl.getAdministrativeState())
										   .withAddressInterfaces(ifl.getAddressInterfaces())
										   .withPhysicalInterfaces(ifps)
										   .build();
		
		return newLogicalInterface()
			   .withGroupId(element.getGroup().getGroupId())
			   .withGroupName(element.getGroup().getGroupName())
			   .withGroupType(element.getGroup().getGroupType())
			   .withElementId(element.getElementId())
			   .withElementName(element.getElementName())
			   .withElementAlias(element.getElementAlias())
			   .withLogicalInterface(data)
			   .build();
		
		
	}

	public ElementLogicalInterfaces getLogicalInterfaces(Element element) {
		List<ElementLogicalInterfaceData> ifls = new LinkedList<>();
		for(Element_LogicalInterface ifl : repository.execute(findIflsOfElement(element))){

			Set<PhysicalInterface> ifps = new TreeSet<>();
			
			for(Element_PhysicalInterface ifp : ifl.getContainerInterface().getPhysicalInterfaces()) {
				ifps.add(newPhysicalInterfaceInfo()
						 .withIfpName(ifp.getIfpName())
						 .withIfpAlias(ifp.getIfpAlias())
						 .withIfpClass(ifp.getIfpClass())
						 .withMacAddress(ifp.getMacAddress())
						 .withMtuSize(ifp.getMtuSize())
						 .withOperationalState(ifp.getOperationalState())
						 .withAdministrativeState(ifp.getAdministrativeState())
						 .build());
			}
			
			ifls.add(newElementLogicalInterfaceData()
					 .withInterfaceName(ifl.getInterfaceName())
					 .withAddressInterfaces(ifl.getAddressInterfaces())
					 .withPhysicalInterfaces(ifps)
					 .build());
			
		}
		
		return newLogicalInterfaces()
			   .withGroupId(element.getGroup().getGroupId())
			   .withGroupName(element.getGroup().getGroupName())
			   .withGroupType(element.getGroup().getGroupType())
			   .withElementId(element.getElementId())
			   .withElementName(element.getElementName())
			   .withElementAlias(element.getElementAlias())
			   .withLogicalInterface(ifls)
			   .build();
	}

	public void storeLogicalInterfaces(Element element, List<ElementLogicalInterfaceSubmission> submissions){
		repository.lock(element, OPTIMISTIC_FORCE_INCREMENT);
		// FIXME Implement complete merge (add/update/remove interfaces)
		for(ElementLogicalInterfaceSubmission submission : submissions){
			storeLogicalInterface(element,submission);
			
		}
	}
	
	
	public boolean storeLogicalInterface(Element element, ElementLogicalInterfaceSubmission submission) {
		Element_ContainerInterface ifc = getContainerInterface(element, 
															   submission.getContainerInterfaceName());
		
		Element_LogicalInterface ifl = repository.execute(findIflByName(element, 
																		submission.getIflName()));
		
		boolean created = false;
		
		if(ifl == null){
			ifl = new Element_LogicalInterface(element, ifc, submission.getIflName());
			repository.add(ifl);
			created = true;
		}
		
		ifl.setAddressInterfaces(submission.getAddresses());
		ifl.setContainerInterface(ifc);
		ifl.setRoutingInstance(submission.getRoutingInstance());
		ifl.setVlans(submission.getVlans());
		ifl.setOperationalState(submission.getOperationalState());
		ifl.setAdministrativeState(submission.getAdministrativeState());
		
		LOG.fine(() -> format("%s: Logical interface %s at %s stored.",
							  IVT0361I_ELEMENT_IFL_STORED.getReasonCode(),
							  submission.getIflName(),
							  element.getElementName()));
		
		messages.add(createMessage(IVT0361I_ELEMENT_IFL_STORED, 
								   element.getElementName(),
								   submission.getIflName()));
		
		return created;
	
	}

	public void removeLogicalInterface(Element element, InterfaceName iflName) {
		Element_LogicalInterface ifl = repository.execute(findIflByName(element,iflName));
		if(ifl != null){
			repository.lock(element, OPTIMISTIC_FORCE_INCREMENT);
			Element_ContainerInterface ifc = ifl.getContainerInterface();
			ifc.removeLogicalInterface(ifl);
			repository.remove(ifl);
			LOG.fine(() -> format("%s: Logical interface %s at %s removed.",
					  			  IVT0362I_ELEMENT_IFL_REMOVED.getReasonCode(),
					  			  iflName,
					  			  element.getElementName()));
			messages.add(createMessage(IVT0362I_ELEMENT_IFL_REMOVED, 
									   element.getElementName(), 
									   iflName));
		}
		
	}

}

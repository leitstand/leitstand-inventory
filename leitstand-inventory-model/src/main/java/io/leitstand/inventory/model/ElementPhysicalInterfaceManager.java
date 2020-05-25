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
import static io.leitstand.inventory.event.ElementPhysicalInterfaceRemovedEvent.newPhysicalInterfaceRemovedEvent;
import static io.leitstand.inventory.event.ElementPhysicalInterfaceStoredEvent.newPhysicalInterfaceStoredEvent;
import static io.leitstand.inventory.model.Element_PhysicalInterface.findIfpByName;
import static io.leitstand.inventory.model.Element_PhysicalInterface.findIfps;
import static io.leitstand.inventory.service.ElementPhysicalInterface.newPhysicalInterface;
import static io.leitstand.inventory.service.ElementPhysicalInterfaceData.newPhysicalInterfaceData;
import static io.leitstand.inventory.service.ElementPhysicalInterfaces.newPhysicalInterfaces;
import static io.leitstand.inventory.service.ReasonCode.IVT0300E_ELEMENT_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0350E_ELEMENT_IFP_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0351I_ELEMENT_IFP_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0352I_ELEMENT_IFP_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0353E_ELEMENT_IFP_NOT_REMOVABLE;
import static io.leitstand.inventory.service.ReasonCode.IVT0355W_ELEMENT_IFP_NEIGHBOR_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0356I_ELEMENT_IFP_NEIGHBOR_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0370I_ELEMENT_IFC_STORED;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static javax.persistence.LockModeType.OPTIMISTIC_FORCE_INCREMENT;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.event.ElementPhysicalInterfaceEvent;
import io.leitstand.inventory.service.AdministrativeState;
import io.leitstand.inventory.service.ElementPhysicalInterface;
import io.leitstand.inventory.service.ElementPhysicalInterfaceData;
import io.leitstand.inventory.service.ElementPhysicalInterfaceNeighbor;
import io.leitstand.inventory.service.ElementPhysicalInterfaceSubmission;
import io.leitstand.inventory.service.ElementPhysicalInterfaces;
import io.leitstand.inventory.service.InterfaceName;
import io.leitstand.inventory.service.OperationalState;

@Dependent
public class ElementPhysicalInterfaceManager {
	private static final Logger LOG = Logger.getLogger(ElementPhysicalInterfaceManager.class.getName());
	
	private Repository repository;
	
	private ElementProvider elements;
	
	private Messages messages;
	
	private Event<ElementPhysicalInterfaceEvent> event;
	
	@Inject
	protected ElementPhysicalInterfaceManager(@Inventory Repository repository,
											  ElementProvider elements,
											  Messages messages, 
											  Event<ElementPhysicalInterfaceEvent> event){
		this.repository = repository;
		this.elements = elements;
		this.messages = messages;
		this.event = event;
	}

	protected ElementPhysicalInterfaceManager() {
		//CDI
	}

	public ElementPhysicalInterface getPhysicalInterface(Element element, InterfaceName name) {
		
		Element_PhysicalInterface ifp = repository.execute(findIfpByName(element, name));
		
		if(ifp == null) {
			LOG.fine(()->format("%s: Physical interface %s at element %s does not exist",
								IVT0350E_ELEMENT_IFP_NOT_FOUND.getReasonCode(), 
								name, 
								element.getElementName()));
					
			
			throw new EntityNotFoundException(IVT0350E_ELEMENT_IFP_NOT_FOUND, 
											  element.getElementName(), 
											  name);
		}
		
		ElementPhysicalInterfaceData ifpData = newPhysicalInterfaceData()
											  .withIfpName(ifp.getIfpName())
											  .withIfpAlias(ifp.getIfpAlias())
											  .withCategory(ifp.getCategory())
											  .withBandwidth(ifp.getBandwidth())
											  .withMacAddress(ifp.getMacAddress())
											  .withAdministrativeState(ifp.getAdministrativeState())
											  .withOperationalState(ifp.getOperationalState())
											  .withLogicalInterfaces(ifp.getLogicalInterfaces()
				 												  	    .stream()
				 												  		.map(Element_LogicalInterface::getInterfaceName)
				 												  		.collect(toList()))
											  .withNeighbor(ifp.getNeighbor())
											  .build();
		
		return newPhysicalInterface()
			   .withGroupId(element.getGroup().getGroupId())
			   .withGroupName(element.getGroup().getGroupName())
			   .withGroupType(element.getGroup().getGroupType())
			   .withElementId(element.getElementId())
			   .withElementName(element.getElementName())
			   .withElementAlias(element.getElementAlias())
			   .withPhysicalInterface(ifpData)
			   .build();
	}
	
	public ElementPhysicalInterfaces getPhysicalInterfaces(Element element){
		List<ElementPhysicalInterfaceData> ifps = new LinkedList<>();
		for(Element_PhysicalInterface ifp : repository.execute(findIfps(element))){
			ifps.add(newPhysicalInterfaceData()
					 .withIfpName(ifp.getIfpName())
					 .withIfpAlias(ifp.getIfpAlias())
					 .withCategory(ifp.getCategory())
					 .withBandwidth(ifp.getBandwidth())
					 .withMacAddress(ifp.getMacAddress())
					 .withAdministrativeState(ifp.getAdministrativeState())
					 .withOperationalState(ifp.getOperationalState())
					 .withLogicalInterfaces(ifp.getLogicalInterfaces()
											   .stream()
											   .map(Element_LogicalInterface::getInterfaceName)
											   .collect(toList()))
					 .withNeighbor(ifp.getNeighbor())
					 .build());
		}
		ifps.sort((a,b)->a.getName().compareTo(b.getName()));
		return newPhysicalInterfaces()
			   .withGroupId(element.getGroup().getGroupId())
			   .withGroupName(element.getGroup().getGroupName())
			   .withGroupType(element.getGroup().getGroupType())
			   .withElementId(element.getElementId())
			   .withElementName(element.getElementName())
			   .withElementAlias(element.getElementAlias())
			   .withPhysicalInterfaces(ifps)
			   .build();
	}

	public boolean storePhysicalInterface(Element element, ElementPhysicalInterfaceSubmission submission) {
		Element_ContainerInterface ifc = repository.find(Element_ContainerInterface.class, 
														 new Element_InterfacePK(element, 
																		 	     submission.getIfcName()));
		
		if(ifc == null) {
			ifc = new Element_ContainerInterface(element,submission.getIfcName());
			repository.add(ifc);
			LOG.fine(() -> format("Created new container interface %s for physical interface %s for element %s", 
								  submission.getIfcName(),
								  submission.getIfpName(),
								  element.getElementName()));
			messages.add(createMessage(IVT0370I_ELEMENT_IFC_STORED, 
									   element.getElementName(),
									   submission.getIfcName()));
		}
		
		Element_PhysicalInterface ifp = repository.execute(findIfpByName(element, 
																	     submission.getIfpName()));
		
		boolean created = false;
		if(ifp == null){
			ifp = new Element_PhysicalInterface(element,
												submission.getIfpName(),
												submission.getBandwidth(),
												ifc);
			repository.add(ifp);
			LOG.fine(() -> format("Created new physical interface %s for element %s",
								  submission.getIfpName(),
								  element.getElementName()));
			created = true;
		}
		
		ifp.setMacAddress(submission.getMacAddress());
		ifp.setAdministrativeState(submission.getAdministrativeState());
		ifp.setOperationalState(submission.getOperationalState());
		ifp.setContainerInterface(ifc);
		ifp.setIfpAlias(submission.getIfpAlias());
		ifp.setCategory(submission.getCategory());
		if(submission.getNeighbor() != null ) {
			Element neighborElement = elements.tryFetchElement(submission.getNeighbor().getElementName());
			if(neighborElement != null) {
				LOG.fine(()->format("Registered neighbor interface %s at %s for interface %s at %s",
									neighborElement.getElementName(),
									submission.getNeighbor().getInterfaceName(),
									element.getElementName(),
									submission.getIfpName()));
				ifp.linkTo(neighborElement, submission.getNeighbor().getInterfaceName());
			} else {
				LOG.fine(()->format("%s: Cannot register neighbor for %s at %s. Neighbor element %s does not exist!",
									IVT0355W_ELEMENT_IFP_NEIGHBOR_NOT_FOUND.getReasonCode(),
									submission.getIfpName(),
									element.getElementName(),
									submission.getNeighbor().getElementName()));
				
				messages.add(createMessage(IVT0355W_ELEMENT_IFP_NEIGHBOR_NOT_FOUND,
										   element.getElementName(),
										   submission.getIfpName(),
										   submission.getNeighbor().getElementName()));	
			}
		} else {
			LOG.fine(()->format("%s: Remove neighbor interface for interface %s at %s",
								IVT0356I_ELEMENT_IFP_NEIGHBOR_REMOVED.getReasonCode(),
								element.getElementName(),
								submission.getIfpName()));
			ifp.removeNeighbor();
		}
		
		LOG.fine(()->format("%s: Physical interface %s at element %s stored",
							IVT0351I_ELEMENT_IFP_STORED.getReasonCode(),
							submission.getIfpName(),
							element.getElementName()));

		messages.add(createMessage(IVT0351I_ELEMENT_IFP_STORED,
								   element.getElementName(),
								   submission.getIfpName()));	
		
		event.fire(newPhysicalInterfaceStoredEvent()
				   .withGroupId(element.getGroupId())
				   .withGroupName(element.getGroupName())
				   .withGroupType(element.getGroupType())
				   .withElementId(element.getElementId())
				   .withElementName(element.getElementName())
				   .withElementAlias(element.getElementAlias())
				   .withElementRole(element.getElementRoleName())
				   .withInterfaceName(ifp.getIfpName())
				   .withOperationalState(ifp.getOperationalState())
				   .withAdministrativeState(ifp.getAdministrativeState())
				   .withNeighbor(ifp.getNeighbor())
				   .build());

		
		
		return created;
	}
	
	public void storePhysicalInterfaces(Element element, List<ElementPhysicalInterfaceSubmission> submissions) {
		repository.lock(element, OPTIMISTIC_FORCE_INCREMENT);
		for(ElementPhysicalInterfaceSubmission submission : submissions){
			storePhysicalInterface(element,submission);
		}
	}

	public void removePhysicalInterface(Element element, InterfaceName ifpName) {
		Element_PhysicalInterface ifp = repository.execute(findIfpByName(element, ifpName));
		if(ifp == null){
			return;
		}
		Element_ContainerInterface ifc = ifp.getContainerInterface();
		if(ifc.getLogicalInterfaces().isEmpty()){
			repository.lock(element, OPTIMISTIC_FORCE_INCREMENT);
			ifc.removePhyiscalInterface(ifp);
			if(ifc.getPhysicalInterfaces().isEmpty()){
				repository.remove(ifc);
			}
			repository.remove(ifp);
			LOG.fine(() -> format("%s: Physical interface %s at element %s removed", 
								  IVT0352I_ELEMENT_IFP_REMOVED.getReasonCode(),
								  ifpName,
								  element.getElementName()));	
			messages.add(createMessage(IVT0352I_ELEMENT_IFP_REMOVED, 
									   element.getElementName(),
									   ifpName));
			event.fire(newPhysicalInterfaceRemovedEvent()
					   .withGroupId(element.getGroupId())
					   .withGroupName(element.getGroupName())
					   .withGroupType(element.getGroupType())
					   .withElementId(element.getElementId())
					   .withElementName(element.getElementName())
					   .withElementAlias(element.getElementAlias())
					   .withElementRole(element.getElementRoleName())
					   .withInterfaceName(ifp.getIfpName())
					   .withOperationalState(ifp.getOperationalState())
					   .withAdministrativeState(ifp.getAdministrativeState())
					   .withNeighbor(ifp.getNeighbor())
					   .build());
			return;
		}
		
		LOG.fine(() -> format("%s: Cannot remove physical interface %s at element %s becasue of existing logical interfaces", 
							  IVT0353E_ELEMENT_IFP_NOT_REMOVABLE.getReasonCode(),
							  ifpName,
							  element.getElementName()));
		
		throw new ConflictException(IVT0353E_ELEMENT_IFP_NOT_REMOVABLE,
									"Cannot remove physical interface {0} because of {1} assigned logical interfaces.", 
									ifpName,
									ifc.getLogicalInterfaces().size());
		
	}

	public void storePhysicalNeighborInterface(Element element, 
											   InterfaceName ifpName, 
											   ElementPhysicalInterfaceNeighbor link) {
		Element_PhysicalInterface ifp = repository.execute(findIfpByName(element, ifpName));
		if(ifp == null) {
			LOG.fine(() -> format("%s: Physical interface %s at %s does not exist",
								  IVT0350E_ELEMENT_IFP_NOT_FOUND,
								  ifpName,
								  element.getElementName()));
			throw new EntityNotFoundException(IVT0350E_ELEMENT_IFP_NOT_FOUND, 
											  element.getElementName(),
											  ifpName);
		}
		Element linkedElement = elements.tryFetchElement(link.getElementName());
		if(linkedElement == null) {
			LOG.fine(() -> format("%s: Neighbor element %s does not exist",
								  IVT0300E_ELEMENT_NOT_FOUND,
								  link.getElementName()));
			throw new EntityNotFoundException(IVT0300E_ELEMENT_NOT_FOUND,
											  link.getElementName());
		}
		ifp.linkTo(linkedElement, link.getInterfaceName());
		event.fire(newPhysicalInterfaceStoredEvent()
				   .withGroupId(element.getGroupId())
				   .withGroupName(element.getGroupName())
				   .withGroupType(element.getGroupType())
				   .withElementId(element.getElementId())
				   .withElementName(element.getElementName())
				   .withElementAlias(element.getElementAlias())
				   .withElementRole(element.getElementRoleName())
				   .withInterfaceName(ifp.getIfpName())
				   .withOperationalState(ifp.getOperationalState())
				   .withAdministrativeState(ifp.getAdministrativeState())
				   .withNeighbor(ifp.getNeighbor())
				   .build());
	}

	public void updatePhysicalLinkOperationalState(Element element, 
												   InterfaceName ifpName,
												   OperationalState opState) {
		Element_PhysicalInterface ifp = repository.execute(findIfpByName(element,ifpName));
		if(ifp == null) {
			LOG.fine(() -> format("%s: Physical interface %s at %s does not exist",
								  IVT0350E_ELEMENT_IFP_NOT_FOUND.getReasonCode(),
								  ifpName,
								  element.getElementName()));	
			throw new EntityNotFoundException(IVT0350E_ELEMENT_IFP_NOT_FOUND, 
											  element.getElementName(),
											  ifpName);
		}
		LOG.fine(() -> format("Operational state of physical interface %s at %s set to %s",
							  ifpName,
							  element.getElementName(),
							  opState));	
		ifp.setOperationalState(opState);
		event.fire(newPhysicalInterfaceStoredEvent()
				   .withGroupId(element.getGroupId())
				   .withGroupName(element.getGroupName())
				   .withGroupType(element.getGroupType())
				   .withElementId(element.getElementId())
				   .withElementName(element.getElementName())
				   .withElementAlias(element.getElementAlias())
				   .withElementRole(element.getElementRoleName())
				   .withInterfaceName(ifp.getIfpName())
				   .withOperationalState(ifp.getOperationalState())
				   .withAdministrativeState(ifp.getAdministrativeState())
				   .withNeighbor(ifp.getNeighbor())
				   .build());
	}
	
	public void updatePhysicalLinkAdministrativeState(Element element, InterfaceName ifpName, 
													  AdministrativeState admState) {
		Element_PhysicalInterface ifp = repository.execute(findIfpByName(element,ifpName));
		if(ifp == null) {
			LOG.fine(() -> format("%s: Physical interface %s at %s does not exist",
					  			  IVT0350E_ELEMENT_IFP_NOT_FOUND.getReasonCode(),
					  			  ifpName,
					  			  element.getElementName()));	
			throw new EntityNotFoundException(IVT0350E_ELEMENT_IFP_NOT_FOUND, 
											  element.getElementName(),
											  ifpName);
		}
		LOG.fine(() -> format("Administrative state of physical interface %s at %s set to %s",
				  			  ifpName,
				  			  element.getElementName(),
				  			  admState));	
		ifp.setAdministrativeState(admState);
		event.fire(newPhysicalInterfaceStoredEvent()
				   .withGroupId(element.getGroupId())
				   .withGroupName(element.getGroupName())
				   .withGroupType(element.getGroupType())
				   .withElementId(element.getElementId())
				   .withElementName(element.getElementName())
				   .withElementAlias(element.getElementAlias())
				   .withElementRole(element.getElementRoleName())
				   .withInterfaceName(ifp.getIfpName())
				   .withOperationalState(ifp.getOperationalState())
				   .withAdministrativeState(ifp.getAdministrativeState())
				   .withNeighbor(ifp.getNeighbor())
				   .build());
	}

	public void removePhysicalInterfaceNeighbor(Element element, InterfaceName ifpName) {
		Element_PhysicalInterface ifp = repository.execute(findIfpByName(element,ifpName));
		if(ifp == null) {
			LOG.fine(() -> format("%s: Physical interface %s at %s does not exist",
								  IVT0350E_ELEMENT_IFP_NOT_FOUND.getReasonCode(),
								  ifpName,
								  element.getElementName()));	
			throw new EntityNotFoundException(IVT0350E_ELEMENT_IFP_NOT_FOUND, 
											  element.getElementName(),
											  ifpName);
		}
		ifp.removeNeighbor();
		event.fire(newPhysicalInterfaceStoredEvent()
				   .withGroupId(element.getGroupId())
				   .withGroupName(element.getGroupName())
				   .withGroupType(element.getGroupType())
				   .withElementId(element.getElementId())
				   .withElementName(element.getElementName())
				   .withElementRole(element.getElementRoleName())
				   .withInterfaceName(ifp.getIfpName())
				   .withOperationalState(ifp.getOperationalState())
				   .withAdministrativeState(ifp.getAdministrativeState())
				   .withNeighbor(ifp.getNeighbor())
				   .build());
	}
	
}

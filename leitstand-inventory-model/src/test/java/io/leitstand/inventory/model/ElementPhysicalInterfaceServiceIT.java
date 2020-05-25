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

import static io.leitstand.inventory.model.Element.findElementByName;
import static io.leitstand.inventory.model.ElementGroup.findElementGroupByName;
import static io.leitstand.inventory.model.ElementRole.findRoleByName;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementPhysicalInterfaceNeighbor.newPhysicalInterfaceNeighbor;
import static io.leitstand.inventory.service.ElementPhysicalInterfaceSubmission.newPhysicalInterfaceSubmission;
import static io.leitstand.inventory.service.Plane.DATA;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import javax.enterprise.event.Event;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.event.ElementPhysicalInterfaceEvent;
import io.leitstand.inventory.event.ElementPhysicalInterfaceRemovedEvent;
import io.leitstand.inventory.event.ElementPhysicalInterfaceStoredEvent;
import io.leitstand.inventory.service.AdministrativeState;
import io.leitstand.inventory.service.Bandwidth;
import io.leitstand.inventory.service.ElementPhysicalInterface;
import io.leitstand.inventory.service.ElementPhysicalInterfaceNeighbor;
import io.leitstand.inventory.service.ElementPhysicalInterfaceService;
import io.leitstand.inventory.service.ElementPhysicalInterfaceSubmission;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.InterfaceName;
import io.leitstand.inventory.service.MACAddress;
import io.leitstand.inventory.service.OperationalState;
import io.leitstand.inventory.service.ReasonCode;

public class ElementPhysicalInterfaceServiceIT extends InventoryIT{

	private Element element;
	private Element neighbor;
	private ElementPhysicalInterfaceService service;
	private Event<ElementPhysicalInterfaceEvent> event;
	private Messages messages;
	
	@Before
	public void initTestEnvironment() {
		Repository repository = new Repository(getEntityManager());
		event = mock(Event.class);
		messages = mock(Messages.class);
		ElementProvider elements = new ElementProvider(repository);
		ElementPhysicalInterfaceManager manager = new ElementPhysicalInterfaceManager(repository,
																					  elements,
																					  messages,
																					  event);
		service = new DefaultElementPhysicalInterfaceService(elements,manager);
		
		transaction(()->{
			ElementGroup group = repository.addIfAbsent(findElementGroupByName(groupType("unittest"), 
																			   groupName("interfaces")),
														() -> new ElementGroup(randomGroupId(),
																			   groupType("unittest"), 
																			   groupName("interfaces")));
			
			ElementRole role = repository.addIfAbsent(findRoleByName(ElementRoleName.valueOf("unittest")),
													   () -> new ElementRole(ElementRoleName.valueOf("unittest"),
															   				 DATA));
			
			element = repository.addIfAbsent(findElementByName(elementName("ifc_test_element")),
									 		 () -> new Element(group,
									 				 		   role,
									 				 		   randomElementId(), 
									 				 		   elementName("ifc_test_element")));	
			
			neighbor = repository.addIfAbsent(findElementByName(elementName("ifc_neighbor_element")),
			 		    					  () -> new Element(group,
			 		    							  			role,
			 		    							  			randomElementId(), 
			 		    							  			elementName("ifc_neighbor_element")));	
			
		});
		
	}
	
	
	@Test
	public void store_physical_interface_without_neighbor() {
		ElementPhysicalInterfaceSubmission submission = newPhysicalInterfaceSubmission()
														.withAdministrativeState(AdministrativeState.UP)
														.withBandwidth(new Bandwidth("100.000 Mbps"))
														.withIfpAlias("IFP alias")
														.withCategory("IFP category")
														.withIfpName(new InterfaceName("ifp-0/0/0"))
														.withMacAddress(new MACAddress("00:11:22:33:44:55:66"))
														.withOperationalState(OperationalState.UP)
														.build();
		ArgumentCaptor<ElementPhysicalInterfaceEvent> eventCaptor = forClass(ElementPhysicalInterfaceEvent.class);
		doNothing().when(event).fire(eventCaptor.capture());
		
		transaction(()->{
			service.storePhysicalInterface(element.getElementName(), submission);
			assertThat(eventCaptor.getValue(),is(ElementPhysicalInterfaceStoredEvent.class));
		});
		
		transaction(() -> {
			ElementPhysicalInterface ifp = service.getPhysicalInterface(element.getElementName(), submission.getIfpName());
			
			assertEquals(submission.getAdministrativeState(), ifp.getPhysicalInterface().getAdministrativeState());
			assertEquals(submission.getBandwidth(), ifp.getPhysicalInterface().getBandwidth());
			assertEquals(submission.getIfpAlias(), ifp.getPhysicalInterface().getIfpAlias());
			assertEquals(submission.getCategory(), ifp.getPhysicalInterface().getCategory());
			assertEquals(submission.getMacAddress(), ifp.getPhysicalInterface().getMacAddress());
			assertEquals(submission.getOperationalState(), ifp.getPhysicalInterface().getOperationalState());
			assertNull(ifp.getPhysicalInterface().getNeighbor());
			assertTrue(ifp.getPhysicalInterface().getLogicalInterfaces().isEmpty());
			
		});
		
	}
	
	@Test
	public void store_physical_interface_with_neighbor() {
		ElementPhysicalInterfaceSubmission submission = newPhysicalInterfaceSubmission()
														.withAdministrativeState(AdministrativeState.UP)
														.withBandwidth(new Bandwidth("100.000 Mbps"))
														.withIfpAlias("IFP alias")
														.withCategory("IFP category")
														.withIfpName(new InterfaceName("ifp-0/0/1"))
														.withMacAddress(new MACAddress("00:11:22:33:44:55:66"))
														.withOperationalState(OperationalState.UP)
														.withNeighbor(newPhysicalInterfaceNeighbor()
																	  .withElementId(neighbor.getElementId())
																	  .withElementName(neighbor.getElementName())
																	  .withInterfaceName(new InterfaceName("ifp-0/0/1")))
														.build();
		ArgumentCaptor<ElementPhysicalInterfaceEvent> eventCaptor = forClass(ElementPhysicalInterfaceEvent.class);
		doNothing().when(event).fire(eventCaptor.capture());
		
		transaction(()->{
			service.storePhysicalInterface(element.getElementName(), submission);
			assertThat(eventCaptor.getValue(),is(ElementPhysicalInterfaceStoredEvent.class));
		});
		
		transaction(() -> {
			ElementPhysicalInterface ifp = service.getPhysicalInterface(element.getElementName(), submission.getIfpName());
			
			assertEquals(submission.getAdministrativeState(), ifp.getPhysicalInterface().getAdministrativeState());
			assertEquals(submission.getBandwidth(), ifp.getPhysicalInterface().getBandwidth());
			assertEquals(submission.getIfpAlias(), ifp.getPhysicalInterface().getIfpAlias());
			assertEquals(submission.getCategory(), ifp.getPhysicalInterface().getCategory());
			assertEquals(submission.getMacAddress(), ifp.getPhysicalInterface().getMacAddress());
			assertEquals(submission.getOperationalState(), ifp.getPhysicalInterface().getOperationalState());
			assertNotNull(ifp.getPhysicalInterface().getNeighbor());
			assertEquals(submission.getNeighbor(),ifp.getPhysicalInterface().getNeighbor());
			assertTrue(ifp.getPhysicalInterface().getLogicalInterfaces().isEmpty());
		});
	}
	
	@Test
	public void store_physical_interface_neighbor() {
		ElementPhysicalInterfaceSubmission submission = newPhysicalInterfaceSubmission()
														.withAdministrativeState(AdministrativeState.UP)
														.withBandwidth(new Bandwidth("100.000 Mbps"))
														.withIfpAlias("IFP alias")
														.withCategory("IFP category")
														.withIfpName(new InterfaceName("ifp-0/0/2"))
														.withMacAddress(new MACAddress("00:11:22:33:44:55:66"))
														.withOperationalState(OperationalState.UP)
														.build();
		ArgumentCaptor<ElementPhysicalInterfaceEvent> eventCaptor = forClass(ElementPhysicalInterfaceEvent.class);
		doNothing().when(event).fire(eventCaptor.capture());
		
		transaction(()->{
			service.storePhysicalInterface(element.getElementName(), submission);
			assertThat(eventCaptor.getValue(),is(ElementPhysicalInterfaceStoredEvent.class));
		});
		
		ElementPhysicalInterfaceNeighbor neighborSubmission = newPhysicalInterfaceNeighbor()
															  .withElementId(neighbor.getElementId())
															  .withElementName(neighbor.getElementName())
															  .withInterfaceName(new InterfaceName("ifp-0/0/2"))
															  .build();
		
		transaction(() -> {
			ElementPhysicalInterface ifp = service.getPhysicalInterface(element.getElementName(), submission.getIfpName());
			assertNull(ifp.getPhysicalInterface().getNeighbor());
			service.storePhysicalInterfaceNeighbor(element.getElementName(), 
												   submission.getIfpName(), 
												   neighborSubmission);
		});		
		
		transaction(() -> {
			ElementPhysicalInterface ifp = service.getPhysicalInterface(element.getElementName(), submission.getIfpName());
			assertEquals(neighborSubmission, ifp.getPhysicalInterface().getNeighbor());
		});
	}
	
	@Test
	public void remove_physical_interface_neighbor() {
		ElementPhysicalInterfaceSubmission submission = newPhysicalInterfaceSubmission()
														.withAdministrativeState(AdministrativeState.UP)
														.withBandwidth(new Bandwidth("100.000 Mbps"))
														.withIfpAlias("IFP alias")
														.withCategory("IFP category")
														.withIfpName(new InterfaceName("ifp-0/0/3"))
														.withMacAddress(new MACAddress("00:11:22:33:44:55:66"))
														.withOperationalState(OperationalState.UP)
														.withNeighbor(newPhysicalInterfaceNeighbor()
																	  .withElementId(neighbor.getElementId())
																	  .withElementName(neighbor.getElementName())
																	  .withInterfaceName(new InterfaceName("ifp-0/0/1")))
														.build();
		ArgumentCaptor<ElementPhysicalInterfaceEvent> eventCaptor = forClass(ElementPhysicalInterfaceEvent.class);
		doNothing().when(event).fire(eventCaptor.capture());

		transaction(()->{
			service.storePhysicalInterface(element.getElementName(), submission);
			assertThat(eventCaptor.getValue(),is(ElementPhysicalInterfaceStoredEvent.class));
		});

		transaction(() -> {
			ElementPhysicalInterface ifp = service.getPhysicalInterface(element.getElementName(), submission.getIfpName());
			assertEquals(submission.getNeighbor(),ifp.getPhysicalInterface().getNeighbor());
		});
		
		transaction(() -> {
			service.removePhysicalInterfaceNeighbor(element.getElementName(), submission.getIfpName());
		});
		
		transaction(() -> {
			ElementPhysicalInterface ifp = service.getPhysicalInterface(element.getElementName(), submission.getIfpName());
			assertNull(ifp.getPhysicalInterface().getNeighbor());
		});

	}
	
	@Test
	public void remove_physical_interface() {
		ElementPhysicalInterfaceSubmission submission = newPhysicalInterfaceSubmission()
														.withAdministrativeState(AdministrativeState.UP)
														.withBandwidth(new Bandwidth("100.000 Mbps"))
														.withIfpAlias("IFP alias")
														.withCategory("IFP category")
														.withIfpName(new InterfaceName("ifp-0/0/3"))
														.withMacAddress(new MACAddress("00:11:22:33:44:55:66"))
														.withOperationalState(OperationalState.UP)
														.withNeighbor(newPhysicalInterfaceNeighbor()
																	  .withElementId(neighbor.getElementId())
																	  .withElementName(neighbor.getElementName())
																	  .withInterfaceName(new InterfaceName("ifp-0/0/1")))
														.build();
		ArgumentCaptor<ElementPhysicalInterfaceEvent> eventCaptor = forClass(ElementPhysicalInterfaceEvent.class);
		doNothing().when(event).fire(eventCaptor.capture());

		transaction(()->{
			service.storePhysicalInterface(element.getElementName(), submission);
		});

		transaction(() -> {
			service.removePhysicalInterface(element.getElementName(), submission.getIfpName());
			assertThat(eventCaptor.getValue(),is(ElementPhysicalInterfaceRemovedEvent.class));
		});
		
		transaction(() -> {
			try {
				service.getPhysicalInterface(element.getElementName(), submission.getIfpName());
				fail("Exception expected");
			} catch (EntityNotFoundException e) {
				assertEquals(ReasonCode.IVT0350E_ELEMENT_IFP_NOT_FOUND,e.getReason());
			}
		});
	}
	
	@Test
	public void update_physical_interface_administrative_state() {
		ElementPhysicalInterfaceSubmission submission = newPhysicalInterfaceSubmission()
														.withAdministrativeState(AdministrativeState.UP)
														.withBandwidth(new Bandwidth("100.000 Mbps"))
														.withIfpAlias("IFP alias")
														.withCategory("IFP category")
														.withIfpName(new InterfaceName("ifp-0/0/4"))
														.withMacAddress(new MACAddress("00:11:22:33:44:55:66"))
														.withOperationalState(OperationalState.UP)
														.withNeighbor(newPhysicalInterfaceNeighbor()
																	  .withElementId(neighbor.getElementId())
																	  .withElementName(neighbor.getElementName())
																	  .withInterfaceName(new InterfaceName("ifp-0/0/1")))
														.build();
		ArgumentCaptor<ElementPhysicalInterfaceEvent> eventCaptor = forClass(ElementPhysicalInterfaceEvent.class);
		doNothing().when(event).fire(eventCaptor.capture());

		transaction(()->{
			service.storePhysicalInterface(element.getElementName(), submission);
			assertThat(eventCaptor.getValue(),is(ElementPhysicalInterfaceStoredEvent.class));
		});

		transaction(() -> {
			service.updatePhysicalInterfaceAdministrativeState(element.getElementName(), submission.getIfpName(),AdministrativeState.DOWN);
		});
		
		transaction(() -> {
			assertEquals(AdministrativeState.DOWN, service.getPhysicalInterface(element.getElementName(), submission.getIfpName()).getPhysicalInterface().getAdministrativeState());
		});
	}
	
	
	@Test
	public void update_physical_interface_operational_state() {
		ElementPhysicalInterfaceSubmission submission = newPhysicalInterfaceSubmission()
														.withAdministrativeState(AdministrativeState.UP)
														.withBandwidth(new Bandwidth("100.000 Mbps"))
														.withIfpAlias("IFP alias")
														.withCategory("IFP category")
														.withIfpName(new InterfaceName("ifp-0/0/5"))
														.withMacAddress(new MACAddress("00:11:22:33:44:55:66"))
														.withOperationalState(OperationalState.UP)
														.withNeighbor(newPhysicalInterfaceNeighbor()
																	  .withElementId(neighbor.getElementId())
																	  .withElementName(neighbor.getElementName())
																	  .withInterfaceName(new InterfaceName("ifp-0/0/1")))
														.build();
		ArgumentCaptor<ElementPhysicalInterfaceEvent> eventCaptor = forClass(ElementPhysicalInterfaceEvent.class);
		doNothing().when(event).fire(eventCaptor.capture());

		transaction(()->{
			service.storePhysicalInterface(element.getElementName(), submission);
			assertThat(eventCaptor.getValue(),is(ElementPhysicalInterfaceStoredEvent.class));
		});

		transaction(() -> {
			service.updatePhysicalInterfaceOperationalState(element.getElementName(), submission.getIfpName(),OperationalState.DOWN);
		});
		
		transaction(() -> {
			assertEquals(OperationalState.DOWN, service.getPhysicalInterface(element.getElementName(), submission.getIfpName()).getPhysicalInterface().getOperationalState());
		});
	}
	
	
}

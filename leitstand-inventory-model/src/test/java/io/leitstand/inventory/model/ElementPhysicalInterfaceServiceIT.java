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
import static io.leitstand.inventory.service.Bandwidth.bandwidth;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementPhysicalInterfaceFilter.ifpFilter;
import static io.leitstand.inventory.service.ElementPhysicalInterfaceNeighbor.newPhysicalInterfaceNeighbor;
import static io.leitstand.inventory.service.ElementPhysicalInterfaceSubmission.newPhysicalInterfaceSubmission;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.InterfaceName.interfaceName;
import static io.leitstand.inventory.service.MACAddress.macAddress;
import static io.leitstand.inventory.service.Plane.DATA;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.hasSizeOf;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import java.util.List;

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
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementPhysicalInterface;
import io.leitstand.inventory.service.ElementPhysicalInterfaceNeighbor;
import io.leitstand.inventory.service.ElementPhysicalInterfaceService;
import io.leitstand.inventory.service.ElementPhysicalInterfaceSubmission;
import io.leitstand.inventory.service.ElementPhysicalInterfaces;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.InterfaceName;
import io.leitstand.inventory.service.OperationalState;
import io.leitstand.inventory.service.ReasonCode;

public class ElementPhysicalInterfaceServiceIT extends InventoryIT{
    
    private static final ElementGroupId GROUP_ID = randomGroupId();
    private static final ElementGroupName GROUP_NAME = groupName("group");
    private static final ElementGroupType GROUP_TYPE = groupType("unittest");
    private static final ElementId ELEMENT_ID = randomElementId();
    private static final ElementName ELEMENT_NAME = elementName("element");
    private static final ElementRoleName ELEMENT_ROLE = elementRoleName("role");
    private static final ElementId NEIGHBOR_ID = randomElementId();
    private static final ElementName NEIGHBOR_NAME = elementName("neighbor");
    private static final InterfaceName IFP_NAME = interfaceName("ifp-0/0/0");
    private static final InterfaceName NEIGHBOR_IFP_NAME = interfaceName("ifp-0/0/25");
    private static final InterfaceName IFC_NAME = interfaceName("ifp-0/0/0/0");
    private static final InterfaceName IFL_NAME = interfaceName("ifl-0/0/0/0/0");
    

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
			ElementGroup group = repository.addIfAbsent(findElementGroupByName(GROUP_TYPE, 
																			   GROUP_NAME),
														() -> new ElementGroup(GROUP_ID,
																			   GROUP_TYPE, 
																			   GROUP_NAME));
			
			ElementRole role = repository.addIfAbsent(findRoleByName(ELEMENT_ROLE),
													   () -> new ElementRole(ELEMENT_ROLE,
															   				 DATA));
			
			repository.addIfAbsent(findElementByName(ELEMENT_NAME),
			                       () -> new Element(group,
										 		     role,
									 				 ELEMENT_ID, 
									 				 ELEMENT_NAME));	
			
			repository.addIfAbsent(findElementByName(NEIGHBOR_NAME),
			 		    		   () -> new Element(group,
			 		    			  	  			 role,
			 		    							 NEIGHBOR_ID, 
			 		    							 NEIGHBOR_NAME));	
			
		});
		
	}
	
	@Test
	public void store_physical_interface_without_neighbor() {
		ElementPhysicalInterfaceSubmission submission = newPhysicalInterfaceSubmission()
														.withAdministrativeState(AdministrativeState.UP)
														.withBandwidth(bandwidth("100.000 Mbps"))
														.withIfpAlias("IFP alias")
														.withCategory("IFP category")
														.withIfpName(IFP_NAME)
														.withMacAddress(macAddress("00:11:22:33:44:55"))
														.withOperationalState(OperationalState.UP)
														.build();
		ArgumentCaptor<ElementPhysicalInterfaceEvent> eventCaptor = forClass(ElementPhysicalInterfaceEvent.class);
		doNothing().when(event).fire(eventCaptor.capture());
		
		transaction(()->{
			service.storePhysicalInterface(ELEMENT_NAME, submission);
			assertThat(eventCaptor.getValue(),is(ElementPhysicalInterfaceStoredEvent.class));
		});
		
		transaction(() -> {
			ElementPhysicalInterface ifp = service.getPhysicalInterface(ELEMENT_NAME, IFP_NAME);
			
			assertEquals(submission.getAdministrativeState(), ifp.getPhysicalInterface().getAdministrativeState());
			assertEquals(submission.getBandwidth(), ifp.getPhysicalInterface().getBandwidth());
			assertEquals(submission.getIfpAlias(), ifp.getPhysicalInterface().getIfpAlias());
			assertEquals(submission.getCategory(), ifp.getPhysicalInterface().getCategory());
			assertEquals(submission.getMacAddress(), ifp.getPhysicalInterface().getMacAddress());
			assertEquals(submission.getOperationalState(), ifp.getPhysicalInterface().getOperationalState());
			assertNull(ifp.getPhysicalInterface().getNeighbor());
			
		});
		
	}
	
	@Test
	public void store_physical_interface_with_neighbor() {
		ElementPhysicalInterfaceSubmission submission = newPhysicalInterfaceSubmission()
														.withAdministrativeState(AdministrativeState.UP)
														.withBandwidth(bandwidth("100.000 Mbps"))
														.withIfpAlias("IFP alias")
														.withCategory("IFP category")
														.withIfpName(IFP_NAME)
														.withMacAddress(macAddress("00:11:22:33:44:55"))
														.withOperationalState(OperationalState.UP)
														.withNeighbor(newPhysicalInterfaceNeighbor()
																	  .withElementId(NEIGHBOR_ID)
																	  .withElementName(NEIGHBOR_NAME)
																	  .withInterfaceName(NEIGHBOR_IFP_NAME))
														.build();
		ArgumentCaptor<ElementPhysicalInterfaceEvent> eventCaptor = forClass(ElementPhysicalInterfaceEvent.class);
		doNothing().when(event).fire(eventCaptor.capture());
		
		transaction(()->{
			service.storePhysicalInterface(ELEMENT_NAME, submission);
			assertThat(eventCaptor.getValue(),is(ElementPhysicalInterfaceStoredEvent.class));
		});
		
		transaction(() -> {
			ElementPhysicalInterface ifp = service.getPhysicalInterface(ELEMENT_NAME, IFP_NAME);
			
			assertEquals(submission.getAdministrativeState(), ifp.getPhysicalInterface().getAdministrativeState());
			assertEquals(submission.getBandwidth(), ifp.getPhysicalInterface().getBandwidth());
			assertEquals(submission.getIfpAlias(), ifp.getPhysicalInterface().getIfpAlias());
			assertEquals(submission.getCategory(), ifp.getPhysicalInterface().getCategory());
			assertEquals(submission.getMacAddress(), ifp.getPhysicalInterface().getMacAddress());
			assertEquals(submission.getOperationalState(), ifp.getPhysicalInterface().getOperationalState());
			assertNotNull(ifp.getPhysicalInterface().getNeighbor());
			assertEquals(submission.getNeighbor(),ifp.getPhysicalInterface().getNeighbor());
		});
	}
	
	@Test
	public void store_physical_interface_neighbor() {
		ElementPhysicalInterfaceSubmission submission = newPhysicalInterfaceSubmission()
														.withAdministrativeState(AdministrativeState.UP)
														.withBandwidth(bandwidth("100.000 Mbps"))
														.withIfpAlias("IFP alias")
														.withCategory("IFP category")
														.withIfpName(IFP_NAME)
														.withMacAddress(macAddress("00:11:22:33:44:55"))
														.withOperationalState(OperationalState.UP)
														.build();
		ArgumentCaptor<ElementPhysicalInterfaceEvent> eventCaptor = forClass(ElementPhysicalInterfaceEvent.class);
		doNothing().when(event).fire(eventCaptor.capture());
		
		transaction(()->{
			service.storePhysicalInterface(ELEMENT_NAME, submission);
			assertThat(eventCaptor.getValue(),is(ElementPhysicalInterfaceStoredEvent.class));
		});
		
		ElementPhysicalInterfaceNeighbor neighborSubmission = newPhysicalInterfaceNeighbor()
															  .withElementId(NEIGHBOR_ID)
															  .withElementName(NEIGHBOR_NAME)
															  .withInterfaceName(NEIGHBOR_IFP_NAME)
															  .build();
		
		transaction(() -> {
			ElementPhysicalInterface ifp = service.getPhysicalInterface(ELEMENT_NAME, IFP_NAME);
			assertNull(ifp.getPhysicalInterface().getNeighbor());
			service.storePhysicalInterfaceNeighbor(ELEMENT_NAME, 
												   IFP_NAME, 
												   neighborSubmission);
		});		
		
		transaction(() -> {
			ElementPhysicalInterface ifp = service.getPhysicalInterface(ELEMENT_NAME, IFP_NAME);
			assertEquals(neighborSubmission, ifp.getPhysicalInterface().getNeighbor());
		});
	}
	
	@Test
	public void remove_physical_interface_neighbor() {
		ElementPhysicalInterfaceSubmission submission = newPhysicalInterfaceSubmission()
														.withAdministrativeState(AdministrativeState.UP)
														.withBandwidth(bandwidth("100.000 Mbps"))
														.withIfpAlias("IFP alias")
														.withCategory("IFP category")
														.withIfpName(IFP_NAME)
														.withMacAddress(macAddress("00:11:22:33:44:55"))
														.withOperationalState(OperationalState.UP)
														.withNeighbor(newPhysicalInterfaceNeighbor()
																	  .withElementId(NEIGHBOR_ID)
																	  .withElementName(NEIGHBOR_NAME)
																	  .withInterfaceName(NEIGHBOR_IFP_NAME))
														.build();
		ArgumentCaptor<ElementPhysicalInterfaceEvent> eventCaptor = forClass(ElementPhysicalInterfaceEvent.class);
		doNothing().when(event).fire(eventCaptor.capture());

		transaction(()->{
			service.storePhysicalInterface(ELEMENT_NAME, submission);
			assertThat(eventCaptor.getValue(),is(ElementPhysicalInterfaceStoredEvent.class));
		});

		transaction(() -> {
			ElementPhysicalInterface ifp = service.getPhysicalInterface(ELEMENT_NAME, IFP_NAME);
			assertEquals(submission.getNeighbor(),ifp.getPhysicalInterface().getNeighbor());
		});
		
		transaction(() -> {
			service.removePhysicalInterfaceNeighbor(ELEMENT_NAME, IFP_NAME);
		});
		
		transaction(() -> {
			ElementPhysicalInterface ifp = service.getPhysicalInterface(ELEMENT_NAME, IFP_NAME);
			assertNull(ifp.getPhysicalInterface().getNeighbor());
		});

	}
	
	@Test
	public void remove_physical_interface() {
		ElementPhysicalInterfaceSubmission submission = newPhysicalInterfaceSubmission()
														.withAdministrativeState(AdministrativeState.UP)
														.withBandwidth(bandwidth("100.000 Mbps"))
														.withIfpAlias("IFP alias")
														.withCategory("IFP category")
														.withIfpName(IFP_NAME)
														.withMacAddress(macAddress("00:11:22:33:44:55"))
														.withOperationalState(OperationalState.UP)
														.withNeighbor(newPhysicalInterfaceNeighbor()
																	  .withElementId(NEIGHBOR_ID)
																	  .withElementName(NEIGHBOR_NAME)
																	  .withInterfaceName(NEIGHBOR_IFP_NAME))
														.build();
		ArgumentCaptor<ElementPhysicalInterfaceEvent> eventCaptor = forClass(ElementPhysicalInterfaceEvent.class);
		doNothing().when(event).fire(eventCaptor.capture());

		transaction(()->{
			service.storePhysicalInterface(ELEMENT_NAME, submission);
		});

		transaction(() -> {
			service.removePhysicalInterface(ELEMENT_NAME, IFP_NAME);
			assertThat(eventCaptor.getValue(),is(ElementPhysicalInterfaceRemovedEvent.class));
		});
		
		transaction(() -> {
			try {
				service.getPhysicalInterface(ELEMENT_NAME, IFP_NAME);
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
														.withBandwidth(bandwidth("100.000 Mbps"))
														.withIfpAlias("IFP alias")
														.withCategory("IFP category")
														.withIfpName(IFP_NAME)
														.withMacAddress(macAddress("00:11:22:33:44:55"))
														.withOperationalState(OperationalState.UP)
														.withNeighbor(newPhysicalInterfaceNeighbor()
																	  .withElementId(NEIGHBOR_ID)
																	  .withElementName(NEIGHBOR_NAME)
																	  .withInterfaceName(NEIGHBOR_IFP_NAME))
														.build();
		ArgumentCaptor<ElementPhysicalInterfaceEvent> eventCaptor = forClass(ElementPhysicalInterfaceEvent.class);
		doNothing().when(event).fire(eventCaptor.capture());

		transaction(()->{
			service.storePhysicalInterface(ELEMENT_NAME, submission);
			assertThat(eventCaptor.getValue(),is(ElementPhysicalInterfaceStoredEvent.class));
		});

		transaction(() -> {
			service.updatePhysicalInterfaceAdministrativeState(ELEMENT_NAME, IFP_NAME,AdministrativeState.DOWN);
		});
		
		transaction(() -> {
			assertEquals(AdministrativeState.DOWN, service.getPhysicalInterface(ELEMENT_NAME, IFP_NAME).getPhysicalInterface().getAdministrativeState());
		});
	}
	
	
	@Test
	public void update_physical_interface_bandwidth() {
		ElementPhysicalInterfaceSubmission submission = newPhysicalInterfaceSubmission()
														.withAdministrativeState(AdministrativeState.UP)
														.withBandwidth(bandwidth("100.000 Mbps"))
														.withIfpAlias("IFP alias")
														.withCategory("IFP category")
														.withIfpName(IFP_NAME)
														.withMacAddress(macAddress("00:11:22:33:44:55"))
														.withOperationalState(OperationalState.UP)
														.withNeighbor(newPhysicalInterfaceNeighbor()
																	  .withElementId(NEIGHBOR_ID)
																	  .withElementName(NEIGHBOR_NAME)
																	  .withInterfaceName(NEIGHBOR_IFP_NAME))
														.build();
		ArgumentCaptor<ElementPhysicalInterfaceEvent> eventCaptor = forClass(ElementPhysicalInterfaceEvent.class);
		doNothing().when(event).fire(eventCaptor.capture());

		transaction(()->{
			service.storePhysicalInterface(ELEMENT_NAME, submission);
			assertThat(eventCaptor.getValue(),is(ElementPhysicalInterfaceStoredEvent.class));
		});

		transaction(() -> {
		     ElementPhysicalInterfaceSubmission update = newPhysicalInterfaceSubmission()
                                                         .withAdministrativeState(AdministrativeState.UP)
                                                         .withBandwidth(bandwidth("1Gbps"))
                                                         .withIfpAlias("IFP alias")
                                                         .withCategory("IFP category")
                                                         .withIfpName(IFP_NAME)
                                                         .withMacAddress(macAddress("00:11:22:33:44:55"))
                                                         .withOperationalState(OperationalState.UP)
                                                         .withNeighbor(newPhysicalInterfaceNeighbor()
                                                                       .withElementId(NEIGHBOR_ID)
                                                                       .withElementName(NEIGHBOR_NAME)
                                                                       .withInterfaceName(NEIGHBOR_IFP_NAME))
                                                         .build();
		    
		     service.storePhysicalInterface(ELEMENT_NAME, update);
	         assertThat(eventCaptor.getValue(),is(ElementPhysicalInterfaceStoredEvent.class));
		});
		
		transaction(() -> {
			assertEquals(bandwidth("1Gbps"), service.getPhysicalInterface(ELEMENT_NAME, IFP_NAME)
			                                        .getPhysicalInterface()
			                                        .getBandwidth());
		});
	}
	
	
    @Test
    public void update_physical_interface_operational_state() {
        ElementPhysicalInterfaceSubmission submission = newPhysicalInterfaceSubmission()
                                                        .withAdministrativeState(AdministrativeState.UP)
                                                        .withBandwidth(bandwidth("100.000 Mbps"))
                                                        .withIfpAlias("IFP alias")
                                                        .withCategory("IFP category")
                                                        .withIfpName(IFP_NAME)
                                                        .withMacAddress(macAddress("00:11:22:33:44:55"))
                                                        .withOperationalState(OperationalState.UP)
                                                        .withNeighbor(newPhysicalInterfaceNeighbor()
                                                                      .withElementId(NEIGHBOR_ID)
                                                                      .withElementName(NEIGHBOR_NAME)
                                                                      .withInterfaceName(NEIGHBOR_IFP_NAME))
                                                        .build();
        ArgumentCaptor<ElementPhysicalInterfaceEvent> eventCaptor = forClass(ElementPhysicalInterfaceEvent.class);
        doNothing().when(event).fire(eventCaptor.capture());

        transaction(()->{
            service.storePhysicalInterface(ELEMENT_NAME, submission);
            assertThat(eventCaptor.getValue(),is(ElementPhysicalInterfaceStoredEvent.class));
        });

        transaction(() -> {
            service.updatePhysicalInterfaceOperationalState(ELEMENT_NAME, 
                                                            IFP_NAME,
                                                            OperationalState.DOWN);
        });
        
        transaction(() -> {
            assertEquals(OperationalState.DOWN, service.getPhysicalInterface(ELEMENT_NAME, IFP_NAME)
                                                       .getPhysicalInterface()
                                                       .getOperationalState());
        });
    }
	
	@Test
	public void store_physical_interface_list() {
		
		List<ElementPhysicalInterfaceSubmission> ifps = asList(newPhysicalInterfaceSubmission()
					  										   .withAdministrativeState(AdministrativeState.UP)
					  										   .withBandwidth(bandwidth("10.000 Gbps"))
					  										   .withIfpAlias("Fabric Interface")
					  										   .withCategory("FABRIC")
					  										   .withIfpName(interfaceName("ifp-0/0/1"))
					  										   .withMacAddress(macAddress("00:11:22:33:44:55"))
					  										   .withOperationalState(OperationalState.UP)
															   .build(),
															   newPhysicalInterfaceSubmission()
					  										   .withAdministrativeState(AdministrativeState.DOWN)
					  										   .withBandwidth(bandwidth("100.000 Mbps"))
					  										   .withIfpAlias("Access Interface")
					  										   .withCategory("ACCESS")
					  										   .withIfpName(interfaceName("ifp-0/0/2"))
					  										   .withMacAddress(macAddress("00:11:22:33:44:56"))
					  										   .withOperationalState(OperationalState.DOWN)
															   .build());
														

		transaction(() -> {
			service.storePhysicalInterfaces(ELEMENT_ID, ifps);
		});
		
		transaction(() -> {
			ElementPhysicalInterfaces reloaded = service.getPhysicalInterfaces(ELEMENT_ID,ifpFilter());
			assertThat(reloaded.getPhysicalInterfaces(), hasSizeOf(2));
			assertEquals(AdministrativeState.UP,reloaded.getPhysicalInterfaces().get(0).getAdministrativeState());
			assertEquals(OperationalState.UP,reloaded.getPhysicalInterfaces().get(0).getOperationalState());
			assertEquals(bandwidth("10.000 Gbps"),reloaded.getPhysicalInterfaces().get(0).getBandwidth());
			assertEquals("Fabric Interface",reloaded.getPhysicalInterfaces().get(0).getIfpAlias());
			assertEquals("FABRIC",reloaded.getPhysicalInterfaces().get(0).getCategory());
			assertEquals(interfaceName("ifp-0/0/1"),reloaded.getPhysicalInterfaces().get(0).getIfpName());
			assertEquals(macAddress("00:11:22:33:44:55"),reloaded.getPhysicalInterfaces().get(0).getMacAddress());

			assertEquals(AdministrativeState.DOWN,reloaded.getPhysicalInterfaces().get(1).getAdministrativeState());
			assertEquals(OperationalState.DOWN,reloaded.getPhysicalInterfaces().get(1).getOperationalState());
			assertEquals(bandwidth("100.000 Mbps"),reloaded.getPhysicalInterfaces().get(1).getBandwidth());
			assertEquals("Access Interface",reloaded.getPhysicalInterfaces().get(1).getIfpAlias());
			assertEquals("ACCESS",reloaded.getPhysicalInterfaces().get(1).getCategory());
			assertEquals(interfaceName("ifp-0/0/2"),reloaded.getPhysicalInterfaces().get(1).getIfpName());
			assertEquals(macAddress("00:11:22:33:44:56"),reloaded.getPhysicalInterfaces().get(1).getMacAddress());
			
		});

	}
	

	@Test
	public void merge_physical_interface_list() {
		
		List<ElementPhysicalInterfaceSubmission> ifps = asList(newPhysicalInterfaceSubmission()
															   .withAdministrativeState(AdministrativeState.UP)
															   .withBandwidth(bandwidth("10.000 Gbps"))
															   .withIfpAlias("Fabric Interface")
															   .withCategory("FABRIC")
															   .withIfpName(interfaceName("ifp-0/0/1"))
															   .withMacAddress(macAddress("00:11:22:33:44:55"))
															   .withOperationalState(OperationalState.UP)
															   .build(),
															   newPhysicalInterfaceSubmission()
															   .withAdministrativeState(AdministrativeState.DOWN)
															   .withBandwidth(bandwidth("100.000 Mbps"))
															   .withIfpAlias("Access Interface")
															   .withCategory("ACCESS")
															   .withIfpName(interfaceName("ifp-0/0/2"))
															   .withMacAddress(macAddress("00:11:22:33:44:56"))
															   .withOperationalState(OperationalState.DOWN)
															   .build());
			

			transaction(() -> {
				service.storePhysicalInterfaces(ELEMENT_ID, ifps);
			});
			
			List<ElementPhysicalInterfaceSubmission> ifpsUpdate = asList(newPhysicalInterfaceSubmission()
																	     .withAdministrativeState(AdministrativeState.UP)
																	     .withBandwidth(bandwidth("10.000 Gbps"))
																	     .withIfpAlias("Fabric Interface")
																	     .withCategory("FABRIC")
																	     .withIfpName(interfaceName("ifp-0/0/1"))
																	     .withMacAddress(macAddress("00:11:22:33:44:55"))
																	     .withOperationalState(OperationalState.UP)
																	     .withNeighbor(newPhysicalInterfaceNeighbor()
																	    	  		   .withElementId(NEIGHBOR_ID)
																	    		  	   .withElementName(NEIGHBOR_NAME)
																	    		  	   .withInterfaceName(interfaceName("ifp-0/0/8")))
																	     .build(),
																	     newPhysicalInterfaceSubmission()
																	     .withAdministrativeState(AdministrativeState.DOWN)
																	     .withBandwidth(bandwidth("100.000 Mbps"))
																	     .withIfpAlias("Access Interface")
																	     .withCategory("ACCESS")
																	     .withIfpName(interfaceName("ifp-0/0/3"))
																	     .withMacAddress(macAddress("00:11:22:33:44:57"))
																	     .withOperationalState(OperationalState.DOWN)
																	     .build());
			
			transaction(() -> {
				service.storePhysicalInterfaces(ELEMENT_ID, ifpsUpdate);
			});
			
			transaction(() -> {
				ElementPhysicalInterfaces reloaded = service.getPhysicalInterfaces(ELEMENT_ID,ifpFilter());
				assertThat(reloaded.getPhysicalInterfaces(), hasSizeOf(2));
				assertEquals(AdministrativeState.UP,reloaded.getPhysicalInterfaces().get(0).getAdministrativeState());
				assertEquals(OperationalState.UP,reloaded.getPhysicalInterfaces().get(0).getOperationalState());
				assertEquals(bandwidth("10.000 Gbps"),reloaded.getPhysicalInterfaces().get(0).getBandwidth());
				assertEquals("Fabric Interface",reloaded.getPhysicalInterfaces().get(0).getIfpAlias());
				assertEquals("FABRIC",reloaded.getPhysicalInterfaces().get(0).getCategory());
				assertEquals(interfaceName("ifp-0/0/1"),reloaded.getPhysicalInterfaces().get(0).getIfpName());
				assertEquals(macAddress("00:11:22:33:44:55"),reloaded.getPhysicalInterfaces().get(0).getMacAddress());
				assertNotNull(reloaded.getPhysicalInterfaces().get(0).getNeighbor());
				assertEquals(NEIGHBOR_ID, reloaded.getPhysicalInterfaces().get(0).getNeighbor().getElementId());
				assertEquals(NEIGHBOR_NAME, reloaded.getPhysicalInterfaces().get(0).getNeighbor().getElementName());
				assertEquals(interfaceName("ifp-0/0/8"), reloaded.getPhysicalInterfaces().get(0).getNeighbor().getInterfaceName());
				
				assertEquals(AdministrativeState.DOWN,reloaded.getPhysicalInterfaces().get(1).getAdministrativeState());
				assertEquals(OperationalState.DOWN,reloaded.getPhysicalInterfaces().get(1).getOperationalState());
				assertEquals(bandwidth("100.000 Mbps"),reloaded.getPhysicalInterfaces().get(1).getBandwidth());
				assertEquals("Access Interface",reloaded.getPhysicalInterfaces().get(1).getIfpAlias());
				assertEquals("ACCESS",reloaded.getPhysicalInterfaces().get(1).getCategory());
				assertEquals(interfaceName("ifp-0/0/3"),reloaded.getPhysicalInterfaces().get(1).getIfpName());
				assertEquals(macAddress("00:11:22:33:44:57"),reloaded.getPhysicalInterfaces().get(1).getMacAddress());
			});
		
	}

	
}

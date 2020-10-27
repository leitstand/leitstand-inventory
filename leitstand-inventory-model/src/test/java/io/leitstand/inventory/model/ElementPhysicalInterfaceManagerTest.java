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

import static io.leitstand.inventory.service.AdministrativeState.UP;
import static io.leitstand.inventory.service.Bandwidth.bandwidth;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementPhysicalInterfaceNeighbor.newPhysicalInterfaceNeighbor;
import static io.leitstand.inventory.service.ElementPhysicalInterfaceSubmission.newPhysicalInterfaceSubmission;
import static io.leitstand.inventory.service.InterfaceName.interfaceName;
import static io.leitstand.inventory.service.MACAddress.macAddress;
import static io.leitstand.inventory.service.OperationalState.DOWN;
import static io.leitstand.inventory.service.ReasonCode.IVT0300E_ELEMENT_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0350E_ELEMENT_IFP_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0353E_ELEMENT_IFP_NOT_REMOVABLE;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.enterprise.event.Event;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.event.ElementPhysicalInterfaceEvent;
import io.leitstand.inventory.service.AdministrativeState;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementPhysicalInterfaceNeighbor;
import io.leitstand.inventory.service.ElementPhysicalInterfaceSubmission;
import io.leitstand.inventory.service.InterfaceName;
import io.leitstand.inventory.service.OperationalState;
@RunWith(MockitoJUnitRunner.class)
public class ElementPhysicalInterfaceManagerTest {

	private static final InterfaceName IFP_NAME = interfaceName("ifp-0/0/0");
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private Repository repository;

	@Mock
	private Messages messages;
	
	@Mock
	private Event<ElementPhysicalInterfaceEvent> event;
	
	@Mock
	private ElementProvider elements;

	@Mock
	private Element element;

	@Mock
	private Element_PhysicalInterface ifp;

	
	@Mock
	private Element_LogicalInterface ifl;
	
	@InjectMocks
	private ElementPhysicalInterfaceManager manager = new ElementPhysicalInterfaceManager();
	
	@Test
	public void cannot_remove_physical_interface_having_logical_interfaces() {
		when(repository.execute(any(Query.class))).thenReturn(ifp).thenReturn(1L);
		Element_ContainerInterface ifc = mock(Element_ContainerInterface.class);
		when(ifp.getContainerInterface()).thenReturn(ifc);
		
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT0353E_ELEMENT_IFP_NOT_REMOVABLE));
		
		manager.removePhysicalInterface(element, IFP_NAME);
	}
	
	@Test
	public void remove_physical_interface() {
		when(repository.execute(any(Query.class))).thenReturn(ifp).thenReturn(0L);
		when(ifp.getElement()).thenReturn(element);
		Element_ContainerInterface ifc = mock(Element_ContainerInterface.class);
		when(ifp.getContainerInterface()).thenReturn(ifc);
		manager.removePhysicalInterface(element, IFP_NAME);
		
		verify(repository).remove(ifc);
		verify(repository).remove(ifp);
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_store_nonexistent_neighbor() {
		ElementPhysicalInterfaceNeighbor neighbor = newPhysicalInterfaceNeighbor()
												   .withElementId(ElementId.randomElementId())
												   .withElementName(new ElementName("unit-test-neihgbor"))
												   .withInterfaceName(new InterfaceName("ifp-0/0/0"))
												   .build();
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0350E_ELEMENT_IFP_NOT_FOUND));
		manager.storePhysicalNeighborInterface(element, IFP_NAME, neighbor);
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_store_unknown_neighbor() {
		ElementPhysicalInterfaceNeighbor neighbor = newPhysicalInterfaceNeighbor()
												   .withElementId(ElementId.randomElementId())
												   .withElementName(new ElementName("unit-test-neihgbor"))
												   .withInterfaceName(new InterfaceName("ifp-0/0/0"))
												   .build();
		
		Element neighborElement = mock(Element.class);
		when(repository.execute(any(Query.class))).thenReturn(ifp)
												  .thenReturn(null);

		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0300E_ELEMENT_NOT_FOUND));
		manager.storePhysicalNeighborInterface(element, IFP_NAME, neighbor);
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_update_operational_state_of_unknown_physical_interface() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0350E_ELEMENT_IFP_NOT_FOUND));
		manager.updatePhysicalLinkOperationalState(element, IFP_NAME, DOWN);
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_update_administrative_state_of_unknown_physical_interface() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0350E_ELEMENT_IFP_NOT_FOUND));
		manager.updatePhysicalLinkAdministrativeState(element, IFP_NAME, UP);
	}
	
	@Test
	public void update_operational_state_of_physical_interface() {
		when(repository.execute(any(Query.class))).thenReturn(ifp);
		manager.updatePhysicalLinkOperationalState(element, IFP_NAME, DOWN);
		verify(ifp).setOperationalState(DOWN);
	}
	
	@Test
	public void update_administrative_state_of_physical_interface() {
		when(repository.execute(any(Query.class))).thenReturn(ifp);

		manager.updatePhysicalLinkAdministrativeState(element, IFP_NAME, UP);
		verify(ifp).setAdministrativeState(UP);
	}
	
	@Test
	public void store_neighbor_interface() {
		ElementPhysicalInterfaceNeighbor neighbor = newPhysicalInterfaceNeighbor()
												   .withElementId(ElementId.randomElementId())
												   .withElementName(new ElementName("unit-test-neihgbor"))
												   .withInterfaceName(IFP_NAME)
												   .build();
		
		Element_PhysicalInterface neighborIfp = mock(Element_PhysicalInterface.class);
		when(neighborIfp.getIfpName()).thenReturn(IFP_NAME);
		when(repository.execute(any(Query.class))).thenReturn(ifp);
		Element neighborElement = mock(Element.class);
		when(elements.tryFetchElement(neighbor.getElementName())).thenReturn(neighborElement);

		manager.storePhysicalNeighborInterface(element, IFP_NAME, neighbor);
		
		verify(ifp).linkTo(neighborElement, IFP_NAME);
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_remove_neighbor_of_unknown_physical_interface() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0350E_ELEMENT_IFP_NOT_FOUND));

		manager.removePhysicalInterfaceNeighbor(element, IFP_NAME);
	}
	
	@Test
	public void remove_neighbor_of_physical_interface() {
		when(repository.execute(any(Query.class))).thenReturn(ifp);
		manager.removePhysicalInterfaceNeighbor(element, IFP_NAME);
		verify(ifp).removeNeighbor();
	}
	
	@Test
	public void storing_new_pyhsical_interface_creates_container_interface() {
		ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
		doNothing().when(repository).add(captor.capture());
		assertTrue(manager.storePhysicalInterface(element, mock(ElementPhysicalInterfaceSubmission.class)));
		
		List<Object> added = captor.getAllValues();
		assertEquals(2,added.size());
		assertThat(added.get(0), is(Element_ContainerInterface.class));
		assertThat(added.get(1), is(Element_PhysicalInterface.class));
	}
	
	@Test
	public void updating_physical_interface_creates_no_new_container_interface() {
		Element_ContainerInterface  ifc = mock(Element_ContainerInterface.class);
		when(repository.find(eq(Element_ContainerInterface.class),any(Element_InterfacePK.class))).thenReturn(ifc);
		when(repository.execute(any(Query.class))).thenReturn(ifp);
		
		assertFalse(manager.storePhysicalInterface(element, mock(ElementPhysicalInterfaceSubmission.class)));
			
		verify(repository,never()).add(ifc);
		verify(repository,never()).add(ifp);
	}
	
	@Test
	public void update_existing_interface_addresses_all_attributes_and_remove_neighbor() {
	    Element_ContainerInterface  ifc = mock(Element_ContainerInterface.class);
        when(repository.find(eq(Element_ContainerInterface.class),any(Element_InterfacePK.class))).thenReturn(ifc);
        when(repository.execute(any(Query.class))).thenReturn(ifp);	    
	    
        ElementPhysicalInterfaceSubmission submission = newPhysicalInterfaceSubmission()
                                                        .withAdministrativeState(AdministrativeState.UP)
                                                        .withBandwidth(bandwidth("10 Gbps"))
                                                        .withCategory("category")
                                                        .withIfcName(interfaceName("ifc-0/0/0/1"))
                                                        .withIfpAlias("alias")
                                                        .withIfpName(interfaceName("ifp-0/0/1"))
                                                        .withMacAddress(macAddress("00:11:22:33:44:55"))
                                                        .withOperationalState(OperationalState.UP)
                                                        .build();
        
        manager.storePhysicalInterface(element, submission);

        verify(ifp).setAdministrativeState(AdministrativeState.UP);
        verify(ifp).setCategory("category");
        verify(ifp).setIfpAlias("alias");
        verify(ifp).setMacAddress(macAddress("00:11:22:33:44:55"));
        verify(ifp).setOperationalState(OperationalState.UP);
	    verify(ifp).setContainerInterface(ifc);
	    verify(ifp).setBandwidth(bandwidth("10 Gbps"));
	    verify(ifp).removeNeighbor();
	}
	
	@Test
    public void update_existing_interface_addresses_all_attributes_and_neighbor() {
        Element_ContainerInterface  ifc = mock(Element_ContainerInterface.class);
        Element neighbor = mock(Element.class);
        when(repository.find(eq(Element_ContainerInterface.class),any(Element_InterfacePK.class))).thenReturn(ifc);
        when(repository.execute(any(Query.class))).thenReturn(ifp);
        when(elements.tryFetchElement(elementName("neighbor"))).thenReturn(neighbor);     
        
        ElementPhysicalInterfaceSubmission submission = newPhysicalInterfaceSubmission()
                                                        .withAdministrativeState(AdministrativeState.UP)
                                                        .withBandwidth(bandwidth("10 Gbps"))
                                                        .withCategory("category")
                                                        .withIfcName(interfaceName("ifc-0/0/0/1"))
                                                        .withIfpAlias("alias")
                                                        .withIfpName(interfaceName("ifp-0/0/1"))
                                                        .withMacAddress(macAddress("00:11:22:33:44:55"))
                                                        .withOperationalState(OperationalState.UP)
                                                        .withNeighbor(newPhysicalInterfaceNeighbor()
                                                                      .withElementName(elementName("neighbor"))
                                                                      .withInterfaceName(interfaceName("ifp-0/0/2")))
                                                        .build();
        
        manager.storePhysicalInterface(element, submission);

        verify(ifp).setAdministrativeState(AdministrativeState.UP);
        verify(ifp).setCategory("category");
        verify(ifp).setIfpAlias("alias");
        verify(ifp).setMacAddress(macAddress("00:11:22:33:44:55"));
        verify(ifp).setOperationalState(OperationalState.UP);
        verify(ifp).setContainerInterface(ifc);
        verify(ifp).setBandwidth(bandwidth("10Gbps"));
        verify(ifp).linkTo(neighbor, interfaceName("ifp-0/0/2"));
    }
	
}

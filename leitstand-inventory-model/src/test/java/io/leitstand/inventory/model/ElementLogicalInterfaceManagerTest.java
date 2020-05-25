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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.enterprise.event.Event;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;

import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.event.ElementLogicalInterfaceEvent;
import io.leitstand.inventory.service.ElementLogicalInterfaceSubmission;
import io.leitstand.inventory.service.InterfaceName;

public class ElementLogicalInterfaceManagerTest {

	private Repository repository;
	private Messages messages;
	private Event<ElementLogicalInterfaceEvent> event;
	private ElementProvider elements;
	
	private Element element;


	private InterfaceName iflName;
	
	private Element_LogicalInterface ifl;
	
	@InjectMocks
	private ElementLogicalInterfaceManager manager;
	
	@Before
	public void initTestEnvironment() {
		
		this.repository = mock(Repository.class);
		this.elements = new ElementProvider(repository);
		this.messages = mock(Messages.class);
		this.event = mock(Event.class);
		
		this.manager = new ElementLogicalInterfaceManager(repository, 
														   elements, 
														   messages, 
														   event);
		
		ifl = mock(Element_LogicalInterface.class);
		iflName = mock(InterfaceName.class);
		element = mock(Element.class);
		
	}

	@Test
	public void remove_logical_interface_from_container_interface() {
		when(repository.execute(any(Query.class))).thenReturn(ifl);
		Element_ContainerInterface  ifc = mock(Element_ContainerInterface.class);
		when(ifl.getContainerInterface()).thenReturn(ifc);
		
		manager.removeLogicalInterface(element, iflName);
		
		verify(repository).remove(ifl);
	}
	
	@Test
	public void store_new_logical_interface_creates_container_interface() {
		ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
		doNothing().when(repository).add(captor.capture());
		assertTrue(manager.storeLogicalInterface(element, mock(ElementLogicalInterfaceSubmission.class)));
		
		List<Object> added = captor.getAllValues();
		assertEquals(2,added.size());
		assertThat(added.get(0), is(Element_ContainerInterface.class));
		assertThat(added.get(1), is(Element_LogicalInterface.class));
	}
	
	@Test
	public void update_existing_logical_interface_creates_container_interface() {
		Element_ContainerInterface  ifc = mock(Element_ContainerInterface.class);
		when(repository.execute(any(Query.class))).thenReturn(ifc)
												 .thenReturn(ifl);
		
		assertFalse(manager.storeLogicalInterface(element, mock(ElementLogicalInterfaceSubmission.class)));
			
		verify(repository,never()).add(ifc);
		verify(repository,never()).add(ifl);
	}
}

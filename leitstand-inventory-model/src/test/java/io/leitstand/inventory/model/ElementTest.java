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

import static io.leitstand.inventory.service.AdministrativeState.NEW;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.OperationalState.DOWN;
import static io.leitstand.inventory.service.OperationalState.OPERATIONAL;
import static io.leitstand.inventory.service.OperationalState.UP;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import io.leitstand.inventory.service.ElementName;

public class ElementTest {

	private ElementGroup group;
	private Element element;
	
	@Before
	public void initTestEnvironment() {
		group = mock(ElementGroup.class);
		element = new Element(group,
							  mock(ElementRole.class),
							  randomElementId(),
							  new ElementName("unit-test-element"));
	}

	
	@Test
	public void switch_administrative_state_to_active_when_device_becomes_operational(){
		element.setAdministrativeState(NEW);
		element.setOperationalState(DOWN);
		assertFalse(element.isActive());
		assertFalse(element.isOperational());
		element.setOperationalState(OPERATIONAL);
		assertTrue(element.isActive());
		assertTrue(element.isOperational());
	}
	
	@Test
	public void UP_is_valid_alias_for_operational() {
		element.setAdministrativeState(NEW);
		element.setOperationalState(DOWN);
		assertFalse(element.isActive());
		assertFalse(element.isOperational());
		element.setOperationalState(UP);
		assertTrue(element.isActive());
		assertTrue(element.isOperational());
	}
	
	@Test
	public void move_element_to_new_group_by_add_and_remove() {
		ElementGroup newgroup = mock(ElementGroup.class);
		element.setGroup(newgroup);
		verify(group).remove(element);
		verify(newgroup).add(element);
	}
}

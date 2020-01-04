/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.AdministrativeState.NEW;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.OperationalState.DOWN;
import static io.leitstand.inventory.service.OperationalState.OPERATIONAL;
import static io.leitstand.inventory.service.OperationalState.UP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
	public void modelname_is_null_if_platform_is_null() {
		assertNull(element.getModelName());
	}
	
	@Test
	public void vendorname_is_null_if_platrofm_is_null() {
		assertNull(element.getVendorName());
	}
	
	@Test
	public void vendor_and_model_are_read_from_platform() {
		Platform platform = mock(Platform.class);
		when(platform.getModel()).thenReturn("unit-model_name");
		when(platform.getVendor()).thenReturn("unit-vendor_name");
		element.setPlatform(platform);
		
		assertEquals("unit-model_name",element.getModelName());
		assertEquals("unit-vendor_name",element.getVendorName());
		
	}
	
	
	@Test
	public void move_element_to_new_group_by_add_and_remove() {
		ElementGroup newgroup = mock(ElementGroup.class);
		element.setGroup(newgroup);
		verify(group).remove(element);
		verify(newgroup).add(element);
	}
}

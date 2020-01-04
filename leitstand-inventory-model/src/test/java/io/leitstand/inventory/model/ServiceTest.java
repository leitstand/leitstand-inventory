/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.ServiceType.DAEMON;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.leitstand.inventory.service.ServiceName;

public class ServiceTest {

	
	private Service service;
	
	@Test
	public void display_name_defaults_to_service_name() {
		service = new Service(DAEMON, new ServiceName("unittest"));
		assertNotNull(service.getDisplayName());
		assertEquals("unittest",service.getDisplayName());
	}
	
	@Test
	public void display_name_defaults_to_service_name_when_passing_empty_display_name() {
		service = new Service(DAEMON, new ServiceName("unittest"));
		service.setDisplayName("");
		assertNotNull(service.getDisplayName());
		assertEquals("unittest",service.getDisplayName());
		
	}
	
	@Test
	public void display_name_defaults_to_service_name_when_passing_null_display_name() {
		service = new Service(DAEMON, new ServiceName("unittest"));
		service.setDisplayName(null);
		assertNotNull(service.getDisplayName());
		assertEquals("unittest",service.getDisplayName());
		
	}
	
}

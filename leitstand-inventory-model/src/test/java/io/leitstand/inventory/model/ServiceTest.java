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

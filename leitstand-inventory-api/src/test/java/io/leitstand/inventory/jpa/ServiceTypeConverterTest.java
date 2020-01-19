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
package io.leitstand.inventory.jpa;

import static io.leitstand.inventory.service.ServiceType.CONTAINER;
import static io.leitstand.inventory.service.ServiceType.DAEMON;
import static io.leitstand.inventory.service.ServiceType.OS;
import static io.leitstand.inventory.service.ServiceType.SERVICE;
import static io.leitstand.inventory.service.ServiceType.VM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;


public class ServiceTypeConverterTest {

	private ServiceTypeConverter converter = new ServiceTypeConverter();
	
	@Test
	public void map_null_service_type_to_null_database_column() {
		assertNull(converter.convertToDatabaseColumn(null));
	}
	
	@Test
	public void map_nonnull_service_type_to_unique_character() {
		assertEquals("C",converter.convertToDatabaseColumn(CONTAINER));
		assertEquals("S",converter.convertToDatabaseColumn(SERVICE));
		assertEquals("D",converter.convertToDatabaseColumn(DAEMON));
		assertEquals("V",converter.convertToDatabaseColumn(VM));
		assertEquals("O",converter.convertToDatabaseColumn(OS));
	}

	@Test
	public void map_nonnull_column_to_service_type() {
		assertEquals(CONTAINER ,converter.convertToEntityAttribute("C"));
		assertEquals(SERVICE   ,converter.convertToEntityAttribute("S"));
		assertEquals(DAEMON    ,converter.convertToEntityAttribute("D"));
		assertEquals(VM        ,converter.convertToEntityAttribute("V"));
		assertEquals(OS        ,converter.convertToEntityAttribute("O"));
	}

	@Test
	public void map_unknown_service_types_to_null() {
		assertNull(converter.convertToEntityAttribute("unknown"));
	}
	
	@Test
	public void map_null_column_to_null_service_type() {
		assertNull(converter.convertToEntityAttribute(null));
	}
	
}

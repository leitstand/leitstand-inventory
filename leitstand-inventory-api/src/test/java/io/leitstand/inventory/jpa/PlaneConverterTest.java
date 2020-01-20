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

import static io.leitstand.inventory.service.Plane.CONTROL;
import static io.leitstand.inventory.service.Plane.DATA;
import static io.leitstand.inventory.service.Plane.MANAGEMENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PlaneConverterTest {

	private PlaneConverter converter = new PlaneConverter();
	
	@Test
	public void map_null_service_type_to_null_database_column() {
		assertNull(converter.convertToDatabaseColumn(null));
	}
	
	@Test
	public void map_nonnull_service_type_to_unique_character() {
		assertEquals("D",converter.convertToDatabaseColumn(DATA));
		assertEquals("C",converter.convertToDatabaseColumn(CONTROL));
		assertEquals("M",converter.convertToDatabaseColumn(MANAGEMENT));

	}

	@Test
	public void map_nonnull_column_to_service_type() {
		assertEquals(DATA , converter.convertToEntityAttribute("D"));
		assertEquals(CONTROL , converter.convertToEntityAttribute("C"));
		assertEquals(MANAGEMENT   , converter.convertToEntityAttribute("M"));
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

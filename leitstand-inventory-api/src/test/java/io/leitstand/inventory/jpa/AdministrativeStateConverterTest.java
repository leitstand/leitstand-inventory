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

import static io.leitstand.inventory.service.AdministrativeState.ACTIVE;
import static io.leitstand.inventory.service.AdministrativeState.DOWN;
import static io.leitstand.inventory.service.AdministrativeState.INACTIVE;
import static io.leitstand.inventory.service.AdministrativeState.NEW;
import static io.leitstand.inventory.service.AdministrativeState.PROVISIONED;
import static io.leitstand.inventory.service.AdministrativeState.RETIRED;
import static io.leitstand.inventory.service.AdministrativeState.UNKNOWN;
import static io.leitstand.inventory.service.AdministrativeState.UP;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import io.leitstand.inventory.service.AdministrativeState;

public class AdministrativeStateConverterTest {


	private AdministrativeStateConverter converter = new AdministrativeStateConverter();
	
	@Test
	public void map_null_adm_state_to_unknown() {
		assertEquals("UNKNOWN",converter.convertToDatabaseColumn(null));
	}
	
	@Test
	public void map_nonnull_adm_state_to_unique_character() {
		assertEquals("ACTIVE",converter.convertToDatabaseColumn(ACTIVE));
		assertEquals("DOWN",converter.convertToDatabaseColumn(DOWN));
		assertEquals("INACTIVE",converter.convertToDatabaseColumn(INACTIVE));
		assertEquals("NEW",converter.convertToDatabaseColumn(NEW));
		assertEquals("RETIRED",converter.convertToDatabaseColumn(RETIRED));
		assertEquals("UP",converter.convertToDatabaseColumn(UP));
		assertEquals("UNKNOWN",converter.convertToDatabaseColumn(UNKNOWN));
		assertEquals("PROVISIONED",converter.convertToDatabaseColumn(PROVISIONED));


	}

	@Test
	public void map_nonnull_column_to_adm_state() {
		assertEquals(ACTIVE 	  , converter.convertToEntityAttribute("ACTIVE"));
		assertEquals(DOWN   	  , converter.convertToEntityAttribute("DOWN"));
		assertEquals(INACTIVE , converter.convertToEntityAttribute("INACTIVE"));
		assertEquals(NEW      , converter.convertToEntityAttribute("NEW"));
		assertEquals(RETIRED  , converter.convertToEntityAttribute("RETIRED"));
		assertEquals(UP       , converter.convertToEntityAttribute("UP"));
		assertEquals(UNKNOWN  , converter.convertToEntityAttribute("UNKNOWN"));
		assertEquals(PROVISIONED  , converter.convertToEntityAttribute("PROVISIONED"));

	}

	public void preserve_custom_adm_states() {
		assertEquals(AdministrativeState.valueOf("CUSTOM") ,converter.convertToEntityAttribute("CUSTOM"));
	}
	
	@Test
	public void map_null_column_to_UNKNOWN_adm_state() {
		assertEquals(UNKNOWN,converter.convertToEntityAttribute(null));
	}
	
}

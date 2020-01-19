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
package io.leitstand.inventory.jsonb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import io.leitstand.inventory.service.Period;

public class PeriodAdapterTest {

	private PeriodAdapter adapter = new PeriodAdapter();
	
	@Test
	public void null_string_is_mapped_to_null() {
		assertNull(adapter.adaptFromJson(null));
	}
	
	@Test
	public void empty_string_is_mapped_to_null() {
		assertNull(adapter.adaptFromJson(""));
	}
	
	@Test
	public void null_period_is_mapped_to_null() {
		assertNull(adapter.adaptToJson(null));
	}
	
	@Test
	public void string_is_translated_to_period() {
		assertEquals(new Period("3d"),adapter.adaptFromJson("3d"));
	}
	
	@Test
	public void period_is_translated_to_string() {
		assertEquals("3d",adapter.adaptToJson(new Period("3d")));
	}
}

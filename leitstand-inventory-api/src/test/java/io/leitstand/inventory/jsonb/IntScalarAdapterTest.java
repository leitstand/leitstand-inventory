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

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import javax.json.bind.adapter.JsonbAdapter;
import javax.json.bind.annotation.JsonbTypeAdapter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.service.VlanID;
import io.leitstand.inventory.service.VlanTPID;

@RunWith(Parameterized.class)
public class IntScalarAdapterTest {

	@Parameters
	public static Collection<Object[]> adapters(){
		Object[][] adapters = new Object[][]{
			{new VlanIDAdapter(),			10, 			new VlanID(10)},
			{new VlanTPIDAdapter(),			10, 			new VlanTPID(10)},
		};
		return asList(adapters);
	}
	
	
	private JsonbAdapter<Scalar<Integer>,Integer> adapter;
	private Scalar<Integer> scalar;
	private Integer value;
	
	public IntScalarAdapterTest(JsonbAdapter<Scalar<Integer>,Integer> adapter,
								   Integer value,
								   Scalar<Integer> scalar) {
		this.adapter = adapter;
		this.value = value;
		this.scalar = scalar;
		
	}
	
	@Test
	public void null_value_is_mapped_to_null() throws Exception {
		assertNull(adapter.getClass().getSimpleName(),
				   adapter.adaptFromJson(null));
	}
	
	@Test
	public void adapt_from_json() throws Exception{
		assertEquals(adapter.getClass().getSimpleName(),
					 scalar,adapter.adaptFromJson(value));
	}
	
	@Test
	public void adapt_to_json() throws Exception {
		assertEquals(adapter.getClass().getSimpleName(),
					 value,adapter.adaptToJson(scalar));
	}
	
	@Test
	public void null_scalar_is_mapped_to_null() throws Exception{
		assertNull(adapter.adaptToJson(null));
	}
	
	@Test
	public void jsonb_adapter_annotation_present() {
		assertTrue(scalar.getClass().isAnnotationPresent(JsonbTypeAdapter.class));
		assertSame(adapter.getClass(),scalar.getClass().getAnnotation(JsonbTypeAdapter.class).value());
	}
}

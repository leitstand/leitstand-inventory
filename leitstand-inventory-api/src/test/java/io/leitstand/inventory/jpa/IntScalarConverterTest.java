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

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.service.VlanID;
import io.leitstand.inventory.service.VlanTPID;

@RunWith(Parameterized.class)
public class IntScalarConverterTest {

	@Parameters
	public static Collection<Object[]> converters(){
		Object[][] converters = new Object[][]{
			{new VlanIDConverter(),			42, 			new VlanID(42)},
			{new VlanTPIDConverter(),		0x0810, 		new VlanTPID(0x0810)}
		};
		return asList(converters);
	}
	
	private AttributeConverter<Scalar<Integer>,Integer> converter;
	private Scalar<Integer> scalar;
	private Integer value;
	
	public IntScalarConverterTest(AttributeConverter<Scalar<Integer>,Integer> converter,
								  Integer value,
								  Scalar<Integer> scalar) {
		this.converter = converter;
		this.value = value;
		this.scalar = scalar;
		
	}
	
	@Test
	public void converter_annotation_present() {
		assertTrue(converter.getClass().isAnnotationPresent(Converter.class));
	}
	
	@Test
	public void null_column_value_is_translated_to_null_entity_attribute_value() throws Exception {
		assertNull(converter.convertToEntityAttribute(null));
	}
	
	@Test
	public void convert_integer_to_entity_attribute() throws Exception{
		assertEquals(scalar,converter.convertToEntityAttribute(value));
	}
	
	@Test
	public void convert_entity_attribute_to_column() throws Exception {
		assertEquals(value,converter.convertToDatabaseColumn(scalar));
	}
	
	@Test
	public void null_entity_attribute_is_translated_to_null_column_value() throws Exception{
		assertNull(converter.convertToDatabaseColumn(null));
	}
}

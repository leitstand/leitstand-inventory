/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import io.leitstand.inventory.service.VlanId;

public class VlanIdConverterTest {

	private VlanIdConverter converter = new VlanIdConverter();
	
	
	@Test
	public void null_integer_is_mapped_to_null() throws Exception{
		assertNull(converter.convertToEntityAttribute(null));
	}
	
	@Test
	public void null_type_is_mapped_to_null_string() throws Exception {
		assertNull(converter.convertToDatabaseColumn(null));
	}
	
	@Test
	public void non_null_type_is_adaptToJsonled_properly() throws Exception{
		assertEquals(Integer.valueOf(10),converter.convertToDatabaseColumn(new VlanId(10)));

	}
	
	@Test
	public void non_null_string_is_convertToEntityAttributeled_properly() throws Exception{
		assertEquals(new VlanId(10),converter.convertToEntityAttribute(Integer.valueOf(10)));
	}
	
}

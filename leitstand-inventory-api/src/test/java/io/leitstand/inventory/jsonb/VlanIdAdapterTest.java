/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import io.leitstand.inventory.service.VlanId;

public class VlanIdAdapterTest {

	private VlanIdAdapter adapter = new VlanIdAdapter();
	
	
	@Test
	public void null_integer_is_mapped_to_null() throws Exception{
		assertNull(adapter.adaptFromJson(null));
	}
	
	@Test
	public void null_type_is_mapped_to_null_string() throws Exception {
		assertNull(adapter.adaptToJson(null));
	}
	
	@Test
	public void non_null_type_is_adaptToJsonled_properly() throws Exception{
		assertEquals(Integer.valueOf(10),adapter.adaptToJson(new VlanId(10)));

	}
	
	@Test
	public void non_null_string_is_adaptFromJsonled_properly() throws Exception{
		assertEquals(new VlanId(10),adapter.adaptFromJson(Integer.valueOf(10)));
	}
	
}

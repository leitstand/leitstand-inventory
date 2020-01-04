/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import io.leitstand.inventory.service.Version;

public class VersionAdapterTest {

	private VersionAdapter adapter = new VersionAdapter();
	
	@Test
	public void empty_string_is_mapped_to_null() throws Exception{
		assertNull(adapter.adaptFromJson(""));
	}
	
	@Test
	public void null_string_is_mapped_to_null() throws Exception{
		assertNull(adapter.adaptFromJson(null));
	}
	
	@Test
	public void null_type_is_mapped_to_null_string() throws Exception {
		assertNull(adapter.adaptToJson(null));
	}
	
	@Test
	public void non_null_type_is_adaptToJsonled_properly() throws Exception{
		assertEquals("18.0.0",adapter.adaptToJson(new Version(18,0,0)));
		assertEquals("18.0.1",adapter.adaptToJson(new Version(18,0,1)));
		assertEquals("18.1.0",adapter.adaptToJson(new Version(18,1,0)));

	}
	
	@Test
	public void non_null_string_is_adaptFromJsonled_properly() throws Exception{
		assertEquals(new Version(1,0,0),adapter.adaptFromJson("1.0.0"));
		assertEquals(new Version(2,0,1),adapter.adaptFromJson("2.0.1"));
		assertEquals(new Version(3,1,0),adapter.adaptFromJson("3.1.0"));

	}
	
}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import io.leitstand.inventory.service.Bandwidth;

public class BandwidthAdapterTest {

	private BandwidthAdapter adapter = new BandwidthAdapter();
	
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
		assertEquals("100.000 Tbps",adapter.adaptToJson(new Bandwidth(100, Bandwidth.Unit.TBPS)));
		assertEquals("100.000 Gbps",adapter.adaptToJson(new Bandwidth(100, Bandwidth.Unit.GBPS)));
		assertEquals("100.000 Mbps",adapter.adaptToJson(new Bandwidth(100, Bandwidth.Unit.MBPS)));
		assertEquals("100.000 Kbps",adapter.adaptToJson(new Bandwidth(100, Bandwidth.Unit.KBPS)));
	}
	
	@Test
	public void non_null_string_is_adaptFromJsonled_properly() throws Exception{
		assertEquals(new Bandwidth(100, Bandwidth.Unit.TBPS),adapter.adaptFromJson("100.000 Tbps"));
		assertEquals(new Bandwidth(100, Bandwidth.Unit.GBPS),adapter.adaptFromJson("100.000 Gbps"));
		assertEquals(new Bandwidth(100, Bandwidth.Unit.MBPS),adapter.adaptFromJson("100.000 Mbps"));
		assertEquals(new Bandwidth(100, Bandwidth.Unit.KBPS),adapter.adaptFromJson("100.000 Kbps"));
	}
	
}

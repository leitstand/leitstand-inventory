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
	public void write_bandwidth_to_json() throws Exception{
		assertEquals("100.000 Tbps",adapter.adaptToJson(new Bandwidth(100, Bandwidth.Unit.TBPS)));
		assertEquals("100.000 Gbps",adapter.adaptToJson(new Bandwidth(100, Bandwidth.Unit.GBPS)));
		assertEquals("100.000 Mbps",adapter.adaptToJson(new Bandwidth(100, Bandwidth.Unit.MBPS)));
		assertEquals("100.000 Kbps",adapter.adaptToJson(new Bandwidth(100, Bandwidth.Unit.KBPS)));
	}
	
	@Test
	public void create_bandwidth_from_json() throws Exception{
		assertEquals(new Bandwidth(100, Bandwidth.Unit.TBPS),adapter.adaptFromJson("100.000 Tbps"));
		assertEquals(new Bandwidth(100, Bandwidth.Unit.GBPS),adapter.adaptFromJson("100.000 Gbps"));
		assertEquals(new Bandwidth(100, Bandwidth.Unit.MBPS),adapter.adaptFromJson("100.000 Mbps"));
		assertEquals(new Bandwidth(100, Bandwidth.Unit.KBPS),adapter.adaptFromJson("100.000 Kbps"));
	}
	
}

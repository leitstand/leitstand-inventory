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

import static io.leitstand.commons.jsonb.IsoDateAdapter.isoDateFormat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;
import java.util.UUID;

import org.junit.Test;

import io.leitstand.commons.jsonb.IsoDateAdapter;
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
	public void non_null_type_is_adapted_to_string() throws Exception{
		assertEquals("18.0.0",adapter.adaptToJson(new Version(18,0,0)));
		assertEquals("18.0.1",adapter.adaptToJson(new Version(18,0,1)));
		assertEquals("18.1.0",adapter.adaptToJson(new Version(18,1,0)));

	}
	
	@Test
	public void non_null_string_is_adapted_to_type() throws Exception{
		assertEquals(new Version(1,0,0),adapter.adaptFromJson("1.0.0"));
		assertEquals(new Version(2,0,1),adapter.adaptFromJson("2.0.1"));
		assertEquals(new Version(3,1,0),adapter.adaptFromJson("3.1.0"));
		assertEquals(new Version(1,0,0,"SNAPSHOT"),adapter.adaptFromJson("1.0.0-SNAPSHOT"));
		assertEquals(new Version(2,0,1,"RC0"),adapter.adaptFromJson("2.0.1-RC0"));
		assertEquals(new Version(3,1,0,"RC1"),adapter.adaptFromJson("3.1.0-RC1"));
	}

	@Test
	public void prerelease_can_contain_hyphen() throws Exception {
		assertEquals(new Version(3,1,0,"RC-1"),adapter.adaptFromJson("3.1.0-RC-1"));
	}

	
	@Test
	public void prerelease_can_contain_printable_characters() throws Exception {
		UUID buildId = UUID.randomUUID();
		Date date = new Date();
		String prerelease = String.format("RC1+%s+%s",buildId,isoDateFormat(date));
		
		assertEquals(new Version(3,1,0,prerelease),adapter.adaptFromJson("3.1.0-"+prerelease));
	}
	
}

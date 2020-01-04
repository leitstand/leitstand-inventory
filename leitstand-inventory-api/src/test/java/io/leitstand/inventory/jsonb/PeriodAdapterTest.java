/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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

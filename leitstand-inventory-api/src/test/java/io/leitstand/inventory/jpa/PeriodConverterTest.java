/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import io.leitstand.inventory.service.Period;

public class PeriodConverterTest {

	private PeriodConverter converter = new PeriodConverter();
	
	@Test
	public void null_string_is_mapped_to_null() {
		assertNull(converter.convertToEntityAttribute(null));
	}
	
	@Test
	public void empty_string_is_mapped_to_null() {
		assertNull(converter.convertToEntityAttribute(""));
	}
	
	@Test
	public void null_period_is_mapped_to_null() {
		assertNull(converter.convertToDatabaseColumn(null));
	}
	
	@Test
	public void string_is_translated_to_period() {
		assertEquals(new Period("3d"),converter.convertToEntityAttribute("3d"));
	}
	
	@Test
	public void period_is_translated_to_string() {
		assertEquals("3d",converter.convertToDatabaseColumn(new Period("3d")));
	}
}

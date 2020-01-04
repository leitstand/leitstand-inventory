/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import static io.leitstand.inventory.service.Plane.CONTROL;
import static io.leitstand.inventory.service.Plane.DATA;
import static io.leitstand.inventory.service.Plane.MANAGEMENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PlaneConverterTest {

	private PlaneConverter converter = new PlaneConverter();
	
	@Test
	public void map_null_service_type_to_null_database_column() {
		assertNull(converter.convertToDatabaseColumn(null));
	}
	
	@Test
	public void map_nonnull_service_type_to_unique_character() {
		assertEquals("D",converter.convertToDatabaseColumn(DATA));
		assertEquals("C",converter.convertToDatabaseColumn(CONTROL));
		assertEquals("M",converter.convertToDatabaseColumn(MANAGEMENT));

	}

	@Test
	public void map_nonnull_column_to_service_type() {
		assertEquals(DATA , converter.convertToEntityAttribute("D"));
		assertEquals(CONTROL , converter.convertToEntityAttribute("C"));
		assertEquals(MANAGEMENT   , converter.convertToEntityAttribute("M"));
	}

	@Test
	public void map_unknown_service_types_to_null() {
		assertNull(converter.convertToEntityAttribute("unknown"));
	}
	
	@Test
	public void map_null_column_to_null_service_type() {
		assertNull(converter.convertToEntityAttribute(null));
	}
	
}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import static io.leitstand.inventory.service.ImageState.CANDIDATE;
import static io.leitstand.inventory.service.ImageState.NEW;
import static io.leitstand.inventory.service.ImageState.RELEASE;
import static io.leitstand.inventory.service.ImageState.REVOKED;
import static io.leitstand.inventory.service.ImageState.SUPERSEDED;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ImageStateConverterTest {

	private ImageStateConverter converter = new ImageStateConverter();
	
	@Test
	public void map_null_service_type_to_REVOKED_database_column() {
		assertEquals("X",converter.convertToDatabaseColumn(null));
	}
	
	@Test
	public void map_nonnull_service_type_to_unique_character() {
		assertEquals("N",converter.convertToDatabaseColumn(NEW));
		assertEquals("C",converter.convertToDatabaseColumn(CANDIDATE));
		assertEquals("R",converter.convertToDatabaseColumn(RELEASE));
		assertEquals("X",converter.convertToDatabaseColumn(REVOKED));
		assertEquals("S",converter.convertToDatabaseColumn(SUPERSEDED));

	}

	@Test
	public void map_nonnull_column_to_service_type() {
		assertEquals(NEW 	  , converter.convertToEntityAttribute("N"));
		assertEquals(CANDIDATE 	  , converter.convertToEntityAttribute("C"));
		assertEquals(RELEASE 	  , converter.convertToEntityAttribute("R"));
		assertEquals(REVOKED , converter.convertToEntityAttribute("X"));
		assertEquals(SUPERSEDED , converter.convertToEntityAttribute("S"));

	}

	@Test
	public void map_unknown_service_types_to_REVOKED() {
		assertEquals(REVOKED,converter.convertToEntityAttribute("unknown"));
	}
	
	@Test
	public void map_null_column_to_REVOKED_service_type() {
		assertEquals(REVOKED,converter.convertToEntityAttribute(null));
	}
	
}

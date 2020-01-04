/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import static io.leitstand.inventory.service.ServiceContextState.ACTIVE;
import static io.leitstand.inventory.service.ServiceContextState.COLD_STANDBY;
import static io.leitstand.inventory.service.ServiceContextState.HOT_STANDBY;
import static io.leitstand.inventory.service.ServiceContextState.INACTIVE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ServiceContextStateConverterTest {

	
	private ServiceContextStateConverter converter = new ServiceContextStateConverter();
	
	@Test
	public void map_null_service_type_to_null_database_column() {
		assertNull(converter.convertToDatabaseColumn(null));
	}
	
	@Test
	public void map_nonnull_service_type_to_unique_character() {
		assertEquals("A",converter.convertToDatabaseColumn(ACTIVE));
		assertEquals("C",converter.convertToDatabaseColumn(COLD_STANDBY));
		assertEquals("H",converter.convertToDatabaseColumn(HOT_STANDBY));
		assertEquals("I",converter.convertToDatabaseColumn(INACTIVE));

	}

	@Test
	public void map_nonnull_column_to_service_type() {
		assertEquals(ACTIVE 	  , converter.convertToEntityAttribute("A"));
		assertEquals(COLD_STANDBY , converter.convertToEntityAttribute("C"));
		assertEquals(HOT_STANDBY , converter.convertToEntityAttribute("H"));
		assertEquals(INACTIVE   , converter.convertToEntityAttribute("I"));
	}

	@Test
	public void map_unknown_service_types_to_INACTIVE() {
		assertEquals(INACTIVE,converter.convertToEntityAttribute("unknown"));
	}
	
	@Test
	public void map_null_column_to_null_service_type() {
		assertEquals(INACTIVE,converter.convertToEntityAttribute(null));
	}
	
}

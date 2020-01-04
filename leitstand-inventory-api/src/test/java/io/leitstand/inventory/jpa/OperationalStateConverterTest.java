/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import static io.leitstand.inventory.service.OperationalState.DOWN;
import static io.leitstand.inventory.service.OperationalState.MAINTENANCE;
import static io.leitstand.inventory.service.OperationalState.MALFUNCTION;
import static io.leitstand.inventory.service.OperationalState.OPERATIONAL;
import static io.leitstand.inventory.service.OperationalState.PARTIAL;
import static io.leitstand.inventory.service.OperationalState.STARTED;
import static io.leitstand.inventory.service.OperationalState.STOPPED;
import static io.leitstand.inventory.service.OperationalState.UNKNOWN;
import static io.leitstand.inventory.service.OperationalState.UP;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import io.leitstand.inventory.service.OperationalState;

public class OperationalStateConverterTest {

	private OperationalStateConverter converter = new OperationalStateConverter();
	
	@Test
	public void map_null_op_state_to_UNKNOWN() {
		assertEquals("UNKNOWN",converter.convertToDatabaseColumn(null));
	}
	
	@Test
	public void map_nonnull_op_state_to_unique_character() {
		assertEquals("DOWN",converter.convertToDatabaseColumn(DOWN));
		assertEquals("MALFUNCTION",converter.convertToDatabaseColumn(MALFUNCTION));
		assertEquals("MAINTENANCE",converter.convertToDatabaseColumn(MAINTENANCE));
		assertEquals("OPERATIONAL",converter.convertToDatabaseColumn(OPERATIONAL));
		assertEquals("PARTIAL",converter.convertToDatabaseColumn(PARTIAL));
		assertEquals("STARTED",converter.convertToDatabaseColumn(STARTED));
		assertEquals("STOPPED",converter.convertToDatabaseColumn(STOPPED));
		assertEquals("UNKNOWN",converter.convertToDatabaseColumn(UNKNOWN));
		assertEquals("UP",converter.convertToDatabaseColumn(UP));

	}

	@Test
	public void map_nonnull_column_to_op_state() {
		assertEquals(DOWN 	  , converter.convertToEntityAttribute("DOWN"));
		assertEquals(MALFUNCTION , converter.convertToEntityAttribute("MALFUNCTION"));
		assertEquals(MAINTENANCE , converter.convertToEntityAttribute("MAINTENANCE"));
		assertEquals(OPERATIONAL   , converter.convertToEntityAttribute("OPERATIONAL"));
		assertEquals(PARTIAL  , converter.convertToEntityAttribute("PARTIAL"));
		assertEquals(STARTED       , converter.convertToEntityAttribute("STARTED"));
		assertEquals(STOPPED  , converter.convertToEntityAttribute("STOPPED"));
		assertEquals(UNKNOWN  , converter.convertToEntityAttribute("UNKNOWN"));
		assertEquals(UP  , converter.convertToEntityAttribute("UP"));
	}

	@Test
	public void preserver_custom_op_states() {
		assertEquals(OperationalState.valueOf("UNKNOWN"),converter.convertToEntityAttribute("UNKNOWN"));
	}
	
	@Test
	public void map_null_column_to_null_op_state() {
		assertEquals(UNKNOWN,converter.convertToEntityAttribute(null));
	}
	
}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.RoutingInstanceName;

/**
 * Converts a <code>RoutingInstanceName</code> to a string and vice versa.
 */
@Converter(autoApply=true)
public class RoutingInstanceNameConverter implements AttributeConverter<RoutingInstanceName, String> {

	/**
	 * Converts a <code>RoutingInstanceName</code> to a string.
	 * @param name the routing instance name
	 * @return the routing instance name as string or <code>null</code> if the specified name is <code>null</code>.
	 */
	@Override
	public String convertToDatabaseColumn(RoutingInstanceName name) {
		return RoutingInstanceName.toString(name);
	}

	/**
	 * Creates a <code>RoutingInstanceName</code> from the specified string.
	 * @param name the routing instance name
	 * @returns the <code>RoutingInstanceName</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	@Override
	public RoutingInstanceName convertToEntityAttribute(String name) {
		return RoutingInstanceName.valueOf(name);
	}

}
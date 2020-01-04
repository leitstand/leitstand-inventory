/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.EnvironmentName;

/**
 * Converts an <code>EnvironmentName</code> to a string and vice versa.
 */
@Converter
public class EnvironmentNameConverter implements AttributeConverter<EnvironmentName, String>{

	/**
	 * Converts a string to an <code>EnvironmentName</code>.
	 * @param name the environment name
	 * @return the string value or <code>null</code> if the specified name is <code>null</code>.
	 */
	@Override
	public String convertToDatabaseColumn(EnvironmentName name) {
		return EnvironmentName.toString(name);
	}

	/**
	 * Converts an <code>EnvironmentName</code> to a string.
	 * @param name the environment name
	 * @return the <code>EnvironmentName</code> or <code>null</code> if the specified name is <code>null</code> or empty.
	 */
	@Override
	public EnvironmentName convertToEntityAttribute(String name) {
		return EnvironmentName.valueOf(name);
	}

}

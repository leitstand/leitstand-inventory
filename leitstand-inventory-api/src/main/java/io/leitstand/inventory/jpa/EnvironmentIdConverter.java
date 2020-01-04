/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.EnvironmentId;

/**
 * Converts an <code>EnvironmentId</code> to a string and vice versa.
 */
@Converter
public class EnvironmentIdConverter implements AttributeConverter<EnvironmentId, String>{

	/**
	 * Converts a string to an <code>EnvironmentId</code>.
	 * @param id the environment ID
	 * @return the string value or <code>null</code> if the specified ID is <code>null</code>.
	 */
	@Override
	public String convertToDatabaseColumn(EnvironmentId id) {
		return EnvironmentId.toString(id);
	}

	/**
	 * Converts an <code>EnvironmentId</code> to a string.
	 * @param id the environment ID
	 * @return the <code>EnvironmentId</code> or <code>null</code> if the specified ID is <code>null</code> or empty.
	 */
	@Override
	public EnvironmentId convertToEntityAttribute(String id) {
		return EnvironmentId.valueOf(id);
	}

}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.VisualizationConfigName;

/**
 * Converts a <code>VisualizationConfigName</code> into a string and vice versa.
 */
@Converter
public class VisualizationConfigNameConverter implements AttributeConverter<VisualizationConfigName, String> {
	
	/**
	 * Converts a <code>VisualizationConfigName</code> to a string.
	 * @param name the visualization configuration name
	 * @return the configuration name as string or <code>null</code> if the specified name is <code>null</code>
	 */
	@Override
	public String convertToDatabaseColumn(VisualizationConfigName name) {
		return VisualizationConfigName.toString(name);
	}

	/**
	 * Creates a <code>VisualizationConfigName</code> from the specified string.
	 * @param name the visualization configuration name
	 * @return the <code>VisualizationConfigName</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	@Override
	public VisualizationConfigName convertToEntityAttribute(String name) {
		return VisualizationConfigName.valueOf(name);
	}

}

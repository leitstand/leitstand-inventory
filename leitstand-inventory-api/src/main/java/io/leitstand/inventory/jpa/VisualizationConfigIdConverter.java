/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.VisualizationConfigId;

/**
 * Converts a <code>VisualizationConfigId</code> into a string and vice versa.
 */
@Converter
public class VisualizationConfigIdConverter implements AttributeConverter<VisualizationConfigId, String> {
	
	/**
	 * Converts a <code>VisualizationConfigId</code> to a string.
	 * @param name the visualization configuration name
	 * @return the configuration name as string or <code>null</code> if the specified name is <code>null</code>
	 */
	@Override
	public String convertToDatabaseColumn(VisualizationConfigId name) {
		return VisualizationConfigId.toString(name);
	}

	/**
	 * Creates a <code>VisualizationConfigId</code> from the specified string.
	 * @param name the visualization configuration name
	 * @return the <code>VisualizationConfigId</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	@Override
	public VisualizationConfigId convertToEntityAttribute(String name) {
		return VisualizationConfigId.valueOf(name);
	}

}

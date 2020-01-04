/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.VisualizationConfigNameAdapter;
/**
 * Unique name of a metric visualization configuration.
 * <p>
 * The name is unique per metric.
 */
@JsonbTypeAdapter(VisualizationConfigNameAdapter.class)
public class VisualizationConfigName extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a <code>MetricVisualizationName</code> from the specified string.
	 * @param name the metric visualization name
	 * @return the <code>MetricVisualizationName</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static VisualizationConfigName valueOf(String name) {
		return VisualizationConfigName.fromString(name, VisualizationConfigName::new);
	}
	
	private String value;
	
	/**
	 * Creates a <code>MetricVisualizationName</code>.
	 * @param name the name of the metric visualization config.
	 */
	public VisualizationConfigName(String name) {
		this.value = name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return value;
	}


	
}

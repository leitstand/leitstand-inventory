/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.MetricNameAdapter;

/**
 * The name of a registered metric. 
 */
@JsonbTypeAdapter(MetricNameAdapter.class)
public class MetricName extends Scalar<String> {
	
	private static final long serialVersionUID = 0;

	/**
	 * Alias for {@link #valueOf(String)} to improve readability.
	 * <p>
	 * Creates a <code>MetricName</code> from the specified string.
	 * @param name the metric name
	 * @return the <code>MetricName</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static MetricName metricName(String name) {
		return valueOf(name);
	}
	
	/**
	 * Creates a <code>MetricName</code> from the specified string.
	 * @param name the metric name
	 * @return the <code>MetricName</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static MetricName valueOf(String value) {
		return Scalar.fromString(value, MetricName::new);
	}
	
	private String value;

	/**
	 * Create a <code>MetricName</code> instance.
	 * @param name the metric name
	 */
	public MetricName(String name) {
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
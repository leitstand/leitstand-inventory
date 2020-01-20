/*
 * Copyright 2020 RtBrick Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
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

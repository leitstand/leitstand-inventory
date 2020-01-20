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

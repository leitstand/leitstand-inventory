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

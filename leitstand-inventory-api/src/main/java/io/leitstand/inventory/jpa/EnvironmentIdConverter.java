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

import io.leitstand.commons.model.Scalar;
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
		return Scalar.toString(id);
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

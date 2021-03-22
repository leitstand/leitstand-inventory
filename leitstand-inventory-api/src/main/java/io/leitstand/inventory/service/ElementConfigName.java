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
import io.leitstand.inventory.jsonb.ElementConfigNameAdapter;

/**
 * An element configuration name.
 * <p>
 * The element configuration name is unique per element.
 */
@JsonbTypeAdapter(ElementConfigNameAdapter.class)
public class ElementConfigName extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * Creates an element configuration from the specified string.
	 * <p>
	 * Alias for {@link #valueOf(String)} to improve readability.
	 * @param name the configuration name
	 * @return the element configuration name
	 */
	public static ElementConfigName elementConfigName(String name) {
		return valueOf(name);
	}
	
	/**
	 * Creates an element configuration name from the specified string.
	 * @param name the configuration name
	 * @return an element configuration name
	 */
	public static ElementConfigName valueOf(String name) {
		return Scalar.fromString(name, ElementConfigName::new);
	}
	
	
	private String value;

	/**
	 * Creates a new element configuration name.
	 * @param name the configuration name
	 */
	public ElementConfigName(String name) {
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

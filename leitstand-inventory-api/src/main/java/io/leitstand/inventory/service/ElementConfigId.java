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

import static io.leitstand.commons.model.Patterns.UUID_PATTERN;

import java.util.UUID;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.ElementConfigIdAdapter;

/**
 * Unique element configuration identifier.
 */
@JsonbTypeAdapter(ElementConfigIdAdapter.class)
public class ElementConfigId extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a random element configuration ID.
	 * @return a random element configuration ID.
	 */
	public static ElementConfigId randomConfigId() {
		return valueOf(UUID.randomUUID().toString());
	}
	
	/**
	 * Creates an element configuration from the specified name.
	 * <p>
	 * An alias for the {@link #valueOf(String)} to improve readability.
	 * @param configId the configuration ID
	 * @return the element configuration ID
	 */
	public static ElementConfigId elementConfigId(String configId) {
		return valueOf(configId);
	}
	
	/**
	 * Creates an element configuration ID from the specified string.
	 * @param configId the configuration ID
	 * @return the element configuration ID
	 */
	public static ElementConfigId valueOf(String configId) {
		return fromString(configId,ElementConfigId::new);
	}
	
	@NotNull(message="{config_id.required}")
	@Pattern(message="{config_id.invalid}", regexp=UUID_PATTERN)
	private String value;
	
	/**
	 * Creates an element configuration ID
	 * @param value the configuration ID
	 */
	public ElementConfigId(String value) {
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return value;
	}
	
}

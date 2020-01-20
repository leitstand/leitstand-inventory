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

import java.util.UUID;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.EnvironmentIdAdapter;

/**
 * Unique environment ID in UUIDv4 format.
 */
@JsonbTypeAdapter(EnvironmentIdAdapter.class)
public class EnvironmentId extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a random environment ID.
	 * @return a random environment ID.
	 */
	public static EnvironmentId randomEnvironmentId() {
		return valueOf(UUID.randomUUID().toString());
	}

	/**
	 * Alias for {@link #valueOf(String)} to improve readability.
	 * <p>
	 * Converts a string to an <code>EnvironmentName</code>.
	 * @param name the environment name
	 * @return the <code>EnvironmentName</code> or <code>null</code> if the specified name is <code>null</code> or empty.
	 */
	public static EnvironmentId environmentId(String name) {
		return valueOf(name);
	}
	
	/**
	 * Converts a string to an <code>EnvironmentName</code>.
	 * @param name the environment name
	 * @return the <code>EnvironmentName</code> or <code>null</code> if the specified name is <code>null</code> or empty.
	 */
	public static EnvironmentId valueOf(String name) {
		return Scalar.fromString(name,EnvironmentId::new);
	}
	
	// TODO Add pattern
	private String value;
	
	public EnvironmentId(String value) {
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

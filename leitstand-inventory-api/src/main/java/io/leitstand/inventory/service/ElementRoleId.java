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
import io.leitstand.inventory.jsonb.ElementRoleIdAdapter;

/**
 * Unique element role ID in UUIDv4 format.
 * <p>
 * The element role ID is immutable and forms a persistent unique element role key.
 *
 */
@JsonbTypeAdapter(ElementRoleIdAdapter.class)
public class ElementRoleId extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a random element role ID.
	 * @return a random element role ID.
	 */
	public static ElementRoleId randomElementRoleId() {
		return valueOf(UUID.randomUUID().toString());
	}
	
	/**
	 * Creates an <code>ElementRoleId</code> from the specified string.
	 * Returns <code>null</code> if the specified string is <code>null</code> or empty.
	 * <p>
	 * This method is an alias of the {@link #valueOf(String)} method to improve readability by avoiding static import conflicts.
	 * @param roleId the role ID
	 * @return an <code>ElementRoleId</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static ElementRoleId elementRoleId(String roleId) {
		return valueOf(roleId);
	}
	
	/**
     * Creates an <code>ElementRoleId</code> from the specified string.
     * Returns <code>null</code> if the specified string is <code>null</code> or empty.
     *
     * @param roleId the role ID
     * @return an <code>ElementRoleId</code> or <code>null</code> if the specified string is <code>null</code> or empty.
     */
	public static ElementRoleId valueOf(String uuid) {
		return fromString(uuid,ElementRoleId::new);
	}

	@NotNull(message="{role_id.required}")
	@Pattern(regexp=UUID_PATTERN, message="{role_id.required}")
	private String value;
	
	/**
	 * Creates a element role ID.
	 * @param value the role ID
	 */
	public ElementRoleId(String value) {
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

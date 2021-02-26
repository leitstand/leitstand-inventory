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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.ElementGroupIdAdapter;

/**
 * A unique element group ID in UUIDv4 format.
 */
@JsonbTypeAdapter(ElementGroupIdAdapter.class)
public class ElementGroupId extends Scalar<String> {

	public static final String PATTERN = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
	
	private static final long serialVersionUID = 1L;

	/**
	 * Alias for {@link #valueOf(String)} to improve readability.
	 * <p>
	 * Creates an <code>ElementGroupId</code> from the specified string.
	 * @param id the group ID
	 * @return the <code>ElementGroupId</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static ElementGroupId groupId(String id) {
		return fromString(id,ElementGroupId::new);
	}
	
	/**
	 * Creates an <code>ElementGroupId</code> from the specified string.
	 * @param id the group ID
	 * @return the <code>ElementGroupId</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static ElementGroupId valueOf(String id) {
		return fromString(id,ElementGroupId::new);
	}

	/**
	 * Creates a random group ID.
	 * @return a random group ID.
	 */
	public static ElementGroupId randomGroupId() {
		return new ElementGroupId(UUID.randomUUID().toString());
	}
	
	@NotNull(message="{group_id.required}")
	@Pattern(message="{group_id.invalid}", regexp=PATTERN)
	private String value;
	
	/**
	 * Creates a new group ID
	 * @param value the group ID
	 */
	public ElementGroupId(String value){
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

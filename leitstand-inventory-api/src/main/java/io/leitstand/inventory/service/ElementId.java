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
import io.leitstand.inventory.jsonb.ElementIdAdapter;

/**
 * A unique identifier for an element in UUIDv4 format.
 * <p>
 * The element ID is immutable for an element and hence forms a persistent unique key.
 * </p>
 * @see ElementSettings
 * @see ElementConfig
 * @see ElementImages
 * @see ElementPhysicalInterfaces
 * @see ElementLogicalInterfaces
 * @see ElementModules
 * @see ElementLocation
 */
@JsonbTypeAdapter(ElementIdAdapter.class)
public class ElementId extends Scalar<String> {
	
	private static final long serialVersionUID = 1L;
	private static final String PATTERN = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";

	/**
	 * Returns a random <code>ElementId</code>.
	 * @return a random <code>ElementId</code>.
	 */
	public static ElementId randomElementId() {
		return new ElementId(UUID.randomUUID());
	}

	/**
	 * Creates an <code>ElementId</code> from the specified string.
	 * Returns <code>null</code> if the specified string is <code>null</code> or empty.
	 * <p>
	 * This method is an alias for the {@link #valueOf(String)} method to improve readability by avoiding static import conflicts.
	 * @param id the element ID
	 * @returns the <code>ElementId</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static ElementId elementId(String id) {
		return valueOf(id);
	}
	
	/**
	 * Creates an <code>ElementId</code> from the specified string.
	 * Returns <code>null</code> if the specified string is <code>null</code> or empty.
	 * @param id the element ID
	 * @returns the <code>ElementId</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static ElementId valueOf(String id) {
		return ElementId.fromString(id, ElementId::new);
	}

	@NotNull(message="{element_id.required}")
	@Pattern(message="{element_id.invalid}", regexp=PATTERN)
	private String value;
	
	/**
	 * Create a <code>ElementId</code>.
	 * @param value the element id
	 */
	public ElementId(UUID value) {
		this(value.toString());
	}
	
	/**
	 * Create a <code>ElementId</code>.
	 * @param value the element id
	 */
	public ElementId(String value){
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

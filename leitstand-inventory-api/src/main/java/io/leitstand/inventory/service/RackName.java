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
import io.leitstand.inventory.jsonb.RackNameAdapter;

/**
 * The rack name.
 * <p>
 * The rack name is unique for all racks and can be changed for any rack provided the new name is also unique.
 * @see RackId
 */
@JsonbTypeAdapter(RackNameAdapter.class)
public class RackName extends Scalar<String>{

	private static final long serialVersionUID = 1L;


	/**
	 * Creates a rack name from the specified string.
	 * Returns <code>null</code> if the specified string is <code>null</code> or empty.
	 * <p>
	 * This method is an alias of the {@link #valueOf(String)} method to improve readability by avoiding static import conflicts.
	 * @param name the rack name
	 * @return the rack name or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static RackName rackName(String name) {
		return valueOf(name);
	}
	
	/**
	 * Creates a rack name from the specified string.
	 * Returns <code>null</code> if the specified string is <code>null</code> or empty.
	 * @param name the rack name.
	 * @return the rack name or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static RackName valueOf(String name) {
		return fromString(name, RackName::new);
	}
	
	private String value;
	
	/**
	 * Creates a rack name.
	 * @param value the rack name.
	 */
	public RackName(String value) {
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

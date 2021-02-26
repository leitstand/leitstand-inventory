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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.ElementGroupNameAdapter;
/**
 * A unique name of an element group.
 */
@JsonbTypeAdapter(ElementGroupNameAdapter.class)
public class ElementGroupName extends Scalar<String> {

	private static final long serialVersionUID = 1L;

	/**
	 * Alias for {@link #valueOf(String)} to improve readability.
	 * <p>
	 * Creates a <code>ElementGroupName</code> from the specified string.
	 * @param name the group name
	 * @return the <code>ElementGroupName</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static ElementGroupName groupName(String name) {
		return valueOf(name);
	}
	
	/**
	 * Creates a <code>ElementGroupName</code> from the specified string.
	 * @param name the group name
	 * @return the <code>ElementGroupName</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static ElementGroupName valueOf(String name) {
		return fromString(name,ElementGroupName::new);
	}
	
	@NotNull(message="{group_name.required}")
	@Pattern(message="{group_name.invalid}", regexp="\\p{Print}{1,64}")
	private String value;
	
	/**
	 * Creates a <code>ElementGroupName</code>.
	 * @param value the group name
	 */
	public ElementGroupName(String value){
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

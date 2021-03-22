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
import io.leitstand.inventory.jsonb.ApplicationNameAdapter;

/**
 * The unique name of an application a software image supports.
 * <p>
 * A software image typically supports multiple applications. 
 * Only applications supported by the installed image can be configured on an element.
 * Example applications are network protocols like routing protocols (BGP, IS-IS or OSPF) and access protocols (PPPoE)
 * 
 */
@JsonbTypeAdapter(ApplicationNameAdapter.class)
public class ApplicationName extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * Creates an <code>ApplicationName</code> from the specified string.
	 * <p>
	 * This method is an alias of the {@link #valueOf(String)} method to improve readability by avoiding static import conflicts.
	 * Returns <code>null</code> if the specified string is <code>null</code> or empty.
	 * @param  name the application name
	 * @return the application name
	 */
	public static ApplicationName applicationName(String name) {
		return valueOf(name);
	}

	/**
	 * Creates an <code>ApplicationName</code> from the specified string.
	 * Returns <code>null</code> if the specified string is <code>null</code> or empty.
	 * @param  name the application name
	 * @return the application name instance
	 */
	public static ApplicationName valueOf(String name) {
		return fromString(name,ApplicationName::new);
	}

	
	private String value;
	
	/**
	 * Creates an <code>ApplicationName</code>.
	 * @param name the application name
	 */
	public ApplicationName(String name) {
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

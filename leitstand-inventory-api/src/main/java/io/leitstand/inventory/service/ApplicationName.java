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
 * The unique name of an application that can be enabled with a certain software image.
 * <p>
 * A software image is constituted by a set of packages and configurations. 
 * The packages included with an image define what applications can be enabled.
 * Sample applications are routing protocols such as <code>BGP</code>, <code>ISIS</code> or
 * <code>OSPF</code> for example. The {@link ImageInfo} provides a list supported applications
 * such that this information does not have to be derived from the packages list manually.
 * </p>
 */
@JsonbTypeAdapter(ApplicationNameAdapter.class)
public class ApplicationName extends Scalar<String>{

	private static final long serialVersionUID = 1L;


	/**
	 * Creates an <code>ApplicationName</code> from the specified string.
	 * Returns <code>null</code> if the specified string is <code>null</code> or empty.
	 * @param  name - the application name
	 * @return the application name instance
	 * @see Scalar#fromString(String, java.util.function.Function)
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

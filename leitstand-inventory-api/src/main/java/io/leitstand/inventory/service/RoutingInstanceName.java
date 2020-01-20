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

import java.io.Serializable;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.RoutingInstanceNameAdapter;

/**
 * Unique routing instance name.
 */
@JsonbTypeAdapter(RoutingInstanceNameAdapter.class)
public class RoutingInstanceName extends Scalar<String> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a <code>RoutingInstanceName</code> from the specified string.
	 * @param name the routing instance name
	 * @return the <code>RoutingInstanceName</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static RoutingInstanceName valueOf(String name){
		return Scalar.fromString(name, RoutingInstanceName::new);
	}
	
	private String value;
	
	/**
	 * Creates a <code>RoutingInstanceName</code>.
	 * @param name the routing instance name
	 */
	public RoutingInstanceName(String name) {
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

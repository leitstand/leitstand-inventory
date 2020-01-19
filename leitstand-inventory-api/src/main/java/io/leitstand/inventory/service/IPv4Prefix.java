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
import io.leitstand.inventory.jsonb.IPv4PrefixAdapter;

/**
 * An IPv4 prefix.
 */
@JsonbTypeAdapter(IPv4PrefixAdapter.class)
public class IPv4Prefix extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	/**
     * Creates an <code>IPv4Prefix</code> from the specified string.
     * @param prefix the IPv4 prefix
     * @return the <code>IPv4Prefix</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static IPv4Prefix valueOf(String prefix){
		return fromString(prefix,IPv4Prefix::new);
	}
	
	//TODO Pattern
	private String value;
	
	/**
	 * Create a <code>IP4Prefix</code>
	 * @param value - the prefix value
	 */
	public IPv4Prefix(String value){
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

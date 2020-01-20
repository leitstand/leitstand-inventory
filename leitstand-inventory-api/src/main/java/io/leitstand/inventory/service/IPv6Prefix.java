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
import io.leitstand.inventory.jsonb.IPv6PrefixAdapter;

/**
 * An IPv6 prefix.
 */
@JsonbTypeAdapter(IPv6PrefixAdapter.class)
public class IPv6Prefix extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a <code>IPv6Prefix</code> from the specified string.
	 * @param prefix the IPv6 prefix
	 * @return the <code>IPv6Prefix</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static IPv6Prefix valueOf(String prefix){
		return fromString(prefix,IPv6Prefix::new);
	}
	
	private String value;
	
	/**
	 * Creates a <code>IPv6Prefix</code>
	 * @param prefix the IPv6 prefix
	 */
	public IPv6Prefix(String prefix){
		this.value = prefix;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return value;
	}
	
}

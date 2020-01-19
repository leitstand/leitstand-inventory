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

import static io.leitstand.commons.model.Patterns.IP_PREFIX;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.IPvxPrefixAdapter;

/** 
 * IPv4 or IPv6 address in CIDR notation.
 */
@JsonbTypeAdapter(IPvxPrefixAdapter.class)
public class IPvxPrefix extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a <code>CidrAddress</code> from the specified string
	 * @param address an IP address in CIDR notation
	 * @return the <code>CidrAddress</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static IPvxPrefix valueOf(String address) {
		return Scalar.fromString(address, IPvxPrefix::new);
	}
	
	@Pattern(regexp=IP_PREFIX)
	private String value;
	
	/**
	 * Creates a <code>CidrAddress</code>.
	 * @param address the IP address
	 */
	public IPvxPrefix(String address) {
		this.value = address;
	}

	/**
	 * Tests whether this prefix is an IPv4 prefix.
	 * @return <code>true</code> if this prefix is an IPv4 prefix, otherwise <code>false</code>
	 */
	public boolean isIPv4Prefix() {
		return this.value.matches(IP_PREFIX);
	}

	/**
	 * Tests whether this prefix is an IPv6 prefix.
	 * @return <code>true</code> if this prefix is an IPv6 prefix, otherwise <code>false</code>
	 */
	public boolean isIPv6Prefix() {
		return this.value.matches(IP_PREFIX);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return value;
	}
	
}

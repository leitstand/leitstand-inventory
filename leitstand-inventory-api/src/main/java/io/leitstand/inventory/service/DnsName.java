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

import static io.leitstand.commons.model.Patterns.DNS_PATTERN;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.DnsNameAdapter;

/**
 * A DNS name.
 */
@JsonbTypeAdapter(DnsNameAdapter.class)
public class DnsName extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a DNS name from the specified string.
	 * <p>
	 * Returns <code>null</code> if the specified string is <code>null</code> or empty.
	 * <p>
	 * This method is an alias of the {@link #valueOf(String)} method to improve readability by avoiding static import conflicts.
	 * @param name the DNS name.
	 * @return the DNS name.
	 */
	public static DnsName dnsName(String name) {
		return valueOf(name);
	}
	
	/**
	 * Creates a DNS name from the specified string.
	 * <p>
	 * Returns <code>null</code> if the specified string is <code>null</code> or empty.
	 * @param name the DNS name.
	 * @return the DNS name.
	 */
	public static DnsName valueOf(String name) {
		return fromString(name, DnsName::new);
	}
	
	@Pattern(regexp=DNS_PATTERN, message="{dns_name.invalid}")
	private String value;
	
	/**
	 * Creates a new <code>DnsName</code>.
	 * @param name the DNS name
	 */
	public DnsName(String name) {
		this.value = name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return value;
	}

	/**
	 * Tests with this DNS name is suffixed by the given DNS zone name.
	 * @param dnsZoneName the DNS zone name
	 * @return <code>true</code> if this DNS name ends with the specified DNS zone name, <code>false</code> otherwise.
	 */
	public boolean endsWidth(DnsZoneName dnsZoneName) {
		return getValue().endsWith(dnsZoneName.getValue());
	}

	
	
}

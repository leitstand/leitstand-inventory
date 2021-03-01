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
import io.leitstand.inventory.jsonb.DnsRecordTypeAdapter;

/**
 * The DNS record type.
 * <p>
 * The DNS record type defines what values are stored in the DNS records.
 * <p>
 * For example,
 * <ul>
 *  <li><code>A</code> DNS records map a DNS name to <code>IPv4</code> addresses. The DNS record value contains the IPv4 addresses</li>
 *  <li><code>AAAA</code> DNS records map a DNS name to <code>IPv6</code> addresses. The DNS record value contains the IPv6 addresses</li>
 *  <li><code>CNAME</code> DNS records store an alias for a DNS name. The DNS record value contains the alias</li>
 * </ul>
 * 
 * @see DnsRecordSet
 * @see DnsRecord
 *
 */
@JsonbTypeAdapter(DnsRecordTypeAdapter.class)
public class DnsRecordType extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a DNS record type from the specified string.
	 * Returns <code>null</code> if the specified string is <code>null</code> or empty.
	 * <p>
	 * This method is an alias of the {@link #valueOf(String)} method to improve readability by avoiding static import conflicts.
	 * @param type the DNS record type
	 * @return a DNS record type or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static DnsRecordType dnsRecordType(String type) {
		return valueOf(type);
	}
	
    /**
     * Creates a DNS record type from the specified string.
     * Returns <code>null</code> if the specified string is <code>null</code> or empty.
     * @param type the DNS record type
     * @return a DNS record type or <code>null</code> if the specified string is <code>null</code> or empty.
     */
	public static DnsRecordType valueOf(String type) {
		return fromString(type, DnsRecordType::new);
	}
	
	@NotNull(message="{dns_type.required}")
	@Pattern(regexp="[A-Z0-9]+", message="{dns_type.invalid}")
	private String value;
	
	/**
	 * Creates a new DNS record type.
	 * @param type the DNS record type.
	 */
	public DnsRecordType(String type) {
		this.value = type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return value;
	}
	
	
}

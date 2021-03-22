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

import static io.leitstand.commons.model.Patterns.UUID_PATTERN;

import java.util.UUID;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.DnsZoneIdAdapter;

/**
 * Unique DNS zone ID in UUIDv4 format.
 * <p>
 * The DNS zone ID is an immutable identifier for DNS zones stored in the inventory.
 * It simplifies renaming DNS zones and is not shared with the DNS server.
 */
@JsonbTypeAdapter(DnsZoneIdAdapter.class)
public class DnsZoneId extends Scalar<String> {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a random DNS zone ID.
	 * @return a random DNS zone ID.
	 */
	public static DnsZoneId randomDnsZoneId() {
		return valueOf(UUID.randomUUID().toString());
	}
	
	/**
	 * Creates a DNS zone ID from the specified string.
	 * Returns <code>null</code> if the specified string is <code>null</code> or empty.
	 * <p>
	 * This method is an alias of the {@link #valueOf(String)} method to improve readability by avoiding static import conflicts.
	 * @param id the DNS zone ID.
	 * @return the DNS zone ID.
	 */
	public static  DnsZoneId dnsZoneId(String id) {
		return valueOf(id);
	}
	
	/**
	 * Creates a DNS zone ID from the specified string.
	 * Returns <code>null</code> if the specified string is <code>null</code> or empty.
	 * @param id the DNS zone ID
	 * @return
	 */
	public static final DnsZoneId valueOf(String id) {
		return fromString(id, DnsZoneId::new);
	}
	
	@NotNull(message="{dns_zone_id.required}")
	@Pattern(regexp=UUID_PATTERN, message="{dns_zone_id.invalid}")
	private String value;
	
	/**
	 * Creates a <code>DnsZoneId</code>
	 * @param id the DNS zone ID
	 */
	public DnsZoneId(String id) {
		this.value = id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return value;
	}
	
}

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

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static io.leitstand.inventory.service.DnsRecordSetId.randomDnsRecordSetId;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import javax.json.bind.annotation.JsonbProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.leitstand.commons.model.ValueObject;

/**
 * A DNS record set.
 * <p>
 * A DNS record set consists of 
 * <ul>
 *  <li>the DNS zone name</li>
 *  <li>the DNS name</li>
 *  <li>the DNS record type</li>
 *  <li>the DNS records</li>
 *  <li>the DNS TTL (time-to-live, defaults to 3600seconds)</li>
 * </ul>
 * 
 * All records in a DNS record set have the same <a href="https://en.wikipedia.org/wiki/List_of_DNS_record_types">DNS record type</a>.
 */
public class DnsRecordSet extends ValueObject {
	
    /**
     * Creates a builder for a <code>DnsRecordSet</code> instance.
     * @return a builder for a <code>DnsRecordSet</code> instance.
     */
	public static Builder newDnsRecordSet() {
		return new Builder();
	}
	
	/**
	 * Base builder for an immutable <code>DnsRecordSet</code> value object.
	 * @param <T> the DNS record set type.
	 * @param <B> the DNS record set builder.
	 */
	@SuppressWarnings("unchecked")
	public static class BaseDnsRecordSetBuilder<T extends DnsRecordSet,B extends BaseDnsRecordSetBuilder<T,B>> {
		
		protected T set;
		
		/**
		 * Creates a new <code>BaseDnsRecordSetBuilder</code>.
		 * @param set the value object under construction
		 */
		protected BaseDnsRecordSetBuilder(T set) {
			this.set = set;
		}
		
		/**
		 * Sets the DNS zone ID.
		 * @param zoneId the DNS zone ID.
		 * @return a reference to this builder to continue object creation.
		 */
		public B withDnsZoneId(DnsZoneId zoneId) {
			assertNotInvalidated(getClass(), set);
			((DnsRecordSet)set).dnsZoneId = zoneId;
			return (B) this;
		}

		/**
		 * Sets the DNS zone name.
		 * @param zoneName the DNS zone name
		 * @return a reference to this builder to continue object creation.
		 */
		public B withDnsZoneName(DnsZoneName zoneName) {
			assertNotInvalidated(getClass(), set);
			((DnsRecordSet)set).dnsZoneName = zoneName;
			return (B) this;
		}
		
		/**
		 * Sets the DNS record set ID.
		 * @param id the DNS record set ID
		 * @return a reference to this builder to continue object creation.
		 */
		public B withDnsRecordSetId(DnsRecordSetId id) {
			assertNotInvalidated(getClass(), set);
			((DnsRecordSet)set).dnsRecordSetId = id;
			return (B)this;
		}
		
		/**
		 * Sets the DNS name.
		 * @param name the DNS name.
		 * @return a reference to this builder to continue object creation.
		 */
		public B withDnsName(DnsName name) {
			assertNotInvalidated(getClass(), set);
			((DnsRecordSet)set).dnsName = name;
			return (B)this;
		}
		
		/**
		 * Sets the DNS record type.
		 * @param type the DNS record type.
		 * @return a reference to this builder to continue object creation.
		 */
		public B withDnsRecordType(DnsRecordType type) {
			assertNotInvalidated(getClass(), set);
			((DnsRecordSet)set).dnsType = type;
			return (B) this;
		}
		
		/**
		 * Sets the DNS time-to-live in seconds.
		 * @param ttl the time-to-live in seconds.
		 * @return a reference to this builder to continue object creation.
		 */
		public B withDnsRecordTimeToLive(int ttl) {
			assertNotInvalidated(getClass(), set);
			((DnsRecordSet)set).dnsTtl = ttl;
			return (B) this;
		}
		
		/**
		 * Sets the DNS records of this record set.
		 * @param records the DNS records.
		 * @return a reference to this builder to continue object creation.
		 */
		public B withDnsRecords(DnsRecord.Builder... records) {
			return withDnsRecords(stream(records)
								  .map(DnsRecord.Builder::build)
								  .collect(toList()));
		}
		
		/**
		 * Sets the DNS records of this record set.
		 * @param records the DNS records of this record set.
		 * @return a reference to this builder to continue object creation.
		 */
		public B withDnsRecords(List<DnsRecord> records) {
			assertNotInvalidated(getClass(), set);
			((DnsRecordSet)set).dnsRecords = new ArrayList<>(records);
			return (B)this;
		}
		
		/**
		 * Sets the DNS record set description
		 * @param description the description of this DNS record set
		 * @return a reference to this builder to continue object creation.
		 */
		public B withDescription(String description) {
			assertNotInvalidated(getClass(), set);
			((DnsRecordSet)set).description = description;
			return (B)this;
		}
		
		/**
		 * Returns an immutable DNS record set value object.
		 * @return
		 */
		public T build() {
			try {
				assertNotInvalidated(getClass(), set);
				return set;
			} finally {
				this.set = null;
			}
		}
		
	}
	
	/**
	 * A builder for an immutable <code>DnsRecordSet</code> instance.
	 */
	public static class Builder extends BaseDnsRecordSetBuilder<DnsRecordSet,Builder>{
	    
	    /**
	     * Creates a builder for an immutable <code>DnsRecordSet</code> instance.
	     */
		protected Builder() {
			super(new DnsRecordSet());
		}
	}
	
	@NotNull(message="{dns_zone_id.required}")
	@Valid
	private DnsZoneId dnsZoneId;
	@NotNull(message="{dns_zone_name.required}")
	@Valid
	private DnsZoneName dnsZoneName;
	
	@JsonbProperty("dns_recordset_id")
	@NotNull(message="{dns_recorset_id.required}")
	@Valid
	private DnsRecordSetId dnsRecordSetId = randomDnsRecordSetId();

	@NotNull(message="{dns_type.required}")
	@Valid
	private DnsRecordType dnsType;
	
	@NotNull(message="{dns_name.required}")
	@Valid
	private DnsName dnsName;
	private int dnsTtl = 3600;
	@Valid
	private List<DnsRecord> dnsRecords = emptyList();
	private String description;
	
	
	/**
	 * Returns the DNS record set ID.
	 * @return the DNS record set ID.
	 */
	public DnsRecordSetId getDnsRecordSetId() {
		return dnsRecordSetId;
	}
	
	/**
	 * Returns the DNS name.
	 * @return the DNS name.
	 */
	public DnsName getDnsName() {
		return dnsName;
	}
	
	/**
	 * Returns the DNS records.
	 * @return the DNS records.
	 */
	public List<DnsRecord> getDnsRecords() {
		return unmodifiableList(dnsRecords);
	}
	
	/**
	 * Returns the DNS record set description.
	 * @return the DNS record set description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Returns the DNS zone ID.
	 * @return the DNS zone ID.
	 */
	public DnsZoneId getDnsZoneId() {
		return dnsZoneId;
	}
	
	/**
	 * Returns the DNS zone name.
	 * @return the DNS zone name.
	 */
	public DnsZoneName getDnsZoneName() {
		return dnsZoneName;
	}
	
	/**
	 * Returns the DNS time-to-live (TTL) in seconds.
	 * @return the DNS time-to-live (TTL) in seconds.
	 */
	public int getDnsTtl() {
		return dnsTtl;
	}
	
	/**
	 * Returns the DNS type.
	 * @return the DNS type.
	 */
	public DnsRecordType getDnsType() {
		return dnsType;
	}
	

}

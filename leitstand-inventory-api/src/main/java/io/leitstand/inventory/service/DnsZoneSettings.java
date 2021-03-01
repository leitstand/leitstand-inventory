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
import static io.leitstand.inventory.service.DnsZoneId.randomDnsZoneId;

import javax.json.JsonObject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.leitstand.commons.model.ValueObject;

/**
 * DNS zone settings.
 * <p>
 * The DNS zone settings consist of 
 * <ul>
 *  <li>the immutable, unique DNS zone ID</li>
 *  <li>the unique DNS zone name</li>
 *  <li>an optional description and </li>
 *  <li>an optional configuration JSON object, that can be processed by the service programming the DNS server.</li>
 * </ul>
 * 
 */
public class DnsZoneSettings extends ValueObject {
	
    /**
     * Creates a builder for an immutable <code>DnsZoneSettings</code> value object.
     * @return a builder for an immutable <code>DnsZoneSettings</code> value object.
     */
	public static Builder newDnsZoneSettings() {
		return new Builder();
	}
	
	/**
	 * A base builder for DNS zone value objects.
	 * @param <T> the DNS zone value object
	 * @param <B> the DNS zone value object builder
	 */
	@SuppressWarnings("unchecked")
	protected static class DnsZoneSettingsBuilder<T extends DnsZoneSettings,B extends DnsZoneSettingsBuilder<T,B>>  {
		
		protected T zone;
		
		/**
		 * Creates a DNS zone settings builder.
		 * @param dns the value object under construction
		 */
		protected DnsZoneSettingsBuilder(T dns) {
			this.zone = dns;
		}
		
		/**
		 * Sets the DNS zone ID
		 * @param zoneId the DNS zone ID
		 * @return a reference to this builder to continue object creation
		 */
		public B withDnsZoneId(DnsZoneId zoneId) {
			assertNotInvalidated(getClass(), zone);
			((DnsZoneSettings)zone).dnsZoneId = zoneId;
			return (B) this;
		}

		/**
		 * Sets the DNS zone name
		 * @param zoneName the DNS zone name
         * @return a reference to this builder to continue object creation
		 */
		public B withDnsZoneName(DnsZoneName zoneName) {
			assertNotInvalidated(getClass(), zone);
			((DnsZoneSettings)zone).dnsZoneName = zoneName;
			return (B) this;
		}
		
		/**
		 * Sets the DNS zone description.
		 * @param description the DNS zone description
         * @return a reference to this builder to continue object creation
		 */
		public B withDescription(String description) {
			assertNotInvalidated(getClass(), zone);
			((DnsZoneSettings)zone).description = description;
			return (B) this;
		}

		/**
		 * Sets the DNS zone configuration object type.
		 * @param configType the configuration object type
         * @return a reference to this builder to continue object creation
		 */
		public B withDnsZoneConfigType(String configType) {
			assertNotInvalidated(getClass(), zone);
			((DnsZoneSettings)zone).dnsZoneConfigType = configType;
			return (B) this;
		}

		/**
		 * Sets the DNS zone configuration.
		 * @param config the configuration
         * @return a reference to this builder to continue object creation
		 */
		public B withDnsZoneConfig(JsonObject config) {
			assertNotInvalidated(getClass(), zone);
			((DnsZoneSettings)zone).dnsZoneConfig = config;
			return (B) this;
		}
		
		/**
		 * Creates an immutable <code>DnsZoneSettings</code> value object and invalidated this builder.
		 * Subsequent calls of the <code>build()</code> method raises and exception.
		 * @return the immutable DNS zone value object.
		 */
		public T build() {
			try {
				assertNotInvalidated(null, zone);
				return zone;
			} finally {
				this.zone = null;
			}
		}
	}
	
	/**
	 * A buider to create an immutable <code>DnsZoneSettings</code> value object.
	 */
	public static class Builder extends DnsZoneSettingsBuilder<DnsZoneSettings, Builder>{
	    /**
	     * Creates a DNS zone settings builder.
	     */
	    protected Builder() {
			super(new DnsZoneSettings());
		}
	}
	
	@Valid
	@NotNull(message="{dns_zone_id.required}")
	private DnsZoneId dnsZoneId = randomDnsZoneId();
	
	@Valid
	@NotNull(message="{dns_zone_name.required}")
	private DnsZoneName dnsZoneName;
	
	private String dnsZoneConfigType;
	
	private JsonObject dnsZoneConfig;
	
	private String description;

	
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
	 * Returns the DNS zone description.
	 * @return the DNS zone description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Returns the DNS zone configuration.
	 * @return the DNS zone configuration.
	 */
	public JsonObject getDnsZoneConfig() {
		return dnsZoneConfig;
	}
	
	/**
	 * Returns the DNS zone configuration object type.
	 * @return the DNS zone configuration object type.
	 */
	public String getDnsZoneConfigType() {
		return dnsZoneConfigType;
	}
	
}


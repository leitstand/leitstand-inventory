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

import io.leitstand.commons.model.ValueObject;

public class DnsZoneSettings extends ValueObject {
	
	public static Builder newDnsZoneSettings() {
		return new Builder();
	}
	
	protected static class DnsZoneSettingsBuilder<T extends DnsZoneSettings,B extends DnsZoneSettingsBuilder<T,B>>  {
		
		protected T zone;
		
		protected DnsZoneSettingsBuilder(T dns) {
			this.zone = dns;
		}
		
		public B withDnsZoneId(DnsZoneId zoneId) {
			assertNotInvalidated(getClass(), zone);
			((DnsZoneSettings)zone).dnsZoneId = zoneId;
			return (B) this;
		}
		
		public B withDnsZoneName(DnsZoneName zoneName) {
			assertNotInvalidated(getClass(), zone);
			((DnsZoneSettings)zone).dnsZoneName = zoneName;
			return (B) this;
		}
		
		public B withDescription(String description) {
			assertNotInvalidated(getClass(), zone);
			((DnsZoneSettings)zone).description = description;
			return (B) this;
		}
		
		public B withDnsZoneConfigType(String configType) {
			assertNotInvalidated(getClass(), zone);
			((DnsZoneSettings)zone).dnsZoneConfigType = configType;
			return (B) this;
		}
		
		public B withDnsZoneConfig(JsonObject config) {
			assertNotInvalidated(getClass(), zone);
			((DnsZoneSettings)zone).dnsZoneConfig = config;
			return (B) this;
		}
		
		public T build() {
			try {
				assertNotInvalidated(null, zone);
				return zone;
			} finally {
				this.zone = null;
			}
		}
	}
	
	public static class Builder extends DnsZoneSettingsBuilder<DnsZoneSettings, Builder>{
		protected Builder() {
			super(new DnsZoneSettings());
		}
	}
	
	private DnsZoneId dnsZoneId = randomDnsZoneId();
	
	private DnsZoneName dnsZoneName;
	
	private String dnsZoneConfigType;
	
	private JsonObject dnsZoneConfig;
	
	private String description;

	
	public DnsZoneId getDnsZoneId() {
		return dnsZoneId;
	}
	
	public DnsZoneName getDnsZoneName() {
		return dnsZoneName;
	}
	
	public String getDescription() {
		return description;
	}
	
	public JsonObject getDnsZoneConfig() {
		return dnsZoneConfig;
	}
	
	public String getDnsZoneConfigType() {
		return dnsZoneConfigType;
	}
	
}

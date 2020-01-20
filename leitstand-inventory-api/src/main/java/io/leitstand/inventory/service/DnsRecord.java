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

import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

import io.leitstand.commons.jpa.BooleanConverter;
import io.leitstand.commons.model.ValueObject;

@Embeddable
public class DnsRecord extends ValueObject{

	public static Builder newDnsRecord() {
		return new Builder();
	}
	
	public static class Builder {
		
		private DnsRecord record = new DnsRecord();
		
		
		public Builder withRecordValue(String value) {
			assertNotInvalidated(getClass(), record);
			record.dnsRecordValue = value;
			return this;
		}
		
		public Builder withDisabled(boolean disabled) {
			assertNotInvalidated(getClass(),record);
			record.disabled = disabled;
			return this;
		}
		
		public Builder withSetPtr(boolean setPtr) {
			assertNotInvalidated(getClass(),record);
			record.setPtr = setPtr;
			return this;
		}

		public DnsRecord build() {
			try {
				assertNotInvalidated(getClass(), record);
				return this.record;
			} finally {
				this.record = null;
			}
		}
		
	}
	
	@JsonbProperty("dns_value")
	private String dnsRecordValue;
	
	@Convert(converter=BooleanConverter.class)
	private boolean disabled;

	@JsonbProperty("dns_setptr")
	@Convert(converter=BooleanConverter.class)
	private boolean setPtr;
	
	public boolean isDisabled() {
		return disabled;
	}
	
	public String getDnsRecordValue() {
		return dnsRecordValue;
	}
	
	public boolean isSetPtr() {
		return setPtr;
	}
}

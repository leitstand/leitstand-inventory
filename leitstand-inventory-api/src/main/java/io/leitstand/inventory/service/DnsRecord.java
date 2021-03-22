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

/**
 * A DNS record of a DNS record set.
 * @see DnsRecordSet
 */
@Embeddable
public class DnsRecord extends ValueObject{

    /**
     * Returns a builder to create a DNS record.
     * @return a builder to create a DNS record.
     */
	public static Builder newDnsRecord() {
		return new Builder();
	}
	
	/**
	 * A builder to create an immutable DNS record.
	 */
	public static class Builder {
		
		private DnsRecord record = new DnsRecord();
		
		
		/**
		 * Sets the DNS record value. 
		 * @param value the DNS record value.
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withRecordValue(String value) {
			assertNotInvalidated(getClass(), record);
			record.dnsRecordValue = value;
			return this;
		}
		
		/**
		 * Sets the record disabled flag.
		 * The DNS record is disabled if the flag is set to <code>true</code> and enabled if it is set to <code>false</code>.
		 * @param disabled the disabled flag.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withDisabled(boolean disabled) {
			assertNotInvalidated(getClass(),record);
			record.disabled = disabled;
			return this;
		}
		
		/**
		 * Sets the <i>set pointer record (PTR)</i> flag indicating to create a pointer record for reverse DNS lookups.
		 * The flag is set to <code>true</code> if a pointer record for reverse DNS lookups exists.
		 * @param setPtr the set PTR flag
		 * @return
		 */
		public Builder withSetPtr(boolean setPtr) {
			assertNotInvalidated(getClass(),record);
			record.setPtr = setPtr;
			return this;
		}

		/**
		 * Creates an immutable <code>DnsRecord</code> instance and invalidates this builder.
		 * Subsequent calls of the <code>build()</code> method raise an exception.
		 * @return the immutable <code>DnsRecord</code> instance.
		 */
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
	
	/**
	 * Returns whether this DNS record is disabled.
	 * @return <code>true</code> if this DNS record is disabled, <code>false</code> otherwise.
	 */
	public boolean isDisabled() {
		return disabled;
	}
	
	/**
	 * Returns the DNS record value.
	 * @return the DNS record value.
	 */
	public String getDnsRecordValue() {
		return dnsRecordValue;
	}
	
	/**
	 * Returns whether a pointer record (PTR) for reverse DNS lookups exists.
	 * @return <code>true</code> if a pointer record exists.
	 */
	public boolean isSetPtr() {
		return setPtr;
	}
}

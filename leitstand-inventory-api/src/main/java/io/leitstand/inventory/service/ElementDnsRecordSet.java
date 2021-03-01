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
import javax.validation.Valid;

/**
 * DNS resource record set of an element.
 */
public class ElementDnsRecordSet extends BaseElementEnvelope {

    /**
     * Creates a builder for an immutable <code>ElementDnsRecordSet</code> value object.
     * @return a builder for an immutable <code>ElementDnsRecordSet</code> value object.
     */
	public static Builder newElementDnsRecordSet() {
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>ElementDnsRecordSet</code> value object.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementDnsRecordSet, Builder>{

	    /**
	     * Creates a builder for an immutable <code>ElementDnsRecordSet</code> value object.
	     */
	    protected Builder() {
			super(new ElementDnsRecordSet());
		}
	
	    /**
	     * Sets the DNS record set.
	     * @param set the DNS record set
	     * @return a reference to this builder to continue object creation.
	     */
		public Builder withDnsRecordSet(DnsRecordSet.Builder set) {
			return withDnsRecordSet(set.build());
		}
		
		/**
		 * Sets the DNS record set.
		 * @param set the DNS record set.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withDnsRecordSet(DnsRecordSet set) {
			assertNotInvalidated(getClass(),object);
			object.dnsRecordSet = set;
			return this;
		}
	}
	
	@Valid
	@JsonbProperty("dns_recordset")
	private DnsRecordSet dnsRecordSet;
	
	
	/**
	 * Returns the DNS record set.
	 * @return the DNS record set.
	 */
	public DnsRecordSet getDnsRecordSet() {
		return dnsRecordSet;
	}
}

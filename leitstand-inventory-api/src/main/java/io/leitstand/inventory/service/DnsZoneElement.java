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
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import javax.json.bind.annotation.JsonbProperty;

/**
 * Contains all DNS record sets of an element in a DNS zone.
 */
public class DnsZoneElement extends BaseElementEnvelope {

    /**
     * Returns a builder to create a <code>DnsZoneElement</code> instance.
     * @return a builder to create a <code>DnsZoneElement</code> instance.
     */
	public static Builder newDnsZoneElement() {
		return new Builder();
	}
	
	/**
	 * A builder to create an immutable <code>DnsZoneElement</code> instance.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<DnsZoneElement, Builder>{

	    /**
	     * Creates a builder for a <code>DnsZoneElement</code> instance.
	     */
	    protected Builder() {
			super(new DnsZoneElement());
		}
		
	    /**
	     * Sets the element DNS record sets.
	     * @param dnsRecordSets the DNS record sets.
	     * @return a reference to this builder to continue object creation
	     */
		public Builder withDnsRecordSets(DnsRecordSet.Builder... dnsRecordSets) {
			return withDnsRecordSets(stream(dnsRecordSets)
									 .map(DnsRecordSet.Builder::build)
									 .collect(toList()));
		}
		
		/**
		 * Sets the element DNS record sets.
		 * @param dnsRecordSets the DNS record sets.
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withDnsRecordSets(List<DnsRecordSet> dnsRecordSets) {
			assertNotInvalidated(getClass(), object);
			object.dnsRecordSets = new ArrayList<>(dnsRecordSets);
			return this;
		}
	}
	
	@JsonbProperty("dns_recordsets")
	private List<DnsRecordSet> dnsRecordSets; 
	
	/**
	 * Returns the DNS record sets.
	 * @return the DNS record sets.
	 */
	public List<DnsRecordSet> getDnsRecordSets(){
		return unmodifiableList(dnsRecordSets);
	}

}

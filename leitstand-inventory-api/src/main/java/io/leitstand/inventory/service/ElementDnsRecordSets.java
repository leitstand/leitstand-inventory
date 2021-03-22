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

import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import javax.json.bind.annotation.JsonbProperty;
import javax.validation.Valid;

import io.leitstand.commons.model.BuilderUtil;

/**
 * All DNS records of an element.
 */
public class ElementDnsRecordSets extends BaseElementEnvelope {

    /**
     * Creates a builder for an immutable <code>ElementDnsRecordSets</code> value object.
     * @return a builder for an immutable <code>ElementDnsRecordSets</code> value object.
     */
	public static Builder newElementDnsRecordSets() {
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>ElementDnsRecordSets</code> value object.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementDnsRecordSets, Builder>{
		
	    /**
	     * Creates a builder for an immutable <code>ElementDnsRecordSets</code> value object.
	     */
	    protected Builder() {
			super(new ElementDnsRecordSets());
		}

		/**
		 * Sets the DNS records.
		 * @param sets the DNS records
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withDnsRecordSets(DnsRecordSet.Builder... sets) {
			return withDnsRecordSets(stream(sets)
									 .map(DnsRecordSet.Builder::build)
									 .collect(toList()));
		}
		
		/**
		 * Sets the DNS records.
		 * @param sets the DNS records
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withDnsRecordSets(List<DnsRecordSet> sets) {
			BuilderUtil.assertNotInvalidated(getClass(),object);
			object.dnsRecordSets = new ArrayList<>(sets);
			return this;
		}
	}
	
	@Valid
	@JsonbProperty("dns_recordsets")
	private List<DnsRecordSet> dnsRecordSets = emptyList();
	
	/**
	 * Returns the DNS records.
	 * @return the DNS records.
	 */
	public List<DnsRecordSet> getDnsRecordSets() {
		return unmodifiableList(dnsRecordSets);
	} 

}

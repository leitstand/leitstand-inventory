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

import io.leitstand.commons.model.BuilderUtil;

public class ElementDnsRecordSets extends BaseElementEnvelope {

	public static Builder newElementDnsRecordSets() {
		return new Builder();
	}
	
	public static class Builder extends BaseElementEnvelopeBuilder<ElementDnsRecordSets, Builder>{
		protected Builder() {
			super(new ElementDnsRecordSets());
		}
	
		public Builder withDnsRecordSets(DnsRecordSet.Builder... sets) {
			return withDnsRecordSets(stream(sets)
									 .map(DnsRecordSet.Builder::build)
									 .collect(toList()));
		}
		
		public Builder withDnsRecordSets(List<DnsRecordSet> sets) {
			BuilderUtil.assertNotInvalidated(getClass(),object);
			object.dnsRecordSets = new ArrayList<>(sets);
			return this;
		}
	}
	
	@JsonbProperty("dns_recordsets")
	private List<DnsRecordSet> dnsRecordSets = emptyList();
	
	
	public List<DnsRecordSet> getDnsRecordSets() {
		return unmodifiableList(dnsRecordSets);
	} 

}

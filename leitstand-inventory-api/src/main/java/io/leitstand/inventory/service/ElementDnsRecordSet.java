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

import javax.json.bind.annotation.JsonbProperty;

import io.leitstand.commons.model.BuilderUtil;

public class ElementDnsRecordSet extends BaseElementEnvelope {

	public static Builder newElementDnsRecordSet() {
		return new Builder();
	}
	
	public static class Builder extends BaseElementEnvelopeBuilder<ElementDnsRecordSet, Builder>{
		protected Builder() {
			super(new ElementDnsRecordSet());
		}
	
		public Builder withDnsRecordSet(DnsRecordSet.Builder set) {
			return withDnsRecordSet(set.build());
		}
		
		public Builder withDnsRecordSet(DnsRecordSet set) {
			BuilderUtil.assertNotInvalidated(getClass(),object);
			object.dnsRecordSet = set;
			return this;
		}
	}
	
	@JsonbProperty("dns_recordset")
	private DnsRecordSet dnsRecordSet;
	
	
	public DnsRecordSet getDnsRecordSet() {
		return dnsRecordSet;
	}
}
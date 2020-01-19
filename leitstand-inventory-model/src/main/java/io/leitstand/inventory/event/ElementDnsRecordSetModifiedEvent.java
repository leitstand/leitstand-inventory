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
package io.leitstand.inventory.event;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import javax.json.bind.annotation.JsonbProperty;

public class ElementDnsRecordSetModifiedEvent extends ElementEvent {

	public static Builder newDnsRecordSetChangedEvent() {
		return new Builder();
	}
	
	public static class Builder extends ElementEventBuilder<ElementDnsRecordSetModifiedEvent, Builder> {
		
		protected Builder() {
			super(new ElementDnsRecordSetModifiedEvent());
		}
		
		public Builder withDnsRecordSetUpdate(DnsRecordSetUpdate.Builder set) {
			return withDnsRecordSetUpdate(set.build());
		}
			
		
		public Builder withDnsRecordSetUpdate(DnsRecordSetUpdate set) {
			assertNotInvalidated(getClass(), object);
			((ElementDnsRecordSetModifiedEvent)object).dnsRecordSetUpdate = set;
			return this;
		}
		
	}
	
	
	@JsonbProperty("dns_recordset")
	private DnsRecordSetUpdate dnsRecordSetUpdate;
	
	public DnsRecordSetUpdate getDnsRecordSetUpdate() {
		return dnsRecordSetUpdate;
	}


	
}

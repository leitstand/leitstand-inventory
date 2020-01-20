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

import io.leitstand.inventory.service.DnsName;
import io.leitstand.inventory.service.DnsRecordSet;

public class DnsRecordSetUpdate extends DnsRecordSet {

	public static Builder newDnsRecordSetUpdate() {
		return new Builder();
	}
	
	public static class Builder extends BaseDnsRecordSetBuilder<DnsRecordSetUpdate, Builder>{
		protected Builder() {
			super(new DnsRecordSetUpdate());
		}
		
		public Builder withDnsWithdrawnName(DnsName dnsName) {
			assertNotInvalidated(getClass(), set);
			set.dnsWithdrawnName = dnsName;
			return this;
		}
	}
	
	private DnsName dnsWithdrawnName;
	
	public DnsName getDnsWithdrawnName() {
		return dnsWithdrawnName;
	}
}

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

import io.leitstand.commons.model.ValueObject;
import io.leitstand.inventory.service.DnsZoneId;
import io.leitstand.inventory.service.DnsZoneName;

public class DnsZoneEvent extends ValueObject{

	protected static class DnsZoneEventBuilder<T extends DnsZoneEvent,B extends DnsZoneEventBuilder<T,B >>{
		
		protected T event;
		
		protected DnsZoneEventBuilder(T event) {
			this.event = event;
		}
		
		public B withDnsZoneId(DnsZoneId zoneId) {
			assertNotInvalidated(getClass(), event);
			((DnsZoneEvent)event).dnsZoneId = zoneId;
			return (B) this;
		}
		
		public B withDnsZoneName(DnsZoneName zoneName) {
			assertNotInvalidated(getClass(), event);
			((DnsZoneEvent)event).dnsZoneName = zoneName;
			return (B) this;
		}
		
		public T build() {
			try {
				assertNotInvalidated(getClass(), event);
				return event;
			} finally {
				this.event = null;
			}
		}
	}
	
	private DnsZoneId dnsZoneId;
	private DnsZoneName dnsZoneName;
	

	public DnsZoneId getDnsZoneId() {
		return dnsZoneId;
	}
	
	public DnsZoneName getDnsZoneName() {
		return dnsZoneName;
	}

}

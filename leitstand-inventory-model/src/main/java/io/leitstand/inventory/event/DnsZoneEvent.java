/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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

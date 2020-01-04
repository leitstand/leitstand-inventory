/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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

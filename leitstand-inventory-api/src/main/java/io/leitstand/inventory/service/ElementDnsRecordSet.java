/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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

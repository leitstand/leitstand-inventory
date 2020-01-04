/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

public class DnsZoneElement extends BaseElementEnvelope {

	public static Builder newDnsZoneElement() {
		return new Builder();
	}
	
	public static class Builder extends BaseElementEnvelopeBuilder<DnsZoneElement, Builder>{
		protected Builder() {
			super(new DnsZoneElement());
		}
		
		public Builder withDnsRecordSets(DnsRecordSet.Builder... dnsRecordSets) {
			return withDnsRecordSets(stream(dnsRecordSets)
									 .map(DnsRecordSet.Builder::build)
									 .collect(toList()));
		}
		
		public Builder withDnsRecordSets(List<DnsRecordSet> dnsRecordSets) {
			assertNotInvalidated(getClass(), object);
			object.dnsRecordSets = new ArrayList<>(dnsRecordSets);
			return this;
		}
	}
	
	private List<DnsRecordSet> dnsRecordSets; 
	
	public List<DnsRecordSet> getDnsRecordSets(){
		return unmodifiableList(dnsRecordSets);
	}

}

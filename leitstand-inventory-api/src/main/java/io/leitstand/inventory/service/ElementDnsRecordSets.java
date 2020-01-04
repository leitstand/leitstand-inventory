/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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

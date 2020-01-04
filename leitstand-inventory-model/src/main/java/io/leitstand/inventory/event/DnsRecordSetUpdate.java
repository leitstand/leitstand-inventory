/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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

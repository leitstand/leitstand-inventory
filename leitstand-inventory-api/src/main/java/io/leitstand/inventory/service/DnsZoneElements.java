/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

public class DnsZoneElements extends DnsZoneSettings {

	public static Builder newDnsZoneElements() {
		return new Builder();
	}
	
	public static class Builder extends DnsZoneSettingsBuilder<DnsZoneElements, Builder>{
	
		protected Builder() {
			super(new DnsZoneElements());
		}
		
		public Builder withDnsEntries(DnsZoneElement.Builder... entries) {
			return withDnsEntries(stream(entries)
								  .map(DnsZoneElement.Builder::build)
								  .collect(toList()));
		}
		
		public Builder withDnsEntries(List<DnsZoneElement> entries) {
			assertNotInvalidated(getClass(), zone);
			zone.dnsEntries = new ArrayList<>(entries);
			return this;
		}
		
	}
	
	private List<DnsZoneElement> dnsEntries = emptyList();
	
	public List<DnsZoneElement> getDnsEntries() {
		return unmodifiableList(dnsEntries);
	}

	
}
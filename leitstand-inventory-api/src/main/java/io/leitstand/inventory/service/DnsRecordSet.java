/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static io.leitstand.inventory.service.DnsRecordSetId.randomDnsRecordSetId;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import javax.json.bind.annotation.JsonbProperty;

import io.leitstand.commons.model.ValueObject;

public class DnsRecordSet extends ValueObject {
	
	public static Builder newDnsRecordSet() {
		return new Builder();
	}
	
	public static class BaseDnsRecordSetBuilder<T extends DnsRecordSet,B extends BaseDnsRecordSetBuilder<T,B>> {
		
		protected T set;
		
		protected BaseDnsRecordSetBuilder(T set) {
			this.set = set;
		}
		
		public B withDnsZoneId(DnsZoneId zoneId) {
			assertNotInvalidated(getClass(), set);
			((DnsRecordSet)set).dnsZoneId = zoneId;
			return (B) this;
		}

		public B withDnsZoneName(DnsZoneName zoneName) {
			assertNotInvalidated(getClass(), set);
			((DnsRecordSet)set).dnsZoneName = zoneName;
			return (B) this;
		}
		
		public B withDnsRecordSetId(DnsRecordSetId id) {
			assertNotInvalidated(getClass(), set);
			((DnsRecordSet)set).dnsRecordSetId = id;
			return (B)this;
		}
		
		public B withDnsName(DnsName name) {
			assertNotInvalidated(getClass(), set);
			((DnsRecordSet)set).dnsName = name;
			return (B)this;
		}
		
		public B withDnsRecordType(DnsRecordType type) {
			assertNotInvalidated(getClass(), set);
			((DnsRecordSet)set).dnsType = type;
			return (B) this;
		}
		
		public B withDnsRecordTimeToLive(int ttl) {
			assertNotInvalidated(getClass(), set);
			((DnsRecordSet)set).dnsTtl = ttl;
			return (B) this;
		}
		
		public B withDnsRecords(DnsRecord.Builder... records) {
			return withDnsRecords(stream(records)
								  .map(DnsRecord.Builder::build)
								  .collect(toList()));
		}
		
		public B withDnsRecords(List<DnsRecord> records) {
			assertNotInvalidated(getClass(), set);
			((DnsRecordSet)set).dnsRecords = new ArrayList<>(records);
			return (B)this;
		}
		
		public B withDescription(String description) {
			assertNotInvalidated(getClass(), set);
			((DnsRecordSet)set).description = description;
			return (B)this;
		}
		
		public T build() {
			try {
				assertNotInvalidated(getClass(), set);
				return set;
			} finally {
				this.set = null;
			}
		}
		
	}
	
	public static class Builder extends BaseDnsRecordSetBuilder<DnsRecordSet,Builder>{
		protected Builder() {
			super(new DnsRecordSet());
		}
	}
	
	private DnsZoneId dnsZoneId;
	private DnsZoneName dnsZoneName;
	@JsonbProperty("dns_recordset_id")
	private DnsRecordSetId dnsRecordSetId = randomDnsRecordSetId();
	private DnsRecordType dnsType;
	private DnsName dnsName;
	private int dnsTtl = 3600;
	private List<DnsRecord> dnsRecords = emptyList();
	private String description;
	
	
	public DnsRecordSetId getDnsRecordSetId() {
		return dnsRecordSetId;
	}
	
	public DnsName getDnsName() {
		return dnsName;
	}
	
	public List<DnsRecord> getDnsRecords() {
		return dnsRecords;
	}
	
	public String getDescription() {
		return description;
	}
	
	public DnsZoneId getDnsZoneId() {
		return dnsZoneId;
	}
	
	public DnsZoneName getDnsZoneName() {
		return dnsZoneName;
	}
	
	public int getDnsTtl() {
		return dnsTtl;
	}
	
	public DnsRecordType getDnsType() {
		return dnsType;
	}
	

}

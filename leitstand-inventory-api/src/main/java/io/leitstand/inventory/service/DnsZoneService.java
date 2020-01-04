/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import java.util.List;

public interface DnsZoneService {

	List<DnsZoneSettings> getDnsZones(String filter, int offset, int limit);
	
	DnsZoneSettings getDnsZoneSettings(DnsZoneId zoneId);
	DnsZoneSettings getDnsZoneSettings(DnsZoneName zoneName);
	DnsZoneElements getDnsZoneElements(DnsZoneId zoneId);
	DnsZoneElements getDnsZoneElements(DnsZoneName zoneName);
	boolean storeDnsZoneSettings(DnsZoneSettings settings);
	void removeDnsZone(DnsZoneId zoneId);
	void removeDnsZone(DnsZoneName zoneName);
	void forceRemoveDnsZone(DnsZoneId zoneId);
	void forceRemoveDnsZone(DnsZoneName zoneName);
	
}

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

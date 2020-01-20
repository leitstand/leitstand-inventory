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
package io.leitstand.inventory.model;

import java.util.List;

import javax.inject.Inject;

import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.DnsZoneElements;
import io.leitstand.inventory.service.DnsZoneId;
import io.leitstand.inventory.service.DnsZoneName;
import io.leitstand.inventory.service.DnsZoneService;
import io.leitstand.inventory.service.DnsZoneSettings;

@Service
public class DefaultDnsZoneService implements DnsZoneService {

	private DnsZoneManager manager;
	
	private DnsZoneProvider zones;
	
	protected DefaultDnsZoneService() {
		//CDI
	}
	
	@Inject
	protected DefaultDnsZoneService(DnsZoneProvider zones, DnsZoneManager manager) {
		this.zones = zones;
		this.manager = manager;
	}
	
	@Override
	public DnsZoneSettings getDnsZoneSettings(DnsZoneId zoneId) {
		DnsZone zone = zones.fetchDnsZone(zoneId);
		return manager.getDnsZoneSettings(zone);
	}

	@Override
	public DnsZoneSettings getDnsZoneSettings(DnsZoneName zoneName) {
		DnsZone zone = zones.fetchDnsZone(zoneName);
		return manager.getDnsZoneSettings(zone);
	}

	@Override
	public DnsZoneElements getDnsZoneElements(DnsZoneId zoneId) {
		DnsZone zone = zones.fetchDnsZone(zoneId);
		return manager.getDnsZoneElements(zone);
	}

	@Override
	public DnsZoneElements getDnsZoneElements(DnsZoneName zoneName) {
		DnsZone zone = zones.fetchDnsZone(zoneName);
		return manager.getDnsZoneElements(zone);
	}

	@Override
	public boolean storeDnsZoneSettings(DnsZoneSettings settings) {
		DnsZone zone = zones.tryFetchDnsZone(settings.getDnsZoneId());
		if(zone != null) {
			manager.storeDnsZoneSettings(zone,settings);
			return false;
		}
		manager.createDnsZone(settings);
		return true;
	}

	@Override
	public void removeDnsZone(DnsZoneId zoneId) {
		DnsZone zone = zones.tryFetchDnsZone(zoneId);
		if(zone != null) {
			manager.removeDnsZone(zone);
		}
	}

	@Override
	public void removeDnsZone(DnsZoneName zoneName) {
		DnsZone zone = zones.tryFetchDnsZone(zoneName);
		if(zone != null) {
			manager.removeDnsZone(zone);
		}		
	}

	@Override
	public void forceRemoveDnsZone(DnsZoneId zoneId) {
		DnsZone zone = zones.fetchDnsZone(zoneId);
		manager.forceRemoveDnsZone(zone);
	}

	@Override
	public void forceRemoveDnsZone(DnsZoneName zoneName) {
		DnsZone zone = zones.fetchDnsZone(zoneName);
		manager.forceRemoveDnsZone(zone);
	}

	@Override
	public List<DnsZoneSettings> getDnsZones(String filter, int offset, int limit) {
		return manager.getDnsZones(filter,offset,limit);
	}

}

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

import static io.leitstand.inventory.model.DnsZone.findDnsZoneById;
import static io.leitstand.inventory.model.DnsZone.findDnsZoneByName;
import static io.leitstand.inventory.service.ReasonCode.IVT0950E_DNS_ZONE_NOT_FOUND;
import static java.lang.String.format;

import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.DnsZoneId;
import io.leitstand.inventory.service.DnsZoneName;

@Dependent
public class DnsZoneProvider {
	
	private static final Logger LOG = Logger.getLogger(DnsZoneProvider.class.getName());

	private Repository repository;
	
	protected DnsZoneProvider() {
		// CDI
	}
	
	@Inject
	protected DnsZoneProvider(@Inventory Repository repository) {
		this.repository = repository;
	}
	
	public DnsZone tryFetchDnsZone(DnsZoneId zoneId) {
		return repository.execute(findDnsZoneById(zoneId));
	}
	
	public DnsZone tryFetchDnsZone(DnsZoneName zoneName) {
		return repository.execute(findDnsZoneByName(zoneName));
	}
	
	public DnsZone fetchDnsZone(DnsZoneId zoneId) {
		DnsZone zone = tryFetchDnsZone(zoneId);
		if(zone == null) {
			LOG.fine(() -> format("%s: DNS zone %s does not exist!", 
								  IVT0950E_DNS_ZONE_NOT_FOUND.getReasonCode(),
								  zoneId ));
			throw new EntityNotFoundException(IVT0950E_DNS_ZONE_NOT_FOUND, zoneId);
		}
		return zone;
	}
	
	public DnsZone fetchDnsZone(DnsZoneName zoneName) {
		DnsZone zone = tryFetchDnsZone(zoneName);
		if(zone == null) {
			LOG.fine(() -> format("%s: DNS zone %s does not exist!", 
								  IVT0950E_DNS_ZONE_NOT_FOUND.getReasonCode(),
								  zoneName ));
			throw new EntityNotFoundException(IVT0950E_DNS_ZONE_NOT_FOUND, zoneName);
		}
		return zone;
	}
	
}

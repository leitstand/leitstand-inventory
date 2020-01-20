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

import static io.leitstand.inventory.service.DnsZoneId.randomDnsZoneId;
import static io.leitstand.inventory.service.DnsZoneName.dnsZoneName;
import static io.leitstand.inventory.service.ReasonCode.IVT0950E_DNS_ZONE_NOT_FOUND;
import static org.junit.Assert.assertNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.DnsZoneId;
import io.leitstand.inventory.service.DnsZoneName;
import io.leitstand.testing.ut.LeitstandCoreMatchers;

@RunWith(MockitoJUnitRunner.class)
public class DnsZoneProviderTest {

	private static final DnsZoneId ZONE_ID = randomDnsZoneId();
	private static final DnsZoneName ZONE_NAME = dnsZoneName("zone");
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private DnsZone zone;
	
	@Mock
	private Repository repository;
	
	@InjectMocks
	private DnsZoneProvider provider = new DnsZoneProvider();
	
	@Test
	public void throws_EntityNotFoundException_when_dns_zone_name_is_unknown() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(LeitstandCoreMatchers.reason(IVT0950E_DNS_ZONE_NOT_FOUND));
		
		provider.fetchDnsZone(ZONE_NAME);
	}
	
	@Test
	public void throws_EntityNotFoundException_when_dns_zone_id_is_unknown() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(LeitstandCoreMatchers.reason(IVT0950E_DNS_ZONE_NOT_FOUND));
		
		provider.fetchDnsZone(ZONE_ID);
	}
	
	@Test
	public void try_fetch_unknown_dns_zone_id_returns_null() {
		assertNull(provider.tryFetchDnsZone(ZONE_ID));
	}

	@Test
	public void try_fetch_unknown_dns_zone_name_returns_null() {
		assertNull(provider.tryFetchDnsZone(ZONE_NAME));
	}

}

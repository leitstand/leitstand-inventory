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
import static io.leitstand.inventory.service.DnsZoneSettings.newDnsZoneSettings;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.inventory.service.DnsZoneId;
import io.leitstand.inventory.service.DnsZoneName;
import io.leitstand.inventory.service.DnsZoneSettings;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDnsZoneServiceTest {
	
	private static final DnsZoneId ZONE_ID = randomDnsZoneId();
	private static final DnsZoneName ZONE_NAME = dnsZoneName("test.leitstand.io.");

	@Mock
	private DnsZone zone;
	
	@Mock
	private DnsZoneProvider zones;
	
	@Mock
	private DnsZoneManager manager;
	
	@InjectMocks
	private DefaultDnsZoneService service = new DefaultDnsZoneService();
	
	
	@Test
	public void remove_does_not_call_manager_for_unknown_zone_id() {
		service.removeDnsZone(ZONE_ID);
		verify(zones).tryFetchDnsZone(ZONE_ID);
		verify(manager,never()).removeDnsZone(any(DnsZone.class));
	}
	
	@Test
	public void remove_does_not_call_manager_for_unknown_zone_name() {
		service.removeDnsZone(ZONE_NAME);
		verify(zones).tryFetchDnsZone(ZONE_NAME);
		verify(manager,never()).removeDnsZone(any(DnsZone.class));
	}
	
	@Test
	public void remove_calls_manager_for_known_zone_id() {
		when(zones.tryFetchDnsZone(ZONE_ID)).thenReturn(zone);

		service.removeDnsZone(ZONE_ID);
		verify(manager).removeDnsZone(zone);
	}
	
	@Test
	public void remove_calls_manager_for_known_zone_name() {
		when(zones.tryFetchDnsZone(ZONE_NAME)).thenReturn(zone);
		
		service.removeDnsZone(ZONE_NAME);
		verify(manager).removeDnsZone(zone);
	}
	
	@Test
	public void create_dns_zone_for_unknown_zone() {
		DnsZoneSettings settings = newDnsZoneSettings()
								   .withDnsZoneId(ZONE_ID)
								   .withDnsZoneName(ZONE_NAME)
								   .build();
		
		boolean created = service.storeDnsZoneSettings(settings);
		assertTrue(created);
		verify(zones).tryFetchDnsZone(ZONE_ID);
		verify(manager).createDnsZone(settings);
	}
	
	@Test
	public void update_known_dns_zone() {
		DnsZoneSettings settings = newDnsZoneSettings()
							   	   .withDnsZoneId(ZONE_ID)
							   	   .withDnsZoneName(ZONE_NAME)
							   	   .build();
		when(zones.tryFetchDnsZone(ZONE_ID)).thenReturn(zone);
		
		boolean created = service.storeDnsZoneSettings(settings);
		assertFalse(created);
		verify(manager).storeDnsZoneSettings(zone, settings);
	}
}

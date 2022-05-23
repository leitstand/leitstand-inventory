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
package io.leitstand.inventory.rs;

import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.inventory.service.DnsZoneId.randomDnsZoneId;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.json.SerializableJsonObject;
import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.DnsZoneId;
import io.leitstand.inventory.service.DnsZoneName;
import io.leitstand.inventory.service.DnsZoneService;
import io.leitstand.inventory.service.DnsZoneSettings;

@RunWith(MockitoJUnitRunner.class)
public class DnsZoneResourceTest {
	
	private static final DnsZoneId ZONE_ID = randomDnsZoneId();
	private static final DnsZoneName ZONE_NAME = DnsZoneName.dnsZoneName("test.leitstand.io.");
	
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Mock
	private Messages messages;
	
	@Mock
	private DnsZoneService service;
	
	@InjectMocks
	private DnsZoneResource resource = new DnsZoneResource();
	
	@Test
	public void throw_UnprocessableEntityException_when_attempting_to_modify_the_dns_zone_id() {
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));
		
		DnsZoneSettings zone = mock(DnsZoneSettings.class);
		when(zone.getDnsZoneId()).thenReturn(ZONE_ID);
		
		resource.storeDnsZoneSettings(randomDnsZoneId(), zone);
		
		try {
			new SerializableJsonObject(null);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	@Test
	public void send_created_response_when_putting_a_new_dns_zone() {
		DnsZoneSettings zone = mock(DnsZoneSettings.class);
		when(zone.getDnsZoneId()).thenReturn(ZONE_ID);
		when(service.storeDnsZoneSettings(zone)).thenReturn(true);
		
		Response response = resource.storeDnsZoneSettings(ZONE_ID,zone);
		assertEquals(201,response.getStatus());
	}
	
	@Test
	public void send_success_response_when_putting_a_new_dns_zone() {
		DnsZoneSettings zone = mock(DnsZoneSettings.class);
		when(zone.getDnsZoneId()).thenReturn(ZONE_ID);
		when(service.storeDnsZoneSettings(zone)).thenReturn(false);
		
		Response response = resource.storeDnsZoneSettings(ZONE_ID,zone);
		assertEquals(200,response.getStatus());
	}
	
	@Test
	public void send_created_response_when_posting_a_new_dns_zone() {
		DnsZoneSettings zone = mock(DnsZoneSettings.class);
		when(zone.getDnsZoneId()).thenReturn(ZONE_ID);
		when(service.storeDnsZoneSettings(zone)).thenReturn(true);
		
		Response response = resource.storeDnsZoneSettings(zone);
		assertEquals(201,response.getStatus());
	}
	
	@Test
	public void send_success_response_when_posting_a_new_dns_zone() {
		DnsZoneSettings zone = mock(DnsZoneSettings.class);
		when(zone.getDnsZoneId()).thenReturn(ZONE_ID);
		when(service.storeDnsZoneSettings(zone)).thenReturn(false);
		
		Response response = resource.storeDnsZoneSettings(zone);
		assertEquals(200,response.getStatus());
	}
	
	@Test
	public void force_remove_zone_identified_by_id() {
		resource.removeDnsZone(ZONE_ID,true);
		verify(service).forceRemoveDnsZone(ZONE_ID);
	}
	
	@Test
	public void remove_zone_identified_by_id() {
		resource.removeDnsZone(ZONE_ID,false);
		verify(service).removeDnsZone(ZONE_ID);
	}
	
	@Test
	public void force_remove_zone_identified_by_name() {
		resource.removeDnsZone(ZONE_NAME,true);
		verify(service).forceRemoveDnsZone(ZONE_NAME);
	}
	
	@Test
	public void remove_zone_identified_by_name() {
		resource.removeDnsZone(ZONE_NAME,false);
		verify(service).removeDnsZone(ZONE_NAME);
	}
	
}

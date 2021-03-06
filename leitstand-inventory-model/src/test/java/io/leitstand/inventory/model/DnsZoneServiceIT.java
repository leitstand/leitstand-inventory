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
import static io.leitstand.inventory.service.ReasonCode.IVT0950E_DNS_ZONE_NOT_FOUND;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static java.lang.Integer.MAX_VALUE;
import static javax.json.Json.createObjectBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import javax.enterprise.event.Event;
import javax.json.JsonObject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.DnsZoneId;
import io.leitstand.inventory.service.DnsZoneName;
import io.leitstand.inventory.service.DnsZoneService;
import io.leitstand.inventory.service.DnsZoneSettings;

public class DnsZoneServiceIT extends InventoryIT{
	
	private static final DnsZoneId ZONE_ID = randomDnsZoneId();
	private static final DnsZoneName ZONE_NAME = dnsZoneName("zone");

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	private DnsZoneService service;
	
	private Repository repository;
	private DnsZoneProvider zones;
	
	@Before
	public void initTestEnvironment() {
		this.repository = new Repository(getEntityManager());
		this.zones = new DnsZoneProvider(repository);
		DnsZoneManager manager = new DnsZoneManager(repository,
													mock(Event.class),
													mock(Messages.class));
		service = new DefaultDnsZoneService(zones,manager);
		
	}
	
	@Test
	public void throw_EntityNotFoundException_when_dns_zone_id_is_unknown() {
	    exception.expect(EntityNotFoundException.class);
	    exception.expect(reason(IVT0950E_DNS_ZONE_NOT_FOUND));
	    transaction(()->{
	        service.getDnsZoneSettings(ZONE_ID);
	    });
	}
	
    @Test
    public void throw_EntityNotFoundException_when_dns_zone_name_is_unknown() {
        exception.expect(EntityNotFoundException.class);
        exception.expect(reason(IVT0950E_DNS_ZONE_NOT_FOUND));
        transaction(()->{
            service.getDnsZoneSettings(ZONE_NAME);
        });
    }
	
	@Test
	public void create_zone() {
		DnsZoneSettings zone = newDnsZoneSettings()
							   .withDnsZoneId(ZONE_ID)
							   .withDnsZoneName(ZONE_NAME)
							   .withDescription("description")
							   .build();
		
		transaction(()->{
			boolean created = service.storeDnsZoneSettings(zone);
			assertTrue(created);
		});
		
		transaction(()->{
			assertEquals(zone,service.getDnsZoneSettings(ZONE_NAME));
		});
	}
	
	
	@Test
	public void update_zone() {

		transaction(()->{
			DnsZoneSettings zone = newDnsZoneSettings()
								   .withDnsZoneId(ZONE_ID)
								   .withDnsZoneName(ZONE_NAME)
								   .withDescription("description")
								   .build();

			service.storeDnsZoneSettings(zone);
		});
		
		DnsZoneSettings zone = newDnsZoneSettings()
							   .withDnsZoneId(ZONE_ID)
							   .withDnsZoneName(ZONE_NAME)
							   .build();

		transaction(()->{
			boolean created = service.storeDnsZoneSettings(zone);
			assertFalse(created);
		});

		transaction(()->{
			assertEquals(zone,service.getDnsZoneSettings(ZONE_ID));
		});

	}
	
	@Test
	public void remove_zone_identified_by_id() {
		transaction(()->{
			DnsZoneSettings zone = newDnsZoneSettings()
								   .withDnsZoneId(ZONE_ID)
								   .withDnsZoneName(ZONE_NAME)
								   .withDescription("description")
								   .build();

			service.storeDnsZoneSettings(zone);
		});
		

		transaction(()->{
			service.removeDnsZone(ZONE_ID);
		});

		transaction(()->{
			assertTrue(service.getDnsZones(null,0,MAX_VALUE).isEmpty());
		});
	}
	
	@Test
	public void remove_zone_identified_by_name() {
		transaction(()->{
			DnsZoneSettings zone = newDnsZoneSettings()
								   .withDnsZoneId(ZONE_ID)
								   .withDnsZoneName(ZONE_NAME)
								   .withDescription("description")
								   .build();

			service.storeDnsZoneSettings(zone);
		});
		

		transaction(()->{
			service.removeDnsZone(ZONE_NAME);
		});

		transaction(()->{
			assertTrue(service.getDnsZones(null,0,MAX_VALUE).isEmpty());
		});
	}
	
	@Test
	public void store_zone_configuration() {
		JsonObject zoneConfig = createObjectBuilder()
								.add("endpoint", "http://dns.leitstand.io/zone")
								.build();
		
		
		transaction(()->{
			DnsZoneSettings zone = newDnsZoneSettings()
								   .withDnsZoneId(ZONE_ID)
								   .withDnsZoneName(ZONE_NAME)
								   .withDescription("description")
								   .withDnsZoneConfigType("unittest")
								   .withDnsZoneConfig(zoneConfig)
								   .build();

			service.storeDnsZoneSettings(zone);
		});
		

		transaction(()->{
			DnsZoneSettings zone = service.getDnsZoneSettings(ZONE_NAME);
			assertEquals("unittest",zone.getDnsZoneConfigType());
			assertEquals(zoneConfig,zone.getDnsZoneConfig());
		});

		transaction(()->{
			assertTrue(service.getDnsZones(null,0,MAX_VALUE).isEmpty());
		});
		
		
	}
	
}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.DnsZoneId.randomDnsZoneId;
import static io.leitstand.inventory.service.DnsZoneName.dnsZoneName;
import static io.leitstand.inventory.service.DnsZoneSettings.newDnsZoneSettings;
import static java.lang.Integer.MAX_VALUE;
import static javax.json.Json.createObjectBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import javax.enterprise.event.Event;
import javax.json.JsonObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
													mock(Messages.class),
													mock(Event.class));
		service = new DefaultDnsZoneService(zones,manager);
		
	}
	
	@After
	public void clearTestEnvironment() {
		transaction(()->{
			DnsZone zone = zones.tryFetchDnsZone(ZONE_ID);
			if(zone != null) {
				repository.remove(zone);
			}
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

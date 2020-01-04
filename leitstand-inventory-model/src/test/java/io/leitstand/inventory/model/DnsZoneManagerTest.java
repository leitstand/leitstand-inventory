/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.inventory.service.DnsZoneId.randomDnsZoneId;
import static io.leitstand.inventory.service.DnsZoneName.dnsZoneName;
import static io.leitstand.inventory.service.DnsZoneSettings.newDnsZoneSettings;
import static io.leitstand.inventory.service.ReasonCode.IVT0951I_DNS_ZONE_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0952I_DNS_ZONE_REMOVED;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.enterprise.event.Event;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Message;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.event.DnsZoneCreatedEvent;
import io.leitstand.inventory.event.DnsZoneEvent;
import io.leitstand.inventory.event.DnsZoneRemovedEvent;
import io.leitstand.inventory.service.DnsZoneId;
import io.leitstand.inventory.service.DnsZoneName;
import io.leitstand.inventory.service.DnsZoneSettings;

@RunWith(MockitoJUnitRunner.class)
public class DnsZoneManagerTest {
	
	private static final DnsZoneId ZONE_ID = randomDnsZoneId();
	private static final DnsZoneName ZONE_NAME = dnsZoneName("test.leitstand.io.");

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private Repository repository;
	
	@Mock
	private Messages messages;
	
	@Mock
	private Event<DnsZoneEvent> event;
	
	@InjectMocks
	private DnsZoneManager manager = new DnsZoneManager();
	
	private DnsZone zone;
	
	@Before
	public void initTestEnvironment() {
		zone = new DnsZone(ZONE_ID,ZONE_NAME);
	}
	
	@Test
	public void remove_zone_without_records() {
		when(repository.execute(any(Query.class))).thenReturn(0);
		ArgumentCaptor<DnsZoneRemovedEvent> firedEvent = ArgumentCaptor.forClass(DnsZoneRemovedEvent.class);
		doNothing().when(event).fire(firedEvent.capture());
		ArgumentCaptor<Message> sentMessage = ArgumentCaptor.forClass(Message.class);
		doNothing().when(messages).add(sentMessage.capture());
		
		manager.removeDnsZone(zone);
		
		verify(repository).remove(zone);
		assertEquals(ZONE_ID,firedEvent.getValue().getDnsZoneId());
		assertEquals(ZONE_NAME,firedEvent.getValue().getDnsZoneName());		
		assertEquals(IVT0952I_DNS_ZONE_REMOVED.getReasonCode(),sentMessage.getValue().getReason());
	}
	
	@Test
	public void force_remove_zone_with_records() {
		when(repository.execute(any(Query.class))).thenReturn(1);
		ArgumentCaptor<DnsZoneRemovedEvent> firedEvent = ArgumentCaptor.forClass(DnsZoneRemovedEvent.class);
		doNothing().when(event).fire(firedEvent.capture());
		ArgumentCaptor<Message> sentMessage = ArgumentCaptor.forClass(Message.class);
		doNothing().when(messages).add(sentMessage.capture());
		
		manager.forceRemoveDnsZone(zone);
		
		verify(repository).remove(zone);
		assertEquals(ZONE_ID,firedEvent.getValue().getDnsZoneId());
		assertEquals(ZONE_NAME,firedEvent.getValue().getDnsZoneName());		
		assertEquals(IVT0952I_DNS_ZONE_REMOVED.getReasonCode(),sentMessage.getValue().getReason());
		
	}
	
	@Test
	public void force_remove_zone_without_records() {
		when(repository.execute(any(Query.class))).thenReturn(0);
		ArgumentCaptor<DnsZoneRemovedEvent> firedEvent = ArgumentCaptor.forClass(DnsZoneRemovedEvent.class);
		doNothing().when(event).fire(firedEvent.capture());
		ArgumentCaptor<Message> sentMessage = ArgumentCaptor.forClass(Message.class);
		doNothing().when(messages).add(sentMessage.capture());
		
		manager.forceRemoveDnsZone(zone);
		
		verify(repository).remove(zone);
		assertEquals(ZONE_ID,firedEvent.getValue().getDnsZoneId());
		assertEquals(ZONE_NAME,firedEvent.getValue().getDnsZoneName());		
		assertEquals(IVT0952I_DNS_ZONE_REMOVED.getReasonCode(),sentMessage.getValue().getReason());

	}
	
	@Test
	public void create_new_zone() {
		when(repository.execute(any(Query.class))).thenReturn(0);
		ArgumentCaptor<DnsZoneCreatedEvent> firedEvent = ArgumentCaptor.forClass(DnsZoneCreatedEvent.class);
		doNothing().when(event).fire(firedEvent.capture());
		ArgumentCaptor<Message> sentMessage = ArgumentCaptor.forClass(Message.class);
		doNothing().when(messages).add(sentMessage.capture());
		ArgumentCaptor<DnsZone> createdZone = ArgumentCaptor.forClass(DnsZone.class);
		doNothing().when(repository).add(createdZone.capture());
	
		manager.createDnsZone(newDnsZoneSettings()
							  .withDnsZoneId(ZONE_ID)
							  .withDnsZoneName(ZONE_NAME)
							  .withDescription("description")
							  .build());
	
		assertEquals(ZONE_ID,createdZone.getValue().getDnsZoneId());
		assertEquals(ZONE_NAME,createdZone.getValue().getDnsZoneName());		
		assertEquals("description",createdZone.getValue().getDescription());
		assertEquals(ZONE_ID,firedEvent.getValue().getDnsZoneId());
		assertEquals(ZONE_NAME,firedEvent.getValue().getDnsZoneName());		
		assertEquals(IVT0951I_DNS_ZONE_STORED.getReasonCode(),sentMessage.getValue().getReason());

	}
	
	@Test
	public void store_zone() {
		when(repository.execute(any(Query.class))).thenReturn(0);
		ArgumentCaptor<Message> sentMessage = ArgumentCaptor.forClass(Message.class);
		doNothing().when(messages).add(sentMessage.capture());
		
		DnsZoneSettings settings = newDnsZoneSettings()
				  				   .withDnsZoneId(ZONE_ID)
				  				   .withDnsZoneName(ZONE_NAME)
				  				   .withDescription("description")
				  				   .build();
		manager.storeDnsZoneSettings(zone,settings);
		
		assertEquals(IVT0951I_DNS_ZONE_STORED.getReasonCode(),sentMessage.getValue().getReason());
		verify(event,never()).fire(any(DnsZoneCreatedEvent.class));
	}
	
	@Test
	public void cannot_rename_zone() {
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));
		DnsZoneSettings settings = newDnsZoneSettings()
				  				   .withDnsZoneId(ZONE_ID)
				  				   .withDnsZoneName(dnsZoneName("renamed.leitstand.io."))
				  				   .withDescription("description")
				  				   .build();
		manager.storeDnsZoneSettings(zone,settings);
	}
	
	
}

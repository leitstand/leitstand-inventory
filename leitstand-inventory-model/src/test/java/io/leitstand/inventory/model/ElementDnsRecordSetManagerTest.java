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

import static io.leitstand.inventory.service.DnsName.dnsName;
import static io.leitstand.inventory.service.DnsRecord.newDnsRecord;
import static io.leitstand.inventory.service.DnsRecordSet.newDnsRecordSet;
import static io.leitstand.inventory.service.DnsRecordSetId.randomDnsRecordSetId;
import static io.leitstand.inventory.service.DnsRecordType.dnsRecordType;
import static io.leitstand.inventory.service.DnsZoneId.randomDnsZoneId;
import static io.leitstand.inventory.service.DnsZoneName.dnsZoneName;
import static io.leitstand.inventory.service.ElementAlias.elementAlias;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.ReasonCode.IVT3001E_ELEMENT_DNS_RECORD_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT3002I_ELEMENT_DNS_RECORD_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT3003I_ELEMENT_DNS_RECORD_OWNED_BY_OTHER_ELEMENT;
import static io.leitstand.inventory.service.ReasonCode.IVT3003I_ELEMENT_DNS_RECORD_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT3004E_ELEMENT_DNS_RECORD_ZONE_MISMATCH;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
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

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Message;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.event.ElementDnsRecordSetModifiedEvent;
import io.leitstand.inventory.service.DnsName;
import io.leitstand.inventory.service.DnsRecordSet;
import io.leitstand.inventory.service.DnsRecordSetId;
import io.leitstand.inventory.service.DnsZoneId;
import io.leitstand.inventory.service.DnsZoneName;
import io.leitstand.inventory.service.ElementAlias;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.testing.ut.LeitstandCoreMatchers;

@RunWith(MockitoJUnitRunner.class)
public class ElementDnsRecordSetManagerTest {

	private static final DnsZoneId ZONE_ID = randomDnsZoneId();
	private static final DnsZoneName ZONE_NAME = dnsZoneName("leitstand.io");
	private static final DnsRecordSetId RECORDSET_ID = randomDnsRecordSetId();
	private static final DnsName DNS_NAME = DnsName.valueOf("test.leitstand.io");
	private static final ElementGroupId GROUP_ID = randomGroupId();
	private static final ElementGroupType GROUP_TYPE = groupType("unittest");
	private static final ElementGroupName GROUP_NAME = groupName("group");
	private static final ElementRoleName ROLE_NAME = elementRoleName("role");
	private static final ElementId ELEMENT_ID = randomElementId();
	private static final ElementName ELEMENT_NAME = elementName("element");
	private static final ElementAlias ELEMENT_ALIAS = elementAlias("alias");
	

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private Element element;
	
	@Mock
	private Repository repository;
	
	@Mock
	private Event<ElementDnsRecordSetModifiedEvent> event;
	
	@Mock
	private Messages messages;
	
	@Mock
	private DnsZoneProvider zones;
	
	@InjectMocks
	private ElementDnsRecordSetManager manager = new ElementDnsRecordSetManager();
	
	private DnsZone zone;
	
	@Before
	public void initTestEnvironment() {
		when(element.getGroupId()).thenReturn(GROUP_ID);
		when(element.getGroupType()).thenReturn(GROUP_TYPE);
		when(element.getGroupName()).thenReturn(GROUP_NAME);
		when(element.getElementRoleName()).thenReturn(ROLE_NAME);
		when(element.getElementId()).thenReturn(ELEMENT_ID);
		when(element.getElementName()).thenReturn(ELEMENT_NAME);
		when(element.getElementAlias()).thenReturn(ELEMENT_ALIAS);
		this.zone = new DnsZone(ZONE_ID, ZONE_NAME);
		when(zones.fetchDnsZone(ZONE_ID)).thenReturn(zone);
		when(zones.fetchDnsZone(ZONE_NAME)).thenReturn(zone);
		
	}
	
	
	@Test
	public void throws_EntityNotFoundException_when_recordset_id_is_unknown() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(LeitstandCoreMatchers.reason(IVT3001E_ELEMENT_DNS_RECORD_NOT_FOUND));
		
		manager.getElementDnsRecordSet(RECORDSET_ID);
	}
	
	@Test
	public void throws_EntityNotFoundException_when_recordset_name_is_unknown() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(LeitstandCoreMatchers.reason(IVT3001E_ELEMENT_DNS_RECORD_NOT_FOUND));
		
		manager.getElementDnsRecordSet(element, dnsName("unknown"), dnsRecordType("A"));
	}
	
	@Test
	public void add_dns_record() {
		ArgumentCaptor<ElementDnsRecordSetModifiedEvent> firedEvent = ArgumentCaptor.forClass(ElementDnsRecordSetModifiedEvent.class);
		doNothing().when(event).fire(firedEvent.capture());
		ArgumentCaptor<Message> sentMessage = ArgumentCaptor.forClass(Message.class);
		doNothing().when(messages).add(sentMessage.capture());
		ArgumentCaptor<Element_DnsRecordSet> storedRecord = ArgumentCaptor.forClass(Element_DnsRecordSet.class);
		doNothing().when(repository).add(storedRecord.capture());
		
		DnsRecordSet record = newDnsRecordSet()
							  .withDnsZoneId(ZONE_ID)
							  .withDnsZoneName(ZONE_NAME)
							  .withDnsRecordSetId(RECORDSET_ID)
							  .withDnsName(DNS_NAME)
							  .withDnsRecordType(dnsRecordType("A"))
							  .withDescription("Dummy DNS value")
							  .withDnsRecordTimeToLive(3600)
							  .withDnsRecords(newDnsRecord()
									  		  .withRecordValue("127.0.0.1"),
									  		  newDnsRecord()
									  		  .withRecordValue("127.0.0.2")
									  		  .withDisabled(true)
									  		  .withSetPtr(true))
							  .build();
		
		boolean created = manager.storeElementDnsRecordSet(element, record);
		assertTrue(created);

		assertEquals(record.getDnsZoneId(),storedRecord.getValue().getDnsZoneId());
		assertEquals(record.getDnsZoneName(),storedRecord.getValue().getDnsZoneName());
		assertEquals(record.getDnsRecordSetId(),storedRecord.getValue().getDnsRecordSetId());
		assertEquals(record.getDnsName(),storedRecord.getValue().getDnsName());
		assertEquals(record.getDescription(),storedRecord.getValue().getDescription());
		assertEquals(record.getDnsRecords(),storedRecord.getValue().getDnsRecords());

		assertEquals(ZONE_ID,firedEvent.getValue().getDnsRecordSetUpdate().getDnsZoneId());
		assertEquals(ZONE_NAME,firedEvent.getValue().getDnsRecordSetUpdate().getDnsZoneName());
		assertEquals(GROUP_ID,firedEvent.getValue().getGroupId());
		assertEquals(GROUP_NAME,firedEvent.getValue().getGroupName());
		assertEquals(GROUP_TYPE,firedEvent.getValue().getGroupType());
		assertEquals(ROLE_NAME,firedEvent.getValue().getElementRole());
		assertEquals(ELEMENT_ID,firedEvent.getValue().getElementId());
		assertEquals(ELEMENT_NAME,firedEvent.getValue().getElementName());
		assertEquals(ELEMENT_ALIAS,firedEvent.getValue().getElementAlias());
		assertEquals(record.getDnsName(),firedEvent.getValue().getDnsRecordSetUpdate().getDnsName());
		assertEquals(record.getDescription(),firedEvent.getValue().getDnsRecordSetUpdate().getDescription());
		assertEquals(record.getDnsRecords(),firedEvent.getValue().getDnsRecordSetUpdate().getDnsRecords());
		assertEquals(record.getDnsRecordSetId(),firedEvent.getValue().getDnsRecordSetUpdate().getDnsRecordSetId());

		assertEquals(IVT3002I_ELEMENT_DNS_RECORD_STORED.getReasonCode(),sentMessage.getValue().getReason());
	}
	
	@Test
	public void rename_dns_record() {
		ArgumentCaptor<ElementDnsRecordSetModifiedEvent> firedEvent = ArgumentCaptor.forClass(ElementDnsRecordSetModifiedEvent.class);
		doNothing().when(event).fire(firedEvent.capture());
		ArgumentCaptor<Message> sentMessage = ArgumentCaptor.forClass(Message.class);
		doNothing().when(messages).add(sentMessage.capture());
		DnsRecordSet record = newDnsRecordSet()
				  			  .withDnsZoneId(ZONE_ID)
				  			  .withDnsZoneName(ZONE_NAME)
				  			  .withDnsRecordSetId(RECORDSET_ID)
				  			  .withDnsName(DNS_NAME)
				  			  .withDnsRecordType(dnsRecordType("A"))
				  			  .withDnsRecordTimeToLive(3600)
				  			  .withDnsRecords(newDnsRecord()
				  					  		 .withRecordValue("127.0.0.1"))
				  			  .build();
		
		Element_DnsRecordSet _record = new Element_DnsRecordSet(zone,
																element,
																record.getDnsRecordSetId(),
																record.getDnsName());
		_record.setDnsRecords(record.getDnsRecords());
		when(repository.execute(any(Query.class))).thenReturn(_record);
		
		DnsRecordSet renamed = newDnsRecordSet()
							   .withDnsZoneId(ZONE_ID)
							   .withDnsZoneName(ZONE_NAME)
							   .withDnsRecordSetId(record.getDnsRecordSetId())
							   .withDnsName(dnsName("renamed.leitstand.io"))
							   .withDescription("Dummy DNS value")
							   .withDnsRecordType(dnsRecordType("A"))
					  		   .withDnsRecordTimeToLive(3600)
							   .withDnsRecords(newDnsRecord()
									  		   .withRecordValue("127.0.0.1"))
							   .build();
		
		verify(repository,never()).add(_record);
		
		boolean created = manager.storeElementDnsRecordSet(element, renamed);
		assertFalse(created);
		ElementDnsRecordSetModifiedEvent storedEvent = firedEvent.getValue();
		
		assertEquals(ZONE_ID,storedEvent.getDnsRecordSetUpdate().getDnsZoneId());
		assertEquals(ZONE_NAME,storedEvent.getDnsRecordSetUpdate().getDnsZoneName());
		assertEquals(GROUP_ID,storedEvent.getGroupId());
		assertEquals(GROUP_NAME,storedEvent.getGroupName());
		assertEquals(GROUP_TYPE,storedEvent.getGroupType());
		assertEquals(ROLE_NAME,storedEvent.getElementRole());
		assertEquals(ELEMENT_ID,storedEvent.getElementId());
		assertEquals(ELEMENT_NAME,storedEvent.getElementName());
		assertEquals(ELEMENT_ALIAS,storedEvent.getElementAlias());
		assertEquals(renamed.getDnsName() ,storedEvent.getDnsRecordSetUpdate().getDnsName());
		assertEquals(DNS_NAME ,storedEvent.getDnsRecordSetUpdate().getDnsWithdrawnName());
		
		
		assertEquals(IVT3002I_ELEMENT_DNS_RECORD_STORED.getReasonCode(),sentMessage.getValue().getReason());
		
	}
	
	@Test
	public void update_dns_record() {
		ArgumentCaptor<ElementDnsRecordSetModifiedEvent> firedEvent = ArgumentCaptor.forClass(ElementDnsRecordSetModifiedEvent.class);
		doNothing().when(event).fire(firedEvent.capture());
		ArgumentCaptor<Message> sentMessage = ArgumentCaptor.forClass(Message.class);
		doNothing().when(messages).add(sentMessage.capture());

		DnsRecordSet record = newDnsRecordSet()
							  .withDnsZoneId(ZONE_ID)
							  .withDnsZoneName(ZONE_NAME)
							  .withDnsRecordSetId(RECORDSET_ID)
							  .withDnsName(DNS_NAME)
							  .withDnsRecordType(dnsRecordType("A"))
							  .withDnsRecordTimeToLive(3600)
							  .withDnsRecords(newDnsRecord()
									  		  .withRecordValue("127.0.0.1"))
							  .build();

		Element_DnsRecordSet _record = new Element_DnsRecordSet(zone,
																element, 
																RECORDSET_ID, 
																DNS_NAME);
		_record.setDnsRecords(record.getDnsRecords());
		when(repository.execute(any(Query.class))).thenReturn(_record);
		
		
		verify(repository,never()).add(_record);
		
		boolean created = manager.storeElementDnsRecordSet(element, record);
		assertFalse(created);
		assertEquals(ZONE_ID,firedEvent.getValue().getDnsRecordSetUpdate().getDnsZoneId());
		assertEquals(ZONE_NAME,firedEvent.getValue().getDnsRecordSetUpdate().getDnsZoneName());
		assertEquals(GROUP_ID,firedEvent.getValue().getGroupId());
		assertEquals(GROUP_NAME,firedEvent.getValue().getGroupName());
		assertEquals(GROUP_TYPE,firedEvent.getValue().getGroupType());
		assertEquals(ROLE_NAME,firedEvent.getValue().getElementRole());
		assertEquals(ELEMENT_ID,firedEvent.getValue().getElementId());
		assertEquals(ELEMENT_NAME,firedEvent.getValue().getElementName());
		assertEquals(ELEMENT_ALIAS,firedEvent.getValue().getElementAlias());
		assertEquals(record.getDnsName(),firedEvent.getValue().getDnsRecordSetUpdate().getDnsName());
		assertEquals(record.getDnsType(),firedEvent.getValue().getDnsRecordSetUpdate().getDnsType());
		assertEquals(record.getDescription(),firedEvent.getValue().getDnsRecordSetUpdate().getDescription());
		assertEquals(record.getDnsRecords(),firedEvent.getValue().getDnsRecordSetUpdate().getDnsRecords());
		assertEquals(record.getDnsRecordSetId(),firedEvent.getValue().getDnsRecordSetUpdate().getDnsRecordSetId());
		assertEquals(IVT3002I_ELEMENT_DNS_RECORD_STORED.getReasonCode(),sentMessage.getValue().getReason());	
	}

	@Test
	public void remove_dns_record_identified_by_name_type_tuple() {
		ArgumentCaptor<ElementDnsRecordSetModifiedEvent> firedEvent = ArgumentCaptor.forClass(ElementDnsRecordSetModifiedEvent.class);
		doNothing().when(event).fire(firedEvent.capture());
		ArgumentCaptor<Message> sentMessage = ArgumentCaptor.forClass(Message.class);
		doNothing().when(messages).add(sentMessage.capture());

		DnsRecordSet record = newDnsRecordSet()
							  .withDnsZoneId(ZONE_ID)
							  .withDnsZoneName(ZONE_NAME)
							  .withDnsRecordSetId(RECORDSET_ID)
							  .withDnsName(DNS_NAME)
							  .withDnsRecordType(dnsRecordType("A"))
					  		  .withDnsRecordTimeToLive(3600)
							  .withDnsRecords(newDnsRecord()
									  		  .withRecordValue("127.0.0.1"))
							  .build();

		Element_DnsRecordSet _record = new Element_DnsRecordSet(zone,
																element, 
																RECORDSET_ID, 
																DNS_NAME);
		_record.setDnsRecords(record.getDnsRecords());
		_record.setDnsRecordType(record.getDnsType());
		when(repository.execute(any(Query.class))).thenReturn(_record);
		
		
		manager.removeElementDnsRecordSet(element,DNS_NAME,dnsRecordType("A"));
		assertEquals(ZONE_ID,firedEvent.getValue().getDnsRecordSetUpdate().getDnsZoneId());
		assertEquals(ZONE_NAME,firedEvent.getValue().getDnsRecordSetUpdate().getDnsZoneName());
		assertEquals(GROUP_ID,firedEvent.getValue().getGroupId());
		assertEquals(GROUP_NAME,firedEvent.getValue().getGroupName());
		assertEquals(GROUP_TYPE,firedEvent.getValue().getGroupType());
		assertEquals(ROLE_NAME,firedEvent.getValue().getElementRole());
		assertEquals(ELEMENT_ID,firedEvent.getValue().getElementId());
		assertEquals(ELEMENT_NAME,firedEvent.getValue().getElementName());
		assertEquals(ELEMENT_ALIAS,firedEvent.getValue().getElementAlias());
		assertNull(firedEvent.getValue().getDnsRecordSetUpdate().getDnsName());
		assertEquals(record.getDnsName(),firedEvent.getValue().getDnsRecordSetUpdate().getDnsWithdrawnName());
		assertEquals(record.getDnsType(),firedEvent.getValue().getDnsRecordSetUpdate().getDnsType());
		assertEquals(record.getDescription(),firedEvent.getValue().getDnsRecordSetUpdate().getDescription());
		assertEquals(record.getDnsRecords(),firedEvent.getValue().getDnsRecordSetUpdate().getDnsRecords());
		assertEquals(record.getDnsRecordSetId(),firedEvent.getValue().getDnsRecordSetUpdate().getDnsRecordSetId());
		assertEquals(IVT3003I_ELEMENT_DNS_RECORD_REMOVED.getReasonCode(),sentMessage.getValue().getReason());	
		
	}
	
	
	@Test
	public void remove_dns_record_identified_by_id() {
		ArgumentCaptor<ElementDnsRecordSetModifiedEvent> firedEvent = ArgumentCaptor.forClass(ElementDnsRecordSetModifiedEvent.class);
		doNothing().when(event).fire(firedEvent.capture());
		ArgumentCaptor<Message> sentMessage = ArgumentCaptor.forClass(Message.class);
		doNothing().when(messages).add(sentMessage.capture());

		DnsRecordSet record = newDnsRecordSet()
							  .withDnsZoneId(ZONE_ID)
							  .withDnsZoneName(ZONE_NAME)
							  .withDnsRecordSetId(RECORDSET_ID)
							  .withDnsName(DNS_NAME)
							  .withDnsRecordType(dnsRecordType("A"))
					  		  .withDnsRecordTimeToLive(3600)
							  .withDnsRecords(newDnsRecord()
									  		  .withRecordValue("127.0.0.1"))
							  .build();

		Element_DnsRecordSet _record = new Element_DnsRecordSet(zone,
																element, 
																RECORDSET_ID, 
																DNS_NAME);
		_record.setDnsRecords(record.getDnsRecords());
		_record.setDnsRecordType(record.getDnsType());
		when(repository.execute(any(Query.class))).thenReturn(_record);
		
		
		manager.removeElementDnsRecordSet(element,RECORDSET_ID);
		assertEquals(ZONE_ID,firedEvent.getValue().getDnsRecordSetUpdate().getDnsZoneId());
		assertEquals(ZONE_NAME,firedEvent.getValue().getDnsRecordSetUpdate().getDnsZoneName());
		assertEquals(GROUP_ID,firedEvent.getValue().getGroupId());
		assertEquals(GROUP_NAME,firedEvent.getValue().getGroupName());
		assertEquals(GROUP_TYPE,firedEvent.getValue().getGroupType());
		assertEquals(ROLE_NAME,firedEvent.getValue().getElementRole());
		assertEquals(ELEMENT_ID,firedEvent.getValue().getElementId());
		assertEquals(ELEMENT_NAME,firedEvent.getValue().getElementName());
		assertEquals(ELEMENT_ALIAS,firedEvent.getValue().getElementAlias());
		assertNull(firedEvent.getValue().getDnsRecordSetUpdate().getDnsName());
		assertEquals(record.getDnsName(),firedEvent.getValue().getDnsRecordSetUpdate().getDnsWithdrawnName());
		assertEquals(record.getDnsType(),firedEvent.getValue().getDnsRecordSetUpdate().getDnsType());
		assertEquals(record.getDescription(),firedEvent.getValue().getDnsRecordSetUpdate().getDescription());
		assertEquals(record.getDnsRecords(),firedEvent.getValue().getDnsRecordSetUpdate().getDnsRecords());
		assertEquals(record.getDnsRecordSetId(),firedEvent.getValue().getDnsRecordSetUpdate().getDnsRecordSetId());
		assertEquals(IVT3003I_ELEMENT_DNS_RECORD_REMOVED.getReasonCode(),sentMessage.getValue().getReason());	
		
		
	}
	
	@Test
	public void do_nothing_when_removing_unknown_dns_record() {
		manager.removeElementDnsRecordSet(element, DNS_NAME, dnsRecordType("A"));
		
		verify(repository,never()).remove(any());
		verifyZeroInteractions(event,messages);
	}
	
	@Test
	public void move_dns_records_to_other_element() {
		DnsRecordSet record = newDnsRecordSet()
							  .withDnsZoneId(ZONE_ID)
							  .withDnsZoneName(ZONE_NAME)
							  .withDnsRecordSetId(RECORDSET_ID)
							  .withDnsName(DNS_NAME)
							  .withDnsRecordType(dnsRecordType("A"))
					  		  .withDnsRecordTimeToLive(3600)
							  .withDnsRecords(newDnsRecord()
									  		  .withRecordValue("127.0.0.1"))
							  .build();

		// Mock record with different owner.
		Element owner = mock(Element.class);
		Element_DnsRecordSet _record = mock(Element_DnsRecordSet.class);
		when(_record.getElement()).thenReturn(owner);
		when(repository.execute(any(Query.class))).thenReturn(_record);
		
		boolean created = manager.storeElementDnsRecordSet(element, record);
		assertTrue(created); // It is a new sub-resource that has been created by moving it to another owning element.

		verify(_record).setElement(element); // Verify owner is updated
		verify(repository,never()).add(_record);

	}
	
	@Test
	public void report_conflict_when_attempting_to_asign_record_to_wrong_zone() {
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT3004E_ELEMENT_DNS_RECORD_ZONE_MISMATCH));
		
		DnsRecordSet record = newDnsRecordSet()
				  			  .withDnsZoneId(ZONE_ID)
				  			  .withDnsZoneName(ZONE_NAME)
				  			  .withDnsRecordSetId(RECORDSET_ID)
				  			  .withDnsName(dnsName("foo.bar"))
				  			  .withDnsRecordType(dnsRecordType("A"))
				  			  .withDnsRecordTimeToLive(3600)
				  			  .withDnsRecords(newDnsRecord()
				  			  .withRecordValue("127.0.0.1"))
				  			  .build();
		
		manager.storeElementDnsRecordSet(element, record);
	}
	
	@Test
	public void report_conflict_when_removing_a_dns_record_identified_by_id_for_the_wrong_element() {
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT3003I_ELEMENT_DNS_RECORD_OWNED_BY_OTHER_ELEMENT));
		Element owner = mock(Element.class);
		Element_DnsRecordSet record = mock(Element_DnsRecordSet.class);
		when(record.getElement()).thenReturn(owner);
		when(repository.execute(any(Query.class))).thenReturn(record);
		
		manager.removeElementDnsRecordSet(element, RECORDSET_ID);
	}
	
	@Test
	public void do_nothing_when_removing_an_unknown_dns_record_identified_by_id() {
		manager.removeElementDnsRecordSet(element, RECORDSET_ID);
		verify(repository,never()).remove(any(Element_DnsRecordSet.class));
		verify(event,never()).fire(any(ElementDnsRecordSetModifiedEvent.class));

	}
	
}

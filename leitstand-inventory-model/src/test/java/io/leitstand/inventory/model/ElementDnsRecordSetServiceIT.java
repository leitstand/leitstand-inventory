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
import static io.leitstand.inventory.model.Element.findElementById;
import static io.leitstand.inventory.model.ElementGroup.findElementGroupById;
import static io.leitstand.inventory.model.ElementRole.findRoleByName;
import static io.leitstand.inventory.service.DnsName.dnsName;
import static io.leitstand.inventory.service.DnsRecord.newDnsRecord;
import static io.leitstand.inventory.service.DnsRecordSet.newDnsRecordSet;
import static io.leitstand.inventory.service.DnsRecordSetId.randomDnsRecordSetId;
import static io.leitstand.inventory.service.DnsRecordType.dnsRecordType;
import static io.leitstand.inventory.service.DnsZoneId.randomDnsZoneId;
import static io.leitstand.inventory.service.DnsZoneName.dnsZoneName;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.Plane.DATA;
import static io.leitstand.inventory.service.ReasonCode.IVT0950E_DNS_ZONE_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT3001E_ELEMENT_DNS_RECORD_NOT_FOUND;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.LinkedList;
import java.util.List;

import javax.enterprise.event.Event;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.DnsName;
import io.leitstand.inventory.service.DnsRecordSet;
import io.leitstand.inventory.service.DnsRecordSetId;
import io.leitstand.inventory.service.DnsRecordType;
import io.leitstand.inventory.service.DnsZoneId;
import io.leitstand.inventory.service.DnsZoneName;
import io.leitstand.inventory.service.ElementDnsRecordSet;
import io.leitstand.inventory.service.ElementDnsRecordSetService;
import io.leitstand.inventory.service.ElementDnsRecordSets;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementRoleName;

public class ElementDnsRecordSetServiceIT extends InventoryIT{
	
	private static final ElementGroupId GROUP_ID = randomGroupId();
	private static final ElementGroupType GROUP_TYPE = groupType("unittest");
	private static final ElementGroupName GROUP_NAME = groupName(ElementDnsRecordSetServiceIT.class.getSimpleName());
	private static final ElementRoleName ROLE_NAME = elementRoleName(ElementDnsRecordSetServiceIT.class.getSimpleName());
	private static final ElementId ELEMENT_ID = randomElementId();
	private static final ElementName ELEMENT_NAME = elementName(ElementDnsRecordSetServiceIT.class.getName());
	private static final DnsRecordSetId DNS_ID = randomDnsRecordSetId();
	private static final DnsRecordType DNS_TYPE = dnsRecordType("A");
	private static final DnsZoneId ZONE_ID = randomDnsZoneId();
	private static final DnsZoneName ZONE_NAME = dnsZoneName(ElementDnsRecordSetServiceIT.class.getSimpleName());
	private static final DnsName DNS_NAME = dnsName("test.leitstand.io."+ZONE_NAME);
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	private ElementDnsRecordSetService service;
	private Repository repository;
	
	@Before
	public void initTestEnvironment() {
		this.repository = new Repository(getEntityManager());
		ElementProvider elements = new ElementProvider(repository);
		DnsZoneProvider zones = new DnsZoneProvider(repository);
		ElementDnsRecordSetManager manager = new ElementDnsRecordSetManager(zones,
																			repository, 
																			mock(Event.class), 
																			mock(Messages.class));
		this.service = new DefaultElementDnsRecordSetService(elements, manager);
		
		transaction(() -> {
			ElementRole role = repository.addIfAbsent(findRoleByName(ROLE_NAME), 
													  () -> new ElementRole(ROLE_NAME,DATA));
			
			ElementGroup group = repository.addIfAbsent(findElementGroupById(GROUP_ID),
														() -> new ElementGroup(GROUP_ID, GROUP_TYPE, GROUP_NAME));
			
			repository.addIfAbsent(findElementById(ELEMENT_ID), 
								   () -> new Element(group,role,ELEMENT_ID, ELEMENT_NAME));
			
			repository.addIfAbsent(findDnsZoneById(ZONE_ID), 
								   () -> new DnsZone(ZONE_ID,ZONE_NAME));
		});
		
	}
	
	@Test
	public void add_dns_record_set_for_element_identified_by_id() {
		DnsRecordSet record = newDnsRecordSet()
							  .withDnsZoneId(ZONE_ID)
							  .withDnsZoneName(ZONE_NAME)
							  .withDnsRecordSetId(DNS_ID)
							  .withDnsName(DNS_NAME)
							  .withDescription("description")
							  .withDnsRecordType(DNS_TYPE)
							  .withDnsRecords(newDnsRecord()
									  		  .withRecordValue("10.0.0.1"),
									  		  newDnsRecord()
									  		  .withRecordValue("10.0.0.2"))
							  .build();
		
		transaction(() -> {
			boolean created = service.storeElementDnsRecordSet(ELEMENT_ID, record);
			assertTrue(created);
		});
		
		transaction(() -> {
			ElementDnsRecordSet reloaded = service.getElementDnsRecordSet(ELEMENT_ID, DNS_NAME,DNS_TYPE);
			
			assertEquals(GROUP_ID,reloaded.getGroupId());
			assertEquals(GROUP_TYPE,reloaded.getGroupType());
			assertEquals(GROUP_NAME,reloaded.getGroupName());
			assertEquals(ROLE_NAME,reloaded.getElementRole());
			assertEquals(ELEMENT_ID,reloaded.getElementId());
			assertEquals(ELEMENT_NAME,reloaded.getElementName());
			assertEquals(record,reloaded.getDnsRecordSet());
		});
		
	}
	
	@Test
	public void add_dns_record_set_for_element_identified_by_name() {
		DnsRecordSet record = newDnsRecordSet()
							  .withDnsZoneId(ZONE_ID)
							  .withDnsZoneName(ZONE_NAME)
							  .withDnsRecordSetId(DNS_ID)
							  .withDnsName(DNS_NAME)
							  .withDescription("description")
							  .withDnsRecordType(DNS_TYPE)
							  .withDnsRecords(newDnsRecord()
									  		  .withRecordValue("10.0.0.1"),
									  		  newDnsRecord()
									  		  .withRecordValue("10.0.0.2"))
							  .build();
		
		transaction(() -> {
			boolean created = service.storeElementDnsRecordSet(ELEMENT_NAME, record);
			assertTrue(created);
		});
		
		transaction(() -> {
			ElementDnsRecordSet reloaded = service.getElementDnsRecordSet(ELEMENT_NAME, DNS_NAME,DNS_TYPE);
			
			assertEquals(GROUP_ID,reloaded.getGroupId());
			assertEquals(GROUP_TYPE,reloaded.getGroupType());
			assertEquals(GROUP_NAME,reloaded.getGroupName());
			assertEquals(ROLE_NAME,reloaded.getElementRole());
			assertEquals(ELEMENT_ID,reloaded.getElementId());
			assertEquals(ELEMENT_NAME,reloaded.getElementName());
			assertEquals(record,reloaded.getDnsRecordSet());
		});
		
	}
	
	@Test
	public void update_dns_record_set_for_element_identified_by_id() {
		
		
		transaction(() -> {
			DnsRecordSet record = newDnsRecordSet()
								  .withDnsZoneId(ZONE_ID)
								  .withDnsZoneName(ZONE_NAME)
								  .withDnsRecordSetId(DNS_ID)
								  .withDnsName(DNS_NAME)
								  .withDescription("description")
								  .withDnsRecordType(DNS_TYPE)
								  .withDnsRecords(newDnsRecord()
										  		  .withRecordValue("10.0.0.1"),
										  		  newDnsRecord()
										  		  .withRecordValue("10.0.0.2"))
								  .build();
			boolean created = service.storeElementDnsRecordSet(ELEMENT_ID, record);
			assertTrue(created);
		});
		
		DnsRecordSet record = newDnsRecordSet()
							  .withDnsZoneId(ZONE_ID)
							  .withDnsZoneName(ZONE_NAME)
							  .withDnsRecordSetId(DNS_ID)
							  .withDnsName(DNS_NAME)
							  .withDescription("description")
							  .withDnsRecordType(DNS_TYPE)
							  .withDnsRecords(newDnsRecord()
									  		  .withRecordValue("10.0.0.1"))
							  .build();
		transaction(() -> {
			boolean created = service.storeElementDnsRecordSet(ELEMENT_ID, record);
			assertFalse(created);
		});
		
		transaction(() -> {
			ElementDnsRecordSet reloaded = service.getElementDnsRecordSet(ELEMENT_ID, DNS_NAME, DNS_TYPE);
			
			assertEquals(GROUP_ID,reloaded.getGroupId());
			assertEquals(GROUP_TYPE,reloaded.getGroupType());
			assertEquals(GROUP_NAME,reloaded.getGroupName());
			assertEquals(ROLE_NAME,reloaded.getElementRole());
			assertEquals(ELEMENT_ID,reloaded.getElementId());
			assertEquals(ELEMENT_NAME,reloaded.getElementName());
			assertEquals(record,reloaded.getDnsRecordSet());
		});
		
	}
	
	@Test
	public void update_dns_record_set_for_element_identified_by_name() {
		
		
		transaction(() -> {
			DnsRecordSet record = newDnsRecordSet()
								  .withDnsZoneId(ZONE_ID)
								  .withDnsZoneName(ZONE_NAME)
								  .withDnsRecordSetId(DNS_ID)
								  .withDnsName(DNS_NAME)
								  .withDescription("description")
								  .withDnsRecordType(DNS_TYPE)
								  .withDnsRecords(newDnsRecord()
										  		  .withRecordValue("10.0.0.1"),
										  		  newDnsRecord()
										  		  .withRecordValue("10.0.02"))
								  .build();
			boolean created = service.storeElementDnsRecordSet(ELEMENT_NAME, record);
			assertTrue(created);
		});
		
		DnsRecordSet record = newDnsRecordSet()
							  .withDnsZoneId(ZONE_ID)
							  .withDnsZoneName(ZONE_NAME)
							  .withDnsRecordSetId(DNS_ID)
							  .withDnsName(DNS_NAME)
							  .withDescription("description")
							  .withDnsRecordType(DNS_TYPE)
							  .withDnsRecords(newDnsRecord()
									  		  .withRecordValue("10.0.0.1"))
							  .build();
		transaction(() -> {
			boolean created = service.storeElementDnsRecordSet(ELEMENT_NAME, record);
			assertFalse(created);
		});
		
		transaction(() -> {
			ElementDnsRecordSet reloaded = service.getElementDnsRecordSet(ELEMENT_NAME, DNS_NAME, DNS_TYPE);
			
			assertEquals(GROUP_ID,reloaded.getGroupId());
			assertEquals(GROUP_TYPE,reloaded.getGroupType());
			assertEquals(GROUP_NAME,reloaded.getGroupName());
			assertEquals(ROLE_NAME,reloaded.getElementRole());
			assertEquals(ELEMENT_ID,reloaded.getElementId());
			assertEquals(ELEMENT_NAME,reloaded.getElementName());
			assertEquals(record,reloaded.getDnsRecordSet());
		});
		
	}
	
	@Test
	public void remove_dns_record_set_for_element_identified_by_id() {
			
		transaction(() -> {
			DnsRecordSet record = newDnsRecordSet()
								  .withDnsZoneId(ZONE_ID)
								  .withDnsZoneName(ZONE_NAME)
								  .withDnsRecordSetId(DNS_ID)
								  .withDnsName(DNS_NAME)
								  .withDescription("description")
								  .withDnsRecordType(DNS_TYPE)
								  .withDnsRecords(newDnsRecord()
										  		  .withRecordValue("10.0.0.1"),
										  		  newDnsRecord()
										  		  .withRecordValue("10.0.0.2"))
								  .build();
			boolean created = service.storeElementDnsRecordSet(ELEMENT_ID, record);
			assertTrue(created);
		});
		
		transaction(() -> {
			service.removeElementDnsRecordSet(ELEMENT_ID, DNS_NAME, DNS_TYPE);
		});
		
		transaction(() -> {
			exception.expect(EntityNotFoundException.class);
			exception.expect(reason(IVT3001E_ELEMENT_DNS_RECORD_NOT_FOUND));
			service.getElementDnsRecordSet(ELEMENT_ID, DNS_NAME, DNS_TYPE);
		});
		
	}
	
	@Test
	public void cannot_register_dns_record_for_unknown_zone_for_element_identified_by_id() {
		transaction(()->{
			exception.expect(EntityNotFoundException.class);
			exception.expect(reason(IVT0950E_DNS_ZONE_NOT_FOUND));
			service.storeElementDnsRecordSet(ELEMENT_ID, mock(DnsRecordSet.class));
		});
	}
	
	@Test
	public void cannot_register_dns_record_for_unknown_zone_for_element_identified_by_name() {
		transaction(()->{
			exception.expect(EntityNotFoundException.class);
			exception.expect(reason(IVT0950E_DNS_ZONE_NOT_FOUND));
			service.storeElementDnsRecordSet(ELEMENT_ID, mock(DnsRecordSet.class));
		});
	}
	
	
	@Test
	public void remove_dns_record_set_for_element_identified_by_name() {
			
		transaction(() -> {
			DnsRecordSet record = newDnsRecordSet()
								  .withDnsZoneId(ZONE_ID)
								  .withDnsZoneName(ZONE_NAME)
								  .withDnsRecordSetId(DNS_ID)
								  .withDnsName(DNS_NAME)
								  .withDescription("description")
								  .withDnsRecordType(DNS_TYPE)
								  .withDnsRecords(newDnsRecord()
										  		  .withRecordValue("10.0.0.1"),
										  		  newDnsRecord()
										  		  .withRecordValue("10.0.0.2"))
								  .build();
			boolean created = service.storeElementDnsRecordSet(ELEMENT_ID, record);
			assertTrue(created);
		});
		
		transaction(() -> {
			service.removeElementDnsRecordSet(ELEMENT_NAME, DNS_NAME, DNS_TYPE);
		});
		
		transaction(() -> {
			exception.expect(EntityNotFoundException.class);
			exception.expect(reason(IVT3001E_ELEMENT_DNS_RECORD_NOT_FOUND));
			service.getElementDnsRecordSet(ELEMENT_NAME, DNS_NAME, DNS_TYPE);
		});
		
	}

	
	@Test
	public void read_all_element_dns_record_sets() {
		List<DnsRecordSet> records = new LinkedList<>();
		transaction(() -> {
			DnsRecordSet record = newDnsRecordSet()
								  .withDnsZoneId(ZONE_ID)
								  .withDnsZoneName(ZONE_NAME)
								  .withDnsRecordSetId(DNS_ID)
								  .withDnsName(DNS_NAME)
								  .withDescription("description")
								  .withDnsRecordType(DNS_TYPE)
								  .withDnsRecords(newDnsRecord()
										  		  .withRecordValue("10.0.0.1"),
										  		  newDnsRecord()
										  		  .withRecordValue("10.0.0.2"))
								  .build();
			records.add(record);
			boolean created = service.storeElementDnsRecordSet(ELEMENT_ID, record);
			assertTrue(created);
			
			record = newDnsRecordSet()
					 .withDnsZoneId(ZONE_ID)
					 .withDnsZoneName(ZONE_NAME)
					 .withDnsRecordSetId(randomDnsRecordSetId())
					 .withDnsName(dnsName("foo.leitstand.io."+ZONE_NAME))
					 .withDescription("description")
					 .withDnsRecordType(DNS_TYPE)
					 .withDnsRecords(newDnsRecord()
							  		 .withRecordValue("10.0.0.1"))
					 .build();
			records.add(record);
			created = service.storeElementDnsRecordSet(ELEMENT_ID, record);
			assertTrue(created);
		});
		
		transaction(()->{
			ElementDnsRecordSets recordSets = service.getElementDnsRecordSets(ELEMENT_ID);
			
			assertEquals(GROUP_ID,recordSets.getGroupId());
			assertEquals(GROUP_TYPE,recordSets.getGroupType());
			assertEquals(GROUP_NAME,recordSets.getGroupName());
			assertEquals(ROLE_NAME,recordSets.getElementRole());
			assertEquals(ELEMENT_ID,recordSets.getElementId());
			assertEquals(ELEMENT_NAME,recordSets.getElementName());
			//Verify correct order of returned recordsets
			assertEquals(records.get(0),recordSets.getDnsRecordSets().get(1));
			assertEquals(records.get(1),recordSets.getDnsRecordSets().get(0));
			
			
		});
		
	}
	
	@Test
	public void read_dns_recordset_by_id() {
		DnsRecordSet record = newDnsRecordSet()
							  .withDnsZoneId(ZONE_ID)
							  .withDnsZoneName(ZONE_NAME)
							  .withDnsRecordSetId(DNS_ID)
							  .withDnsName(DNS_NAME)
							  .withDescription("description")
							  .withDnsRecordType(DNS_TYPE)
							  .withDnsRecords(newDnsRecord()
									  		  .withRecordValue("10.0.0.1"),
									  		  newDnsRecord()
									  		  .withRecordValue("10.0.0.2"))
							  .build();
		transaction(() -> {
			boolean created = service.storeElementDnsRecordSet(ELEMENT_ID, record);
			assertTrue(created);
		});
		
		transaction(() -> {
			ElementDnsRecordSet reloaded = service.getElementDnsRecordSet(DNS_ID);
			
			assertEquals(GROUP_ID,reloaded.getGroupId());
			assertEquals(GROUP_TYPE,reloaded.getGroupType());
			assertEquals(GROUP_NAME,reloaded.getGroupName());
			assertEquals(ROLE_NAME,reloaded.getElementRole());
			assertEquals(ELEMENT_ID,reloaded.getElementId());
			assertEquals(ELEMENT_NAME,reloaded.getElementName());
			assertEquals(record,reloaded.getDnsRecordSet());
		});
		
	}
	
	@Test
	public void throws_EntityNotFoundException_when_dns_record_set_id_is_unknown() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT3001E_ELEMENT_DNS_RECORD_NOT_FOUND));
		
		service.getElementDnsRecordSet(randomDnsRecordSetId());
	}
}

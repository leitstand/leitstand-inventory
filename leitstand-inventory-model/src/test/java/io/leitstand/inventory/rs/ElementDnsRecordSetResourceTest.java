/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.rs;

import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.inventory.service.DnsName.dnsName;
import static io.leitstand.inventory.service.DnsRecordSetId.randomDnsRecordSetId;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ReasonCode.IVT3003I_ELEMENT_DNS_RECORD_OWNED_BY_OTHER_ELEMENT;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.DnsName;
import io.leitstand.inventory.service.DnsRecordSet;
import io.leitstand.inventory.service.DnsRecordSetId;
import io.leitstand.inventory.service.ElementDnsRecordSet;
import io.leitstand.inventory.service.ElementDnsRecordSetService;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;

@RunWith(MockitoJUnitRunner.class)
public class ElementDnsRecordSetResourceTest {
	
	private static final ElementId ELEMENT_ID = randomElementId();
	private static final ElementName ELEMENT_NAME = elementName("element");
	private static final DnsRecordSetId DNS_ID = randomDnsRecordSetId();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private ElementDnsRecordSetService service;
	
	@Mock
	private Messages messages;
	
	@InjectMocks
	private ElementDnsRecordSetResource resource = new ElementDnsRecordSetResource();
	
	@Test
	public void throws_ConflictException_when_accessing_dns_records_of_other_element_through_dns_record_set_id_for_element_identified_by_id() {
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT3003I_ELEMENT_DNS_RECORD_OWNED_BY_OTHER_ELEMENT));
		
		ElementDnsRecordSet recordSet = mock(ElementDnsRecordSet.class);
		when(recordSet.getElementId()).thenReturn(randomElementId());
		when(service.getElementDnsRecordSet(DNS_ID)).thenReturn(recordSet);
		
		resource.getElementDnsRecordSet(ELEMENT_ID, DNS_ID);
	}
	
	@Test
	public void throws_ConflictException_when_accessing_dns_records_of_other_element_through_dns_record_set_id_for_an_element_identified_by_name() {
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT3003I_ELEMENT_DNS_RECORD_OWNED_BY_OTHER_ELEMENT));
		
		ElementDnsRecordSet recordSet = mock(ElementDnsRecordSet.class);
		when(recordSet.getElementName()).thenReturn(elementName("owner"));
		when(service.getElementDnsRecordSet(DNS_ID)).thenReturn(recordSet);
		
		resource.getElementDnsRecordSet(ELEMENT_NAME, DNS_ID);
	}
	
	@Test
	public void throws_UnprocessableEntityException_when_attempting_to_modify_the_dns_resourceset_id_for_an_element_identified_by_id() {
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));
		
		DnsRecordSet recordSet = mock(DnsRecordSet.class);
		when(recordSet.getDnsRecordSetId()).thenReturn(DNS_ID);
		
		resource.storeElementDnsRecordSet(ELEMENT_ID, randomDnsRecordSetId(),recordSet);
	}
	
	@Test
	public void throws_UnprocessableEntityException_when_attempting_to_modify_the_dns_resourceset_id_for_an_element_identified_by_name() {
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));
		
		DnsRecordSet recordSet = mock(DnsRecordSet.class);
		when(recordSet.getDnsRecordSetId()).thenReturn(DNS_ID);
		
		resource.storeElementDnsRecordSet(ELEMENT_NAME, randomDnsRecordSetId(),recordSet);
	}
	
	@Test
	public void send_created_response_when_putting_a_new_dns_record_for_an_element_identified_by_id() {

		DnsRecordSet recordSet = mock(DnsRecordSet.class);
		when(recordSet.getDnsRecordSetId()).thenReturn(DNS_ID);
		when(service.storeElementDnsRecordSet(ELEMENT_ID, recordSet)).thenReturn(true);
		
		Response response = resource.storeElementDnsRecordSet(ELEMENT_ID, DNS_ID,recordSet);
		assertEquals(201, response.getStatus());
	}
	
	@Test
	public void send_created_response_when_putting_a_new_dns_record_for_an_element_identified_by_name() {

		DnsRecordSet recordSet = mock(DnsRecordSet.class);
		when(recordSet.getDnsRecordSetId()).thenReturn(DNS_ID);
		when(service.storeElementDnsRecordSet(ELEMENT_NAME, recordSet)).thenReturn(true);
		
		Response response = resource.storeElementDnsRecordSet(ELEMENT_NAME, DNS_ID,recordSet);
		assertEquals(201, response.getStatus());
		
	}
	
	@Test
	public void send_created_response_when_posting_a_new_dns_record_for_an_element_identified_by_id() {

		DnsRecordSet recordSet = mock(DnsRecordSet.class);
		when(recordSet.getDnsRecordSetId()).thenReturn(DNS_ID);
		when(service.storeElementDnsRecordSet(ELEMENT_ID, recordSet)).thenReturn(true);
		
		Response response = resource.storeElementDnsRecordSet(ELEMENT_ID, recordSet);
		assertEquals(201, response.getStatus());
	}
	
	@Test
	public void send_created_response_when_posting_a_new_dns_record_for_an_element_identified_by_name() {

		DnsRecordSet recordSet = mock(DnsRecordSet.class);
		when(recordSet.getDnsRecordSetId()).thenReturn(DNS_ID);
		when(service.storeElementDnsRecordSet(ELEMENT_NAME, recordSet)).thenReturn(true);
		
		Response response = resource.storeElementDnsRecordSet(ELEMENT_NAME,recordSet);
		assertEquals(201, response.getStatus());
		
	}
	
	
	@Test
	public void send_success_response_when_putting_a_new_dns_record_for_an_element_identified_by_id() {

		DnsRecordSet recordSet = mock(DnsRecordSet.class);
		when(recordSet.getDnsRecordSetId()).thenReturn(DNS_ID);
		
		Response response = resource.storeElementDnsRecordSet(ELEMENT_ID, DNS_ID,recordSet);
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void send_success_response_when_putting_a_new_dns_record_for_an_element_identified_by_name() {

		DnsRecordSet recordSet = mock(DnsRecordSet.class);
		when(recordSet.getDnsRecordSetId()).thenReturn(DNS_ID);
		
		Response response = resource.storeElementDnsRecordSet(ELEMENT_NAME, DNS_ID,recordSet);
		assertEquals(200, response.getStatus());
		
	}
	
	
	
	@Test
	public void send_success_response_when_posting_a_new_dns_record_for_an_element_identified_by_id() {

		DnsRecordSet recordSet = mock(DnsRecordSet.class);
		when(service.storeElementDnsRecordSet(ELEMENT_ID, recordSet)).thenReturn(false);
		
		Response response = resource.storeElementDnsRecordSet(ELEMENT_ID, recordSet);
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void send_success_response_when_posting_a_new_dns_record_for_an_element_identified_by_name() {

		DnsRecordSet recordSet = mock(DnsRecordSet.class);
		when(service.storeElementDnsRecordSet(ELEMENT_NAME, recordSet)).thenReturn(false);
		
		Response response = resource.storeElementDnsRecordSet(ELEMENT_NAME,recordSet);
		assertEquals(200, response.getStatus());
		
	}
	
	
	
}

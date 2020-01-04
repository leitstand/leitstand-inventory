/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.rs;

import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ServiceName.serviceName;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.junit.Assert.assertEquals;
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
import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementServiceSubmission;
import io.leitstand.inventory.service.ElementServicesService;
import io.leitstand.inventory.service.ServiceName;

@RunWith(MockitoJUnitRunner.class)
public class ElementServiceResourceTest {
	
	private static final ElementId ELEMENT_ID = randomElementId();
	private static final ElementName ELEMENT_NAME = elementName("element");
	private static final ServiceName SERVICE_NAME = serviceName("service");
	private static final ElementServiceSubmission SERVICE = ElementServiceSubmission.newElementServiceSubmission()
													  .withServiceName(SERVICE_NAME)
													  .build();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Mock
	private Messages messages;
	
	@Mock
	private ElementServicesService service;
	
	@InjectMocks
	private ElementServicesResource resource = new ElementServicesResource();
	
	
	@Test
	public void throws_UnprocessableEntityException_when_attempting_to_change_a_service_name_for_an_element_identified_by_id() {
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));
		
		resource.storeElementService(ELEMENT_ID, ServiceName.valueOf("other"), SERVICE);
	}
	
	@Test
	public void throws_UnprocessableEntityException_when_attempting_to_change_a_service_name_for_an_element_identified_by_name() {
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));
		
		resource.storeElementService(ELEMENT_ID, ServiceName.valueOf("other"), SERVICE);
	}
	
	@Test
	public void send_created_when_new_service_was_added_for_element_identified_by_id() {
		when(service.storeElementService(ELEMENT_ID, SERVICE)).thenReturn(true);
		
		Response response = resource.storeElementService(ELEMENT_ID, SERVICE_NAME, SERVICE);
		assertEquals(201, response.getStatus());
	}
	
	@Test
	public void send_created_when_new_service_was_added_for_element_identified_by_name() {
		when(service.storeElementService(ELEMENT_NAME, SERVICE)).thenReturn(true);
		
		Response response = resource.storeElementService(ELEMENT_NAME, SERVICE_NAME, SERVICE);
		assertEquals(201, response.getStatus());
	}
	
	@Test
	public void send_success_when_new_service_was_added_for_element_identified_by_id() {
		when(service.storeElementService(ELEMENT_ID, SERVICE)).thenReturn(false);
		
		Response response = resource.storeElementService(ELEMENT_ID, SERVICE_NAME, SERVICE);
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void send_success_when_new_service_was_added_for_element_identified_by_name() {
		when(service.storeElementService(ELEMENT_NAME, SERVICE)).thenReturn(false);
		
		Response response = resource.storeElementService(ELEMENT_NAME, SERVICE_NAME, SERVICE);
		assertEquals(200, response.getStatus());
	}
	
}

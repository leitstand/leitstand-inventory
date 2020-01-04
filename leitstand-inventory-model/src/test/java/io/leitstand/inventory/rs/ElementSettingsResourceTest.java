/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.rs;

import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementSettings.newElementSettings;
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
import io.leitstand.inventory.service.ElementSettings;
import io.leitstand.inventory.service.ElementSettingsService;

@RunWith(MockitoJUnitRunner.class)
public class ElementSettingsResourceTest {

	private static final ElementId ELEMENT_ID = randomElementId();
	private static final ElementName ELEMENT_NAME = elementName("element");
	private static final ElementSettings ELEMENT_SETTINGS = newElementSettings()
															.withElementId(ELEMENT_ID)
															.withElementName(ELEMENT_NAME)
															.build();
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private Messages messages;
	
	@Mock
	private ElementSettingsService service;
	
	@InjectMocks
	private ElementSettingsResource resource = new ElementSettingsResource();
	
	@Test
	public void throws_UnprocessableEntityException_when_attempting_to_change_the_element_id() {
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));
		
		resource.storeElementSettings(randomElementId(), ELEMENT_SETTINGS);
	}
	
	@Test
	public void send_created_response_when_a_new_element_identified_by_id_has_been_added() {
		when(service.storeElementSettings(ELEMENT_SETTINGS)).thenReturn(true);
		
		Response response = resource.storeElementSettings(ELEMENT_ID,ELEMENT_SETTINGS);
		assertEquals(201, response.getStatus());
		
	}
	
	@Test
	public void send_success_response_when_an_existing_element_identified_by_id_was_updated() {
		when(service.storeElementSettings(ELEMENT_SETTINGS)).thenReturn(false);
		
		Response response = resource.storeElementSettings(ELEMENT_ID,ELEMENT_SETTINGS);
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void send_created_response_when_a_new_element_identified_by_name_has_been_added() {
		when(service.storeElementSettings(ELEMENT_SETTINGS)).thenReturn(true);
		
		Response response = resource.storeElementSettings(ELEMENT_NAME,ELEMENT_SETTINGS);
		assertEquals(201, response.getStatus());
		
	}
	
	@Test
	public void send_success_response_when_an_existing_element_identified_by_name_was_updated() {
		when(service.storeElementSettings(ELEMENT_SETTINGS)).thenReturn(false);
		
		Response response = resource.storeElementSettings(ELEMENT_NAME,ELEMENT_SETTINGS);
		assertEquals(200, response.getStatus());
	}

	
}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.rs;

import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.InterfaceName.interfaceName;
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

import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementPhysicalInterfaceService;
import io.leitstand.inventory.service.ElementPhysicalInterfaceSubmission;
import io.leitstand.inventory.service.InterfaceName;

@RunWith(MockitoJUnitRunner.class)
public class ElementPhysicalInterfaceResourceTest {
	
	private static final ElementId ELEMENT_ID = randomElementId();
	private static final ElementName ELEMENT_NAME = elementName("element");
	private static final InterfaceName IFP_NAME = interfaceName("ifp-0/0/0");
	private static final InterfaceName IFL_NAME = interfaceName("ifl-0/0/0/0/0");
	
	
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Mock
	private Messages messages;
	
	@Mock
	private ElementPhysicalInterfaceService service;
	
	@InjectMocks
	private ElementPhysicalInterfaceResource resource = new ElementPhysicalInterfaceResource();
	
	@Test
	public void throws_UnsupportedEntityException_when_attempting_to_change_physical_interface_name_for_element_identified_by_id() {
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));
		
		resource.storePhysicalInterface(ELEMENT_ID,
										IFP_NAME,
										mock(ElementPhysicalInterfaceSubmission.class));
	}
	
	@Test
	public void throws_UnsupportedEntityException_when_attempting_to_change_physical_interface_name_for_element_identified_by_name() {
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));
		
		resource.storePhysicalInterface(ELEMENT_NAME,
										IFP_NAME,
										mock(ElementPhysicalInterfaceSubmission.class));
	}
	
	@Test
	public void send_created_response_when_new_physical_interface_was_added_for_element_identified_by_id() {
		ElementPhysicalInterfaceSubmission ifp = mock(ElementPhysicalInterfaceSubmission.class);
		when(ifp.getIfpName()).thenReturn(IFP_NAME);
		when(service.storePhysicalInterface(ELEMENT_ID,ifp)).thenReturn(true);
		
		Response response = resource.storePhysicalInterface(ELEMENT_ID, 
															IFP_NAME,
															ifp);
		
		assertEquals(201,response.getStatus());
	}
	
	@Test
	public void send_created_response_when_new_physical_interface_was_added_for_element_identified_by_name() {
		ElementPhysicalInterfaceSubmission ifp = mock(ElementPhysicalInterfaceSubmission.class);
		when(ifp.getIfpName()).thenReturn(IFP_NAME);
		when(service.storePhysicalInterface(ELEMENT_NAME,ifp)).thenReturn(true);
				
		Response response = resource.storePhysicalInterface(ELEMENT_NAME, 
				 											IFP_NAME,
				 											ifp);
		
		assertEquals(201,response.getStatus());
	}
	
	@Test
	public void send_success_response_when_new_physical_interface_was_added_for_element_identified_by_id() {
		ElementPhysicalInterfaceSubmission ifp = mock(ElementPhysicalInterfaceSubmission.class);
		when(ifp.getIfpName()).thenReturn(IFP_NAME);
		when(service.storePhysicalInterface(ELEMENT_ID,ifp)).thenReturn(false);
		
		Response response = resource.storePhysicalInterface(ELEMENT_ID,
															IFP_NAME,
															ifp);
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void send_success_response_when_new_physical_interface_was_added_for_element_identified_by_name() {
		ElementPhysicalInterfaceSubmission ifp = mock(ElementPhysicalInterfaceSubmission.class);
		when(ifp.getIfpName()).thenReturn(IFP_NAME);
		when(service.storePhysicalInterface(ELEMENT_ID,ifp)).thenReturn(false);
				
		Response response = resource.storePhysicalInterface(ELEMENT_NAME, 
															IFP_NAME,
															ifp);
		
		assertEquals(200, response.getStatus());
	}
	
}

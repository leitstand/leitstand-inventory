/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.OperationalState.UP;
import static io.leitstand.inventory.service.ReasonCode.IVT0320E_ELEMENT_SERVICE_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0322I_ELEMENT_SERVICE_REMOVED;
import static io.leitstand.inventory.service.ServiceName.serviceName;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Message;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Repository;
import io.leitstand.testing.ut.LeitstandCoreMatchers;

@RunWith(MockitoJUnitRunner.class)
public class ElementServicesManagerTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private Element element;
	
	@Mock
	private Repository repository;
	
	@Mock
	private Messages messages;
	
	@InjectMocks
	private ElementServicesManager manager = new ElementServicesManager();
	
	
	@Test
	public void get_service_stack_throws_EntityNotFoundException_when_service_does_not_exist() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(LeitstandCoreMatchers.reason(IVT0320E_ELEMENT_SERVICE_NOT_FOUND));
		
		manager.getElementServiceStack(element, serviceName("unkknown"));
	}
	
	@Test
	public void get_service_context_throws_EntityNotFoundException_when_service_does_not_exist() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(LeitstandCoreMatchers.reason(IVT0320E_ELEMENT_SERVICE_NOT_FOUND));
		
		manager.getElementService(element, serviceName("unkknown"));
	}
	
	@Test
	public void update_operational_service_state_throws_EntityNotFoundException_when_service_does_not_exist() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(LeitstandCoreMatchers.reason(IVT0320E_ELEMENT_SERVICE_NOT_FOUND));
		
		manager.updateElementServiceOperationalState(element, serviceName("unkknown"), UP);
	}
	
	@Test
	public void do_nothing_when_attempting_to_remove_an_unknown_service(){
		
		manager.removeElementService(element, serviceName("unknown"));
		
		verify(repository,never()).remove(any(Element_Service.class));
		verifyZeroInteractions(messages);
	}
	
	@Test
	public void send_message_when_removing_an_unknown_service(){
		ArgumentCaptor<Message> sentMessage = ArgumentCaptor.forClass(Message.class);
		Element_Service service = mock(Element_Service.class);
		when(repository.execute(any(Query.class))).thenReturn(service);
		doNothing().when(messages).add(sentMessage.capture());
		manager.removeElementService(element, serviceName("unknown"));
		
		verify(repository).remove(service);
		assertEquals(IVT0322I_ELEMENT_SERVICE_REMOVED.getReasonCode(),sentMessage.getValue().getReason());
	}
	
}

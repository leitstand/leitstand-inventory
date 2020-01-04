/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.ElementAlias.elementAlias;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.Environment.newEnvironment;
import static io.leitstand.inventory.service.EnvironmentId.randomEnvironmentId;
import static io.leitstand.inventory.service.EnvironmentName.environmentName;
import static io.leitstand.inventory.service.ReasonCode.IVT0390E_ELEMENT_ENVIRONMENT_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0391I_ELEMENT_ENVIRONMENT_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0392I_ELEMENT_ENVIRONMENT_REMOVED;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static javax.json.Json.createObjectBuilder;
import static org.junit.Assert.assertEquals;
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

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Message;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.event.ElementEnvironmentEvent;
import io.leitstand.inventory.event.ElementEnvironmentRemovedEvent;
import io.leitstand.inventory.event.ElementEnvironmentStoredEvent;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.Environment;
import io.leitstand.inventory.service.EnvironmentId;
import io.leitstand.inventory.service.EnvironmentName;

@RunWith(MockitoJUnitRunner.class)
public class ElementEnvironmentManagerTest {
	
	private static final ElementId ELEMENT_ID = randomElementId();
	private static final EnvironmentId ENVIRONMENT_ID = randomEnvironmentId();
	private static final EnvironmentName ENVIRONMENT_NAME = environmentName("environment");
	private static final ElementGroupId GROUP_ID = randomGroupId();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private Repository repository;
	
	@Mock
	private Event<ElementEnvironmentEvent> event;
	
	@Mock
	private Messages messages;
	
	private Element element;
	
	@InjectMocks
	private ElementEnvironmentManager manager = new ElementEnvironmentManager();
	
	@Before
	public void initTestEnvironment() {
		element = mock(Element.class);
		when(element.getGroupId()).thenReturn(GROUP_ID);
		when(element.getGroupType()).thenReturn(groupType("type"));
		when(element.getGroupName()).thenReturn(groupName("group"));
		when(element.getElementRoleName()).thenReturn(elementRoleName("role"));
		when(element.getElementName()).thenReturn(elementName("element"));
		when(element.getElementId()).thenReturn(ELEMENT_ID);
		when(element.getElementRoleName()).thenReturn(elementRoleName("role"));
		when(element.getElementName()).thenReturn(elementName("element"));
		when(element.getElementAlias()).thenReturn(elementAlias("alias"));
		
	}
	
	@Test
	public void throw_EntityNotFoundException_when_environment_id_is_unknown() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0390E_ELEMENT_ENVIRONMENT_NOT_FOUND));
		
		manager.getElementEnvironment(ENVIRONMENT_ID);
	}
	
	@Test
	public void throw_EntityNotFoundException_when_environment_name_is_unknown() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0390E_ELEMENT_ENVIRONMENT_NOT_FOUND));
		
		manager.getElementEnvironment(mock(Element.class),
									  environmentName("unknown"));
	}
	
	@Test
	public void do_nothing_when_attempting_to_remove_an_unknown_environment_id() {
		manager.removeElementEnvironment(ENVIRONMENT_ID);
		verify(repository, never()).remove(any());
		verifyZeroInteractions(messages);
	}
	
	@Test
	public void do_nothing_when_attempting_to_remove_an_unknown_environment_name() {
		manager.removeElementEnvironment(mock(Element.class),environmentName("unknown"));
		verify(repository, never()).remove(any());
		verifyZeroInteractions(messages);
	}
	
	@Test
	public void send_message_and_fire_event_when_removing_an_environment_identified_by_name() {
		ArgumentCaptor<ElementEnvironmentRemovedEvent> firedEvent = ArgumentCaptor.forClass(ElementEnvironmentRemovedEvent.class);
		doNothing().when(event).fire(firedEvent.capture());
		ArgumentCaptor<Message> sentMessage = ArgumentCaptor.forClass(Message.class);
		doNothing().when(messages).add(sentMessage.capture());

		Element_Environment env = new Element_Environment(element, ENVIRONMENT_ID,ENVIRONMENT_NAME);
		env.setCategory("category");
		env.setDescription("description");
		env.setType("type");
		when(repository.execute(any(Query.class))).thenReturn(env);
		manager.removeElementEnvironment(element,ENVIRONMENT_NAME);

		assertEquals(IVT0392I_ELEMENT_ENVIRONMENT_REMOVED.getReasonCode(),sentMessage.getValue().getReason());
		assertEquals(element.getGroupId(),firedEvent.getValue().getGroupId());
		assertEquals(element.getGroupType(),firedEvent.getValue().getGroupType());
		assertEquals(element.getGroupName(),firedEvent.getValue().getGroupName());
		assertEquals(element.getElementId(),firedEvent.getValue().getElementId());
		assertEquals(element.getElementRoleName(),firedEvent.getValue().getElementRole());
		assertEquals(element.getElementName(),firedEvent.getValue().getElementName());
		assertEquals(element.getElementAlias(),firedEvent.getValue().getElementAlias());
		assertEquals(env.getEnvironmentId(),firedEvent.getValue().getEnvironment().getEnvironmentId());
		assertEquals(environmentName("environment"),firedEvent.getValue().getEnvironment().getEnvironmentName());
		assertEquals("category",firedEvent.getValue().getEnvironment().getCategory());
		assertEquals("type",firedEvent.getValue().getEnvironment().getType());
		assertEquals("description",firedEvent.getValue().getEnvironment().getDescription());
	}
	
	
	@Test
	public void send_message_and_fire_event_when_removing_an_environment_identified_by_id() {
		ArgumentCaptor<ElementEnvironmentRemovedEvent> firedEvent = ArgumentCaptor.forClass(ElementEnvironmentRemovedEvent.class);
		doNothing().when(event).fire(firedEvent.capture());
		ArgumentCaptor<Message> sentMessage = ArgumentCaptor.forClass(Message.class);
		doNothing().when(messages).add(sentMessage.capture());

		Element_Environment env = new Element_Environment(element, ENVIRONMENT_ID,ENVIRONMENT_NAME);
		env.setCategory("category");
		env.setDescription("description");
		env.setType("type");
		when(repository.execute(any(Query.class))).thenReturn(env);
		manager.removeElementEnvironment(ENVIRONMENT_ID);

		assertEquals(IVT0392I_ELEMENT_ENVIRONMENT_REMOVED.getReasonCode(),sentMessage.getValue().getReason());
		assertEquals(element.getGroupId(),firedEvent.getValue().getGroupId());
		assertEquals(element.getGroupType(),firedEvent.getValue().getGroupType());
		assertEquals(element.getGroupName(),firedEvent.getValue().getGroupName());
		assertEquals(element.getElementId(),firedEvent.getValue().getElementId());
		assertEquals(element.getElementRoleName(),firedEvent.getValue().getElementRole());
		assertEquals(element.getElementName(),firedEvent.getValue().getElementName());
		assertEquals(element.getElementAlias(),firedEvent.getValue().getElementAlias());
		assertEquals(env.getEnvironmentId(),firedEvent.getValue().getEnvironment().getEnvironmentId());
		assertEquals(environmentName("environment"),firedEvent.getValue().getEnvironment().getEnvironmentName());
		assertEquals("category",firedEvent.getValue().getEnvironment().getCategory());
		assertEquals("type",firedEvent.getValue().getEnvironment().getType());
		assertEquals("description",firedEvent.getValue().getEnvironment().getDescription());
	}
	
	
	@Test
	public void create_new_environment_when_specified_environment_does_not_exist() {
		ArgumentCaptor<Element_Environment> newEntity = ArgumentCaptor.forClass(Element_Environment.class);
		doNothing().when(repository).add(newEntity.capture());
		ArgumentCaptor<ElementEnvironmentStoredEvent> firedEvent = ArgumentCaptor.forClass(ElementEnvironmentStoredEvent.class);
		doNothing().when(event).fire(firedEvent.capture());
		ArgumentCaptor<Message> sentMessage = ArgumentCaptor.forClass(Message.class);
		doNothing().when(messages).add(sentMessage.capture());

		Environment env = newEnvironment()
				  		  .withEnvironmentId(ENVIRONMENT_ID)
				  		  .withEnvironmentName(environmentName("environment"))
				  		  .withCategory("category")
				  		  .withType("type")
				  		  .withDescription("description")
				  		  .withVariables(createObjectBuilder().add("foo","bar").build())
				  		  .build();
		
		boolean created = manager.storeElementEnvironment(element,
														  env);
		
		assertTrue(created);
		assertEquals(IVT0391I_ELEMENT_ENVIRONMENT_STORED.getReasonCode(),sentMessage.getValue().getReason());
		assertEquals(element.getGroupId(),firedEvent.getValue().getGroupId());
		assertEquals(element.getGroupType(),firedEvent.getValue().getGroupType());
		assertEquals(element.getGroupName(),firedEvent.getValue().getGroupName());
		assertEquals(element.getElementId(),firedEvent.getValue().getElementId());
		assertEquals(element.getElementRoleName(),firedEvent.getValue().getElementRole());
		assertEquals(element.getElementName(),firedEvent.getValue().getElementName());
		assertEquals(element.getElementAlias(),firedEvent.getValue().getElementAlias());
		assertEquals(env.getEnvironmentId(),firedEvent.getValue().getEnvironment().getEnvironmentId());
		assertEquals(environmentName("environment"),firedEvent.getValue().getEnvironment().getEnvironmentName());
		assertEquals("category",firedEvent.getValue().getEnvironment().getCategory());
		assertEquals("type",firedEvent.getValue().getEnvironment().getType());
		assertEquals("description",firedEvent.getValue().getEnvironment().getDescription());
		
	}
	
	@Test
	public void move_environment_to_other_element() {
		Element owner = mock(Element.class);
		when(owner.getElementId()).thenReturn(randomElementId());
		Element_Environment existingEnv = mock(Element_Environment.class);
		when(existingEnv.getElement()).thenReturn(element);
		when(element.getElementId()).thenReturn(randomElementId());
		when(repository.execute(any(Query.class))).thenReturn(existingEnv);
		
		manager.storeElementEnvironment(owner, 
										newEnvironment()
										.withEnvironmentId(ENVIRONMENT_ID)
										.build());
		
		verify(existingEnv).setElement(element);
	}
	
}

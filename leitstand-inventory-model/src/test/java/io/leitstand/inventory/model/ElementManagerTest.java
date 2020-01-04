/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.OperationalState.DOWN;
import static io.leitstand.inventory.service.OperationalState.UP;
import static io.leitstand.inventory.service.ReasonCode.IVT0303E_ELEMENT_NOT_REMOVABLE;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.enterprise.event.Event;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.event.ElementEvent;
import io.leitstand.inventory.event.ElementOperationalStateChangedEvent;
import io.leitstand.inventory.event.ElementRemovedEvent;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementRoleName;

@RunWith(MockitoJUnitRunner.class)
public class ElementManagerTest {
	
	private static final ElementId ELEMENT_ID = randomElementId();
	private static final ElementName ELEMENT_NAME = elementName("ELEMENT");
	private static final ElementRoleName ELEMENT_ROLE = elementRoleName("junit");
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private Repository repository;
	
	@Mock
	private Event<ElementEvent> event;

	@Mock
	private Messages messages;

	@InjectMocks
	private ElementManager manager = new ElementManager();
	
	@Test
	public void cannot_remove_active_element() {
		Element activeElement = mock(Element.class);
		when(activeElement.isActive()).thenReturn(true);
		
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT0303E_ELEMENT_NOT_REMOVABLE));
		
		try {
			manager.remove(activeElement);
		} finally {
			verify(repository,never()).remove(activeElement);
			verify(event,never()).fire(any());
		}
	}
	
	@Test
	public void cannot_force_remove_of_active_element() {
		Element activeElement = mock(Element.class);
		when(activeElement.isActive()).thenReturn(true);
		
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT0303E_ELEMENT_NOT_REMOVABLE));
		
		try {
			manager.remove(activeElement);
		} finally {
			verify(repository,never()).remove(activeElement);
			verify(repository,never()).execute(any(Query.class));
			verify(event,never()).fire(any());
		}
	}
	
	@Test
	public void fire_opstate_updated_event_when_operational_state_changes() {
		Element element = mock(Element.class);
		when(element.getOperationalState()).thenReturn(DOWN);
		when(element.setOperationalState(UP)).thenReturn(DOWN);
		ArgumentCaptor<ElementOperationalStateChangedEvent> firedEvent = ArgumentCaptor.forClass(ElementOperationalStateChangedEvent.class);
		doNothing().when(event).fire(firedEvent.capture());
		
		manager.updateElementOperationalState(element, UP);
		
		ElementOperationalStateChangedEvent opStateChanged = firedEvent.getValue();
		assertThat(opStateChanged.getPreviousState(), is(DOWN));
		assertThat(opStateChanged.getOperationalState(),is(UP));
	}
	
	@Test
	public void do_not_fire_opstate_updated_event_when_operational_state_was_not_changed() {
		Element element = mock(Element.class);
		when(element.getOperationalState()).thenReturn(UP);
		when(element.setOperationalState(UP)).thenReturn(UP);
		
		manager.updateElementOperationalState(element, UP);
		
		verify(event, never()).fire(any());
	}
	
	
	@Test
	public void remove_inactive_element() {
		Element element = mock(Element.class);
		when(element.getElementId()).thenReturn(ELEMENT_ID);
		when(element.getElementRoleName()).thenReturn(ELEMENT_ROLE);
		when(element.getElementName()).thenReturn(ELEMENT_NAME);
		ArgumentCaptor<ElementRemovedEvent> firedEvent = ArgumentCaptor.forClass(ElementRemovedEvent.class);
		doNothing().when(event).fire(firedEvent.capture());

		manager.remove(element);
		
		verify(repository).remove(element);
		ElementRemovedEvent removed = firedEvent.getValue();
		assertEquals(ELEMENT_ID,removed.getElementId());
		assertEquals(ELEMENT_NAME,removed.getElementName());
		assertEquals(ELEMENT_ROLE,removed.getElementRole());
		
	}
	
	@Test
	public void force_remove_inactive_element() {
		Element element = mock(Element.class);
		when(element.getElementId()).thenReturn(ELEMENT_ID);
		when(element.getElementRoleName()).thenReturn(ELEMENT_ROLE);
		when(element.getElementName()).thenReturn(ELEMENT_NAME);
		ArgumentCaptor<ElementRemovedEvent> firedEvent = ArgumentCaptor.forClass(ElementRemovedEvent.class);
		doNothing().when(event).fire(firedEvent.capture());

		manager.forceRemove(element);
		
		verify(repository).remove(element);
		ElementRemovedEvent removed = firedEvent.getValue();
		assertEquals(ELEMENT_ID,removed.getElementId());
		assertEquals(ELEMENT_NAME,removed.getElementName());
		assertEquals(ELEMENT_ROLE,removed.getElementRole());
	}
}

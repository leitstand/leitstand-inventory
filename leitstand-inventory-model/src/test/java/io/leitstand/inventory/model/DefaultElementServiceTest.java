/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;

@RunWith(MockitoJUnitRunner.class)
public class DefaultElementServiceTest {
	
	private static final ElementId ELEMENT_ID = randomElementId();
	private static final ElementName ELEMENT_NAME = elementName("element");
	private static final Element ELEMENT = mock(Element.class);

	@Mock
	private ElementManager manager;
	
	@Mock
	private ElementProvider elements;
	
	@InjectMocks
	private DefaultElementService service = new DefaultElementService();
	
	@Test
	public void do_nothing_when_removing_a_non_existing_element_id() {
		service.removeElement(ELEMENT_ID);
		verify(elements).tryFetchElement(ELEMENT_ID);
		verifyZeroInteractions(manager);
	}
	
	@Test
	public void do_nothing_when_removing_a_non_existing_element_name() {
		service.removeElement(ELEMENT_NAME);
		verify(elements).tryFetchElement(ELEMENT_NAME);
		verifyZeroInteractions(manager);
	}
	
	@Test
	public void do_nothing_when_force_removing_a_non_existing_element_id() {
		service.forceRemoveElement(ELEMENT_ID);
		verify(elements).tryFetchElement(ELEMENT_ID);
		verifyZeroInteractions(manager);
	}
	
	@Test
	public void do_nothing_when_force_removing_a_non_existing_element_name() {
		service.forceRemoveElement(ELEMENT_NAME);
		verify(elements).tryFetchElement(ELEMENT_NAME);
		verifyZeroInteractions(manager);
	}
	
	@Test
	public void remove_existing_element_identified_by_id() {
		when(elements.tryFetchElement(ELEMENT_ID)).thenReturn(ELEMENT);
		
		service.removeElement(ELEMENT_ID);
		verify(manager).remove(ELEMENT);
	}
	
	@Test
	public void remove_exsting_element_identified_by_name() {
		when(elements.tryFetchElement(ELEMENT_NAME)).thenReturn(ELEMENT);
		
		service.removeElement(ELEMENT_NAME);
		verify(manager).remove(ELEMENT);
	}
	
	@Test
	public void force_remove_existing_element_identified_by_id() {
		when(elements.tryFetchElement(ELEMENT_ID)).thenReturn(ELEMENT);
		
		service.forceRemoveElement(ELEMENT_ID);
		verify(manager).forceRemove(ELEMENT);
	}
	
	@Test
	public void force_remove_exsting_element_identified_by_name() {
		when(elements.tryFetchElement(ELEMENT_NAME)).thenReturn(ELEMENT);
		
		service.forceRemoveElement(ELEMENT_NAME);
		verify(manager).forceRemove(ELEMENT);
	}
	
	
	
	
	
	
	
	
	
	
	
	
}

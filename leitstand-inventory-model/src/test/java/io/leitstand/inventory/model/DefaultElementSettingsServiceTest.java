/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.model.ElementSettingsMother.element;
import static io.leitstand.inventory.service.ReasonCode.IVT0300E_ELEMENT_NOT_FOUND;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import javax.enterprise.event.Event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.inventory.event.ElementEvent;
import io.leitstand.inventory.service.ElementSettings;
import io.leitstand.inventory.service.ElementSettingsService;

@RunWith(MockitoJUnitRunner.class)
public class DefaultElementSettingsServiceTest {

	@Mock
	private ElementSettingsManager inventory;
	
	@Mock
	private ElementProvider elements;
	
	@Mock
	private Event<ElementEvent> sink;
	
	@InjectMocks
	private ElementSettingsService service = new DefaultElementSettingsService();
	
	
	@Test
	public void create_new_element_if_no_element_with_specified_elementid_exists() {
		ElementSettings settings = element();
		when(elements.fetchElement(settings.getElementId())).thenThrow(new EntityNotFoundException(IVT0300E_ELEMENT_NOT_FOUND));

		boolean created = service.storeElementSettings(settings);
		assertTrue(created);
		verify(inventory).createElement(settings);
		verify(elements).fetchElement(settings.getElementId());
		verifyNoMoreInteractions(inventory,elements);
	}
	
	
	@Test
	public void update_existing_element() {
		Element element = mock(Element.class);
		ElementSettings settings = element();
		when(elements.fetchElement(settings.getElementId())).thenReturn(element);
		
		boolean created = service.storeElementSettings(settings);
		assertFalse(created);
		verify(inventory).storeElementSettings(element, settings);
		verify(elements).fetchElement(settings.getElementId());
		verifyNoMoreInteractions(inventory,elements);
		
	}
}

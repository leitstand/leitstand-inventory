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

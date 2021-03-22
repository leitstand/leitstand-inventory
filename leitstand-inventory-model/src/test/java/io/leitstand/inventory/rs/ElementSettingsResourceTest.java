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
package io.leitstand.inventory.rs;

import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementSettings.newElementSettings;
import static io.leitstand.inventory.service.ReasonCode.IVT0307E_ELEMENT_NAME_ALREADY_IN_USE;
import static io.leitstand.testing.ut.Answers.ROLLBACK;
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

import io.leitstand.commons.UniqueKeyConstraintViolationException;
import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementSettings;
import io.leitstand.inventory.service.ElementSettingsService;
import io.leitstand.inventory.service.ReasonCode;
import io.leitstand.testing.ut.Answers;

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
	public void cannot_modify_element_id() {
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));
		
		resource.storeElementSettings(randomElementId(), ELEMENT_SETTINGS);
	}
	
	@Test
	public void add_element_by_id() {
		when(service.storeElementSettings(ELEMENT_SETTINGS)).thenReturn(true);
		
		Response response = resource.storeElementSettings(ELEMENT_ID,ELEMENT_SETTINGS);
		assertEquals(201, response.getStatus());
		
	}
	
	@Test
	public void store_element_by_id() {
		when(service.storeElementSettings(ELEMENT_SETTINGS)).thenReturn(false);
		
		Response response = resource.storeElementSettings(ELEMENT_ID,ELEMENT_SETTINGS);
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void add_element_by_name() {
		when(service.storeElementSettings(ELEMENT_SETTINGS)).thenReturn(true);
		
		Response response = resource.storeElementSettings(ELEMENT_NAME,ELEMENT_SETTINGS);
		assertEquals(201, response.getStatus());
		
	}
	
	@Test
	public void store_element_by_name() {
		when(service.storeElementSettings(ELEMENT_SETTINGS)).thenReturn(false);
		
		Response response = resource.storeElementSettings(ELEMENT_NAME,ELEMENT_SETTINGS);
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void report_element_name_unique_key_constraint_violation() {
	    exception.expect(UniqueKeyConstraintViolationException.class);
	    exception.expect(reason(IVT0307E_ELEMENT_NAME_ALREADY_IN_USE));
	    
	    when(service.storeElementSettings(ELEMENT_SETTINGS)).then(ROLLBACK);
	    when(service.getElementSettings(ELEMENT_NAME)).thenReturn(ELEMENT_SETTINGS);
	    
	    resource.storeElementSettings(ELEMENT_SETTINGS);
	}

	
}

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

import static io.leitstand.inventory.service.ElementConfigId.randomConfigId;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ElementConfigId;
import io.leitstand.inventory.service.ElementConfigService;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.StoreElementConfigResult;

@RunWith(MockitoJUnitRunner.class)
public class ElementConfigResourceTest {
	
	private static final ElementId ELEMENT_ID = randomElementId();
	private static final ElementName ELEMENT_NAME = elementName("element");
	private static final ElementConfigId CONFIG_ID = randomConfigId();
	private static final String COMMENT = "comment";

	@Mock
	private ElementConfigService service;
	
	@Mock
	private Messages messages;
	
	@InjectMocks
	private ElementConfigResource resource = new ElementConfigResource();
	
	private StoreElementConfigResult result;
	
	@Before
	public void initTestEnvironment() {
		result  = mock(StoreElementConfigResult.class);
		when(result.getConfigId()).thenReturn(CONFIG_ID);
	}
	
	@Test
	public void send_created_response_when_restore_config_creates_a_new_config_for_an_element_identified_by_id() {
		when(service.editElementConfig(ELEMENT_ID, CONFIG_ID, COMMENT)).thenReturn(result);
		when(result.isCreated()).thenReturn(true);
		
		assertEquals(201,resource.editElementConfig(ELEMENT_ID,CONFIG_ID,COMMENT).getStatus());
	}
	
	@Test
	public void send_redirect_response_when_restore_config_refers_to_an_existing_config_for_an_element_identified_by_id() {
		when(service.editElementConfig(ELEMENT_ID, CONFIG_ID, COMMENT)).thenReturn(result);
		when(result.isCreated()).thenReturn(false);
		
		assertEquals(303,resource.editElementConfig(ELEMENT_ID,CONFIG_ID,COMMENT).getStatus());
	}
	
	@Test
	public void send_created_response_when_edit_config_creates_a_new_config_for_an_element_identified_by_id() {
		when(service.editElementConfig(ELEMENT_ID, CONFIG_ID, COMMENT)).thenReturn(result);
		when(result.isCreated()).thenReturn(true);
		
		assertEquals(201,resource.editElementConfig(ELEMENT_ID,CONFIG_ID,COMMENT).getStatus());
	}
	
	@Test
	public void send_redirect_response_when_edit_config_refers_to_an_existing_config_for_an_element_identified_by_id() {
		when(service.editElementConfig(ELEMENT_ID, CONFIG_ID, COMMENT)).thenReturn(result);
		when(result.isCreated()).thenReturn(false);
		
		assertEquals(303,resource.editElementConfig(ELEMENT_ID,CONFIG_ID,COMMENT).getStatus());
	}
	
	@Test
	public void send_created_response_when_edit_config_creates_a_new_config_for_an_element_identified_by_name() {
		when(service.editElementConfig(ELEMENT_NAME, CONFIG_ID, COMMENT)).thenReturn(result);
		when(result.isCreated()).thenReturn(true);
		
		assertEquals(201,resource.editElementConfig(ELEMENT_NAME,CONFIG_ID,COMMENT).getStatus());
	}
	
	@Test
	public void send_redirect_response_when_edit_config_refers_to_an_existing_config_for_an_element_identified_by_name() {
		when(service.editElementConfig(ELEMENT_NAME, CONFIG_ID, COMMENT)).thenReturn(result);
		when(result.isCreated()).thenReturn(false);
		
		assertEquals(303,resource.editElementConfig(ELEMENT_NAME,CONFIG_ID,COMMENT).getStatus());
	}
	
	
}

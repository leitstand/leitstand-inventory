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

import static io.leitstand.inventory.rs.ElementConfigResource.ext;
import static io.leitstand.inventory.service.ConfigurationState.CANDIDATE;
import static io.leitstand.inventory.service.ElementConfigId.randomConfigId;
import static io.leitstand.inventory.service.ElementConfigName.elementConfigName;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static java.lang.Boolean.TRUE;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN_TYPE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ElementConfig;
import io.leitstand.inventory.service.ElementConfigId;
import io.leitstand.inventory.service.ElementConfigName;
import io.leitstand.inventory.service.ElementConfigService;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.StoreElementConfigResult;

@RunWith(MockitoJUnitRunner.class)
public class ElementConfigResourceTest {
	
	private static final ElementId ELEMENT_ID = randomElementId();
	private static final ElementName ELEMENT_NAME = elementName("element");
	private static final ElementConfigId CONFIG_ID = randomConfigId();
	private static final ElementConfigName CONFIG_NAME = elementConfigName("config");
	private static final String CONFIG = "foo: bar";
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
		when(service.restoreElementConfig(ELEMENT_ID, CONFIG_ID, COMMENT)).thenReturn(result);
		when(result.isCreated()).thenReturn(true);
		
		assertEquals(201,resource.restoreElementConfig(ELEMENT_ID,CONFIG_ID,COMMENT).getStatus());
	}
	
	@Test
	public void send_redirect_response_when_restore_config_refers_to_an_existing_config_for_an_element_identified_by_id() {
		when(service.restoreElementConfig(ELEMENT_ID, CONFIG_ID, COMMENT)).thenReturn(result);
		when(result.isCreated()).thenReturn(false);
		
		assertEquals(303,resource.restoreElementConfig(ELEMENT_ID,CONFIG_ID,COMMENT).getStatus());
	}
	
	@Test
	public void send_created_response_when_edit_config_creates_a_new_config_for_an_element_identified_by_id() {
		when(service.restoreElementConfig(ELEMENT_ID, CONFIG_ID, COMMENT)).thenReturn(result);
		when(result.isCreated()).thenReturn(true);
		
		assertEquals(201,resource.restoreElementConfig(ELEMENT_ID,CONFIG_ID,COMMENT).getStatus());
	}
	
	@Test
	public void send_redirect_response_when_edit_config_refers_to_an_existing_config_for_an_element_identified_by_id() {
		when(service.restoreElementConfig(ELEMENT_ID, CONFIG_ID, COMMENT)).thenReturn(result);
		when(result.isCreated()).thenReturn(false);
		
		assertEquals(303,resource.restoreElementConfig(ELEMENT_ID,CONFIG_ID,COMMENT).getStatus());
	}
	
	@Test
	public void send_created_response_when_edit_config_creates_a_new_config_for_an_element_identified_by_name() {
		when(service.restoreElementConfig(ELEMENT_NAME, CONFIG_ID, COMMENT)).thenReturn(result);
		when(result.isCreated()).thenReturn(true);
		
		assertEquals(201,resource.restoreElementConfig(ELEMENT_NAME,CONFIG_ID,COMMENT).getStatus());
	}
	
    @Test
    public void create_element_config_by_element_id() {
        StoreElementConfigResult result = mock(StoreElementConfigResult.class);
        when(service.storeElementConfig(ELEMENT_ID, CONFIG_NAME, TEXT_PLAIN_TYPE, CANDIDATE, CONFIG, COMMENT)).thenReturn(result);
        when(result.isCreated()).thenReturn(TRUE);
        when(result.getConfigId()).thenReturn(CONFIG_ID);
        Response response = resource.storeElementConfig(ELEMENT_ID, CONFIG_NAME, TEXT_PLAIN, CANDIDATE , COMMENT, CONFIG);
        assertThat(response.getStatus(),is(201));
    }
    
    @Test
    public void store_element_config_by_element_id() {
        StoreElementConfigResult result = mock(StoreElementConfigResult.class);
        when(service.storeElementConfig(ELEMENT_ID, CONFIG_NAME, TEXT_PLAIN_TYPE, CANDIDATE, CONFIG, COMMENT)).thenReturn(result);
        when(result.getConfigId()).thenReturn(CONFIG_ID);
        Response response = resource.storeElementConfig(ELEMENT_ID, CONFIG_NAME, TEXT_PLAIN, CANDIDATE , COMMENT, CONFIG);
        assertThat(response.getStatus(),is(200));
    }
    
    @Test
    public void create_element_config_by_element_name() {
        StoreElementConfigResult result = mock(StoreElementConfigResult.class);
        when(service.storeElementConfig(ELEMENT_NAME, CONFIG_NAME, TEXT_PLAIN_TYPE, CANDIDATE, CONFIG, COMMENT)).thenReturn(result);
        when(result.isCreated()).thenReturn(TRUE);
        when(result.getConfigId()).thenReturn(CONFIG_ID);
        Response response = resource.storeElementConfig(ELEMENT_NAME, CONFIG_NAME, TEXT_PLAIN, CANDIDATE, COMMENT, CONFIG);
        assertThat(response.getStatus(),is(201));
    }
    
    @Test
    public void store_element_config_by_element_name() {
        StoreElementConfigResult result = mock(StoreElementConfigResult.class);
        when(service.storeElementConfig(ELEMENT_NAME, CONFIG_NAME, TEXT_PLAIN_TYPE, CANDIDATE, CONFIG, COMMENT)).thenReturn(result);
        when(result.getConfigId()).thenReturn(CONFIG_ID);
        Response response = resource.storeElementConfig(ELEMENT_NAME, CONFIG_NAME, TEXT_PLAIN, CANDIDATE, COMMENT, CONFIG);
        assertThat(response.getStatus(),is(200));
    }
    
	@Test
	public void send_redirect_response_when_edit_config_refers_to_an_existing_config_for_an_element_identified_by_name() {
		when(service.restoreElementConfig(ELEMENT_NAME, CONFIG_ID, COMMENT)).thenReturn(result);
		when(result.isCreated()).thenReturn(false);
		
		assertEquals(303,resource.restoreElementConfig(ELEMENT_NAME,CONFIG_ID,COMMENT).getStatus());
	}
	
}

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
import static io.leitstand.inventory.service.ElementConfigName.elementConfigName;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static javax.json.Json.createReader;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.StringReader;

import javax.json.JsonObject;
import javax.json.JsonReader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.messages.Messages;
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
	private static final String CONFIG = "{'property':'value','array':['item','item','item'],'flag':true,'int':0,'nested':{'name':'value'}}".replace('\'', '"');
	private static final String COMMENT = "comment";

    static JsonObject parseJson(String json) {
        try(JsonReader reader = createReader(new StringReader(json))){
            return reader.readObject();
        }
    }
	
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
	public void find_configs_for_element_id() {
	    resource.findElementConfigs(ELEMENT_ID, "filter");
	    verify(service).findElementConfigs(ELEMENT_ID, "filter");
	}
	
    @Test
    public void find_configs_for_element_name() {
        resource.findElementConfigs(ELEMENT_NAME, "filter");
        verify(service).findElementConfigs(ELEMENT_NAME, "filter");
    }
	
    @Test
    public void get_element_config_by_element_id() {
        resource.getElementConfig(ELEMENT_ID, CONFIG_ID);
        verify(service).getElementConfig(ELEMENT_ID, CONFIG_ID);
    }
    
    @Test
    public void get_element_config_by_element_name() {
        resource.getElementConfig(ELEMENT_NAME, CONFIG_ID);
        verify(service).getElementConfig(ELEMENT_NAME, CONFIG_ID);
    }
    
    @Test
    public void get_element_config_revisions_by_element_id() {
        resource.getElementConfigRevisions(ELEMENT_ID, CONFIG_NAME);
        verify(service).getElementConfigRevisions(ELEMENT_ID, CONFIG_NAME);
    }
    
    @Test
    public void get_element_config_revisions_by_element_name() {
        resource.getElementConfigRevisions(ELEMENT_NAME, CONFIG_NAME);
        verify(service).getElementConfigRevisions(ELEMENT_NAME, CONFIG_NAME);
    }
    
    @Test
    public void remove_config_revisions_for_element_id() {
        resource.removeElementConfigRevisions(ELEMENT_ID, CONFIG_NAME);
        verify(service).removeElementConfigRevisions(ELEMENT_ID, CONFIG_NAME);
    }
    
    @Test
    public void remove_config_revisions_for_element_name() {
        resource.removeElementConfigRevisions(ELEMENT_NAME, CONFIG_NAME);
        verify(service).removeElementConfigRevisions(ELEMENT_NAME, CONFIG_NAME);
    }
	
    @Test
    public void remove_config_for_element_id() {
        resource.removeElementConfig(ELEMENT_ID, CONFIG_ID);
        verify(service).removeElementConfig(ELEMENT_ID, CONFIG_ID);
    }
    
    @Test
    public void remove_config_for_element_name() {
        resource.removeElementConfig(ELEMENT_NAME, CONFIG_ID);
        verify(service).removeElementConfig(ELEMENT_NAME, CONFIG_ID);
    }
    
	@Test
	public void set_config_comment_for_element_identified_by_id() {
	    resource.storeElementConfigComment(ELEMENT_ID, CONFIG_ID, "comment");
	    verify(service).setElementConfigComment(ELEMENT_ID, CONFIG_ID, "comment");
	}
	
    @Test
    public void set_config_comment_for_element_identified_by_name() {
        resource.storeElementConfigComment(ELEMENT_NAME, CONFIG_ID, "comment");
        verify(service).setElementConfigComment(ELEMENT_NAME, CONFIG_ID, "comment");
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
	public void send_redirect_response_when_edit_config_refers_to_an_existing_config_for_an_element_identified_by_name() {
		when(service.restoreElementConfig(ELEMENT_NAME, CONFIG_ID, COMMENT)).thenReturn(result);
		when(result.isCreated()).thenReturn(false);
		
		assertEquals(303,resource.restoreElementConfig(ELEMENT_NAME,CONFIG_ID,COMMENT).getStatus());
	}
	
}

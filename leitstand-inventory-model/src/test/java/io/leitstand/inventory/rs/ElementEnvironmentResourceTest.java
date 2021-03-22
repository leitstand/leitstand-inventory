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
import static io.leitstand.inventory.service.EnvironmentId.randomEnvironmentId;
import static io.leitstand.inventory.service.EnvironmentName.environmentName;
import static io.leitstand.inventory.service.ReasonCode.IVT0393E_ELEMENT_ENVIRONMENT_EXISTS;
import static io.leitstand.inventory.service.ReasonCode.IVT0393E_ELEMENT_ENVIRONMENT_OWNED_BY_OTHER_ELEMENT;
import static io.leitstand.testing.ut.Answers.ROLLBACK;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.UniqueKeyConstraintViolationException;
import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ElementEnvironment;
import io.leitstand.inventory.service.ElementEnvironmentService;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.Environment;
import io.leitstand.inventory.service.EnvironmentId;
import io.leitstand.inventory.service.EnvironmentName;

@RunWith(MockitoJUnitRunner.class)
public class ElementEnvironmentResourceTest {
    
    private static final ElementId       ELEMENT_ID       = randomElementId();
    private static final ElementName     ELEMENT_NAME     = elementName("element");
    private static final EnvironmentId   ENVIRONMENT_ID   = randomEnvironmentId();
    private static final EnvironmentName ENVIRONMENT_NAME = environmentName("environment");

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private ElementEnvironmentService service;
	
	@Mock
	private Messages messages;
	
	@InjectMocks
	private ElementEnvironmentResource resource = new ElementEnvironmentResource();
	
	@Test
	public void cannot_change_environment_id_when_storing_an_environment_with_element_id() {
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));
		
		Environment env = mock(Environment.class);
		
		resource.storeElementEnvironment(ELEMENT_ID, ENVIRONMENT_ID, env);
	}
	
	@Test
	public void cannot_change_environment_id_when_storing_an_environment_with_element_name() {
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));

		Environment env = mock(Environment.class);
		
		resource.storeElementEnvironment(ELEMENT_NAME, ENVIRONMENT_ID, env);
	}
	
	
	@Test
	public void store_environment_by_element_id() {
	    Environment env = mock(Environment.class);
	    when(env.getEnvironmentId()).thenReturn(ENVIRONMENT_ID);
		
		Response response = resource.storeElementEnvironment(ELEMENT_ID, ENVIRONMENT_ID, env);

		assertEquals(200,response.getStatus());
	}
	
	@Test
	public void store_environment_by_element_name() {
        Environment env = mock(Environment.class);
		when(env.getEnvironmentId()).thenReturn(ENVIRONMENT_ID);

		Response response = resource.storeElementEnvironment(ELEMENT_NAME, ENVIRONMENT_ID, env);

		assertEquals(200,response.getStatus());
	}
	
	
	
	@Test
	public void add_environment_by_element_id() {
	    Environment env = mock(Environment.class);
		when(service.storeElementEnvironment(ELEMENT_ID, env)).thenReturn(true);
		when(env.getEnvironmentId()).thenReturn(ENVIRONMENT_ID);
		
		Response response = resource.storeElementEnvironment(ELEMENT_ID, ENVIRONMENT_ID, env);
		
		assertEquals(201,response.getStatus());
	}
	
	@Test
	public void add_environment_by_element_name() {
        Environment env = mock(Environment.class);
		when(service.storeElementEnvironment(ELEMENT_NAME, env)).thenReturn(true);
		when(env.getEnvironmentId()).thenReturn(ENVIRONMENT_ID);
		
		Response response = resource.storeElementEnvironment(ELEMENT_NAME, ENVIRONMENT_ID, env);
		
		assertEquals(201,response.getStatus());
	}
	
	@Test
	public void report_when_environment_is_owned_by_other_element_id() {
	    ElementEnvironment elementEnv = mock(ElementEnvironment.class);
	    
		when(service.getElementEnvironment(ENVIRONMENT_ID)).thenReturn(elementEnv);
		when(elementEnv.getElementId()).thenReturn(randomElementId());
		
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT0393E_ELEMENT_ENVIRONMENT_OWNED_BY_OTHER_ELEMENT));
		
		resource.getElementEnvironment(randomElementId(), ENVIRONMENT_ID);
	}
	
	@Test
	public void report_when_environment_is_owned_by_other_element_name() {
	    ElementEnvironment elementEnv = mock(ElementEnvironment.class);

		when(service.getElementEnvironment(ENVIRONMENT_ID)).thenReturn(elementEnv);
		when(elementEnv.getElementName()).thenReturn(elementName("env_owner"));
		
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT0393E_ELEMENT_ENVIRONMENT_OWNED_BY_OTHER_ELEMENT));
		
		resource.getElementEnvironment(elementName("other"), ENVIRONMENT_ID);
	}

	@Test
	public void read_environment_by_element_name_and_environment_id() {
	    ElementEnvironment elementEnv = mock(ElementEnvironment.class);
	    when(elementEnv.getElementId()).thenReturn(ELEMENT_ID);
	    when(elementEnv.getElementName()).thenReturn(ELEMENT_NAME);
	    when(service.getElementEnvironment(ENVIRONMENT_ID)).thenReturn(elementEnv);
	        
	    assertEquals(elementEnv,resource.getElementEnvironment(ELEMENT_NAME, ENVIRONMENT_ID));
	}
	
	@Test
	public void read_environment_by_element_name_and_environment_name() {
	    ElementEnvironment elementEnv = mock(ElementEnvironment.class);
	    when(elementEnv.getElementId()).thenReturn(ELEMENT_ID);
	    when(elementEnv.getElementName()).thenReturn(ELEMENT_NAME);
	    when(service.getElementEnvironment(ELEMENT_NAME, ENVIRONMENT_NAME)).thenReturn(elementEnv);
	    
	    assertEquals(elementEnv,resource.getElementEnvironment(ELEMENT_NAME, ENVIRONMENT_NAME));
	}
	
	@Test
	public void read_environment_by_element_id_and_environment_name() {
	    ElementEnvironment elementEnv = mock(ElementEnvironment.class);
	    when(elementEnv.getElementId()).thenReturn(ELEMENT_ID);
	    when(elementEnv.getElementName()).thenReturn(ELEMENT_NAME);
	    when(service.getElementEnvironment(ELEMENT_ID, ENVIRONMENT_NAME)).thenReturn(elementEnv);
	        
	    assertEquals(elementEnv,resource.getElementEnvironment(ELEMENT_ID, ENVIRONMENT_NAME));
	}
	
	@Test
	public void read_environment_by_element_id_and_environment_id() {
	    ElementEnvironment elementEnv = mock(ElementEnvironment.class);
	    when(elementEnv.getElementId()).thenReturn(ELEMENT_ID);
	    when(elementEnv.getElementName()).thenReturn(ELEMENT_NAME);
	    when(service.getElementEnvironment(ENVIRONMENT_ID)).thenReturn(elementEnv);
	            
	    assertEquals(elementEnv,resource.getElementEnvironment(ELEMENT_ID, ENVIRONMENT_ID));
	}
	
	@Test
	public void cannot_remove_element_owned_by_other_element_id() {
	    ElementEnvironment elementEnv = mock(ElementEnvironment.class);

		when(service.getElementEnvironment(ENVIRONMENT_ID)).thenReturn(elementEnv);
		when(elementEnv.getElementId()).thenReturn(randomElementId());
		
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT0393E_ELEMENT_ENVIRONMENT_OWNED_BY_OTHER_ELEMENT));

		resource.removeElementEnvironment(randomElementId(), ENVIRONMENT_ID);
		
	}
	
	@Test
	public void remove_element_environment_by_element_id() {
        ElementEnvironment elementEnv = mock(ElementEnvironment.class);
        when(service.getElementEnvironment(ENVIRONMENT_ID)).thenReturn(elementEnv);
		when(elementEnv.getElementId()).thenReturn(ELEMENT_ID);
		
		resource.removeElementEnvironment(ELEMENT_ID, ENVIRONMENT_ID);
		
		verify(service).removeElementEnvironment(ENVIRONMENT_ID);
	}
	
	@Test
	public void remove_element_environment_by_element_name() {
	    ElementEnvironment elementEnv = mock(ElementEnvironment.class);
	    when(service.getElementEnvironment(ELEMENT_NAME,ENVIRONMENT_NAME)).thenReturn(elementEnv);
	    when(elementEnv.getElementId()).thenReturn(ELEMENT_ID);
	    when(elementEnv.getElementName()).thenReturn(ELEMENT_NAME);
	        
	    resource.removeElementEnvironment(ELEMENT_NAME, ENVIRONMENT_NAME);
	        
	    verify(service).removeElementEnvironment(ELEMENT_NAME, ENVIRONMENT_NAME);
	}
	
	@Test
	public void report_unique_environment_name_validation_by_element_name() {
	    exception.expect(UniqueKeyConstraintViolationException.class);
	    exception.expect(reason(IVT0393E_ELEMENT_ENVIRONMENT_EXISTS));
	    
	    Environment env = mock(Environment.class);
	    ElementEnvironment elementEnv = mock(ElementEnvironment.class);
	    when(env.getEnvironmentName()).thenReturn(ENVIRONMENT_NAME);
	    when(service.storeElementEnvironment(ELEMENT_NAME, env)).then(ROLLBACK);
	    when(service.getElementEnvironment(ELEMENT_NAME, ENVIRONMENT_NAME)).thenReturn(elementEnv);
	    
	    resource.storeElementEnvironment(ELEMENT_NAME, env);
	}

	@Test
	public void report_unique_environment_name_validation_by_element_id() {
	    exception.expect(UniqueKeyConstraintViolationException.class);
	    exception.expect(reason(IVT0393E_ELEMENT_ENVIRONMENT_EXISTS));
	        
	    Environment env = mock(Environment.class);
	    ElementEnvironment elementEnv = mock(ElementEnvironment.class);
	    when(env.getEnvironmentName()).thenReturn(ENVIRONMENT_NAME);
	    when(service.storeElementEnvironment(ELEMENT_ID, env)).then(ROLLBACK);
	    when(service.getElementEnvironment(ELEMENT_ID, ENVIRONMENT_NAME)).thenReturn(elementEnv);
	        
	    resource.storeElementEnvironment(ELEMENT_ID, env);
	}

	
}

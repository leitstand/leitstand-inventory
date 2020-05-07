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
import static io.leitstand.inventory.service.ReasonCode.IVT0393E_ELEMENT_ENVIRONMENT_OWNED_BY_OTHER_ELEMENT;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.junit.Assert.assertEquals;
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
import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ElementEnvironment;
import io.leitstand.inventory.service.ElementEnvironmentService;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.Environment;
import io.leitstand.inventory.service.EnvironmentId;

@RunWith(MockitoJUnitRunner.class)
public class ElementEnvironmentResourceTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private ElementEnvironment elementEnv;
	
	@Mock
	private ElementId elementId;
	
	@Mock
	private ElementName elementName;
	
	@Mock
	private EnvironmentId environmentId;
	
	@Mock
	private Environment env;
	
	@Mock
	private ElementEnvironmentService service;
	
	@Mock
	private Messages messages;
	
	@InjectMocks
	private ElementEnvironmentResource resource = new ElementEnvironmentResource();
	
	@Test
	public void throws_UnprocessableEntityException_when_attempting_to_change_environment_id_for_element_identified_by_id() {
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));
		resource.storeElementEnvironment(elementId, environmentId, env);
	}
	
	@Test
	public void throws_UnprocessableEntityException_when_attempting_to_change_environment_id_for_element_identified_by_name() {
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));
		resource.storeElementEnvironment(elementName, environmentId, env);
	}
	
	
	@Test
	public void send_success_response_when_existing_environment_for_element_identified_by_id_was_updated() {
		when(env.getEnvironmentId()).thenReturn(environmentId);
		
		Response response = resource.storeElementEnvironment(elementId, environmentId, env);

		assertEquals(200,response.getStatus());
	}
	
	@Test
	public void send_success_response_when_existing_environment_for_element_identified_by_name_was_updated() {
		when(env.getEnvironmentId()).thenReturn(environmentId);

		Response response = resource.storeElementEnvironment(elementName, environmentId, env);

		assertEquals(200,response.getStatus());
	}
	
	
	
	@Test
	public void send_created_response_when_new_environment_for_element_identified_by_id_was_created() {
		when(service.storeElementEnvironment(elementId, env)).thenReturn(true);
		when(env.getEnvironmentId()).thenReturn(environmentId);

		
		Response response = resource.storeElementEnvironment(elementId, environmentId, env);
		
		assertEquals(201,response.getStatus());
	}
	
	@Test
	public void send_created_response_when_new_environment_for_element_identified_by_name_was_created() {
		when(service.storeElementEnvironment(elementName, env)).thenReturn(true);
		when(env.getEnvironmentId()).thenReturn(environmentId);

		
		Response response = resource.storeElementEnvironment(elementName, environmentId, env);
		
		assertEquals(201,response.getStatus());
	}
	
	@Test
	public void throw_ConflictException_when_environment_is_owned_by_other_element_id() {
		when(service.getElementEnvironment(environmentId)).thenReturn(elementEnv);
		when(elementEnv.getElementId()).thenReturn(randomElementId());
		
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT0393E_ELEMENT_ENVIRONMENT_OWNED_BY_OTHER_ELEMENT));
		
		resource.getElementEnvironment(randomElementId(), environmentId);
	}
	
	@Test
	public void throw_ConflictException_when_reading_environment_is_owned_by_other_element() {
		when(service.getElementEnvironment(environmentId)).thenReturn(elementEnv);
		when(elementEnv.getElementName()).thenReturn(elementName("env_owner"));
		
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT0393E_ELEMENT_ENVIRONMENT_OWNED_BY_OTHER_ELEMENT));
		
		resource.getElementEnvironment(elementName("name"), environmentId);
	}
	
	@Test
	public void throw_ConflictException_when_removing_environment_that_is_owned_by_other_element() {
		when(service.getElementEnvironment(environmentId)).thenReturn(elementEnv);
		when(elementEnv.getElementId()).thenReturn(randomElementId());
		
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT0393E_ELEMENT_ENVIRONMENT_OWNED_BY_OTHER_ELEMENT));

		resource.removeElementEnvironment(randomElementId(), environmentId);
		
	}
	
	@Test
	public void remove_environment() {
		ElementId elementId = randomElementId();
		when(service.getElementEnvironment(environmentId)).thenReturn(elementEnv);
		when(elementEnv.getElementId()).thenReturn(elementId);
		
		resource.removeElementEnvironment(elementId, environmentId);
		
		verify(service).removeElementEnvironment(environmentId);
	}
	
}

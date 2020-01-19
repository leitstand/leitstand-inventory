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
import static io.leitstand.inventory.service.ElementRoleId.randomElementRoleId;
import static io.leitstand.inventory.service.ElementRoleSettings.newElementRoleSettings;
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

import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ElementRoleId;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ElementRoleService;
import io.leitstand.inventory.service.ElementRoleSettings;

@RunWith(MockitoJUnitRunner.class)
public class ElementRoleResourceTest {
	
	private static final ElementRoleId ROLE_ID = randomElementRoleId();
	private static final ElementRoleName ROLE_NAME = ElementRoleName.valueOf("role");
	private static final ElementRoleSettings ROLE = newElementRoleSettings()
													.withRoleId(ROLE_ID)
													.withRoleName(ROLE_NAME).build();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private ElementRoleService service;
	
	@Mock
	private Messages messages;
	
	@InjectMocks
	private ElementRoleResource resource = new ElementRoleResource();
	
	@Test
	public void throw_UnprocessableEntityException_when_attempting_to_change_element_role_id() {
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));
		
		resource.storeRole(randomElementRoleId(),ROLE);
	}
	
	@Test
	public void send_created_when_a_new_role_has_been_added() {
		when(service.storeElementRole(ROLE)).thenReturn(true);
		Response response = resource.storeRole(ROLE);
		assertEquals(201,response.getStatus());
	}
	
	@Test
	public void send_success_when_an_existing_role_was_updated() {
		when(service.storeElementRole(ROLE)).thenReturn(true);
		Response response = resource.storeRole(ROLE);
		assertEquals(201,response.getStatus());
	}
}

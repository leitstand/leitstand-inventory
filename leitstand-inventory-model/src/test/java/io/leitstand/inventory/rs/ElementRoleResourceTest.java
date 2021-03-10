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
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.ElementRoleSettings.newElementRoleSettings;
import static io.leitstand.inventory.service.ReasonCode.IVT0404E_ELEMENT_ROLE_NAME_ALREADY_IN_USE;
import static io.leitstand.testing.ut.Answers.ROLLBACK;
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

import io.leitstand.commons.UniqueKeyConstraintViolationException;
import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ElementRoleId;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ElementRoleService;
import io.leitstand.inventory.service.ElementRoleSettings;

@RunWith(MockitoJUnitRunner.class)
public class ElementRoleResourceTest {
	
	private static final ElementRoleId ROLE_ID = randomElementRoleId();
	private static final ElementRoleName ROLE_NAME = elementRoleName("role");
	private static final ElementRoleSettings ROLE = newElementRoleSettings()
													.withRoleId(ROLE_ID)
													.withRoleName(ROLE_NAME)
													.build();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private ElementRoleService service;
	
	@Mock
	private Messages messages;
	
	@InjectMocks
	private ElementRoleResource resource = new ElementRoleResource();
	
	@Test
	public void cannot_change_role_id() {
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));
		
		resource.storeRole(randomElementRoleId(),ROLE);
	}
	
	@Test
	public void report_unique_key_constraint_violation() {
	    exception.expect(UniqueKeyConstraintViolationException.class);
	    exception.expect(reason(IVT0404E_ELEMENT_ROLE_NAME_ALREADY_IN_USE));

	    when(service.storeElementRole(ROLE)).then(ROLLBACK);
	    when(service.getElementRole(ROLE_NAME)).thenReturn(ROLE);
	    
	    resource.storeRole(ROLE);
	}
	
	@Test
	public void add_role() {
		when(service.storeElementRole(ROLE)).thenReturn(true);
		Response response = resource.storeRole(ROLE);
		assertEquals(201,response.getStatus());
	}
	
	@Test
	public void store_role() {
		when(service.storeElementRole(ROLE)).thenReturn(true);
		Response response = resource.storeRole(ROLE);
		assertEquals(201,response.getStatus());
	}
	
	@Test
	public void get_element_role_by_id() {
	    when(service.getElementRole(ROLE_ID)).thenReturn(ROLE);
	    assertEquals(ROLE,resource.getElementRole(ROLE_ID));
	}
	
	@Test
	public void get_element_role_by_name() {
	    when(service.getElementRole(ROLE_NAME)).thenReturn(ROLE);
	    assertEquals(ROLE,resource.getElementRole(ROLE_NAME));
	}

    @Test
    public void remove_element_role_by_id() {
        resource.removeElementRole(ROLE_ID);
        verify(service).removeElementRole(ROLE_ID);
    }
    
    @Test
    public void remove_element_role_by_name() {
        resource.removeElementRole(ROLE_NAME);
        verify(service).removeElementRole(ROLE_NAME);
    }
}

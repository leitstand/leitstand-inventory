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

import static io.leitstand.inventory.service.ElementRoleId.randomElementRoleId;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.inventory.service.ElementRoleId;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ElementRoleService;
import io.leitstand.inventory.service.ElementRoleSettings;

@RunWith(MockitoJUnitRunner.class)
public class DefaultElementRoleServiceTest {

	@Mock
	private ElementRoleProvider roles;
	
	@Mock
	private ElementRoleManager manager;
	
	@InjectMocks
	private ElementRoleService service = new DefaultElementRoleService();
	
	
	@Test
	public void create_new_role_if_specified_role_does_not_exist() {
		ElementRoleSettings role = mock(ElementRoleSettings.class);
		
		boolean created = service.storeElementRole(role);
		
		assertTrue(created);
		verify(manager).createElementRole(role);
		
	}
	
	@Test
	public void update_existing_roles() {
		ElementRole role = mock(ElementRole.class);
		ElementRoleId roleId = randomElementRoleId();
		ElementRoleSettings settings = mock(ElementRoleSettings.class);
		when(settings.getRoleId()).thenReturn(roleId);
		when(roles.tryFetchElementRole(roleId)).thenReturn(role);
		
		boolean created = service.storeElementRole(settings);
		
		assertFalse(created);
		verify(manager).storeElementRole(role, settings);
		
	}
	
	@Test
	public void remove_existing_role_identified_by_id() {
		ElementRole role = mock(ElementRole.class);
		ElementRoleId roleId = randomElementRoleId();
		when(roles.tryFetchElementRole(roleId)).thenReturn(role);
		
		service.removeElementRole(roleId);
		
		verify(manager).removeRole(role);		
	}

	@Test
	public void remove_existing_role_identified_by_name() {
		ElementRole role = mock(ElementRole.class);
		ElementRoleName roleName = ElementRoleName.valueOf("role");
		when(roles.tryFetchElementRole(roleName)).thenReturn(role);
		
		service.removeElementRole(roleName);
		
		verify(manager).removeRole(role);		
	}
	
	@Test
	public void do_nothing_when_attempting_to_remove_an_unknown_role() {
		service.removeElementRole(randomElementRoleId());
		service.removeElementRole(ElementRoleName.valueOf("role"));
		verify(manager,never()).removeRole(any(ElementRole.class));
	}
	
}

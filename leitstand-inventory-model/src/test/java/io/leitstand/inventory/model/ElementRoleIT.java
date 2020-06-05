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
import static io.leitstand.inventory.service.ElementRoleSettings.newElementRoleSettings;
import static io.leitstand.inventory.service.Plane.DATA;
import static io.leitstand.inventory.service.ReasonCode.IVT0400E_ELEMENT_ROLE_NOT_FOUND;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Message;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ElementRoleService;
import io.leitstand.inventory.service.ElementRoleSettings;
import io.leitstand.inventory.service.Plane;
import io.leitstand.testing.ut.LeitstandCoreMatchers;

public class ElementRoleIT extends InventoryIT{

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	private ElementRoleService service;
	private ArgumentCaptor<Message> messageCaptor;
	
	@Before
	public void initTestEnvironment() {
		Repository repository = new Repository(getEntityManager());
		ElementRoleProvider provider = new ElementRoleProvider(repository);
		Messages messages = mock(Messages.class);
		messageCaptor = ArgumentCaptor.forClass(Message.class);
		messages.add(messageCaptor.capture());
		ElementRoleManager manager = new ElementRoleManager(repository,
															messages) ;
		
		service = new DefaultElementRoleService(provider,manager);
	}
	
	@Test
	public void add_element_role() {
		ElementRoleSettings newRole = newElementRoleSettings()
									  .withRoleId(randomElementRoleId())
									  .withRoleName(ElementRoleName.valueOf("new_role"))
									  .withDescription("description")
									  .withDisplayName("New Role")
									  .withPlane(Plane.DATA)
									  .withManageable(true)
									  .build();	
		
		transaction(()->{
			assertTrue(service.storeElementRole(newRole));
		});
		
		transaction(() -> {
			ElementRoleSettings created = service.getElementRole(newRole.getRoleId());
			assertEquals(newRole,created);
		});
	}
	
	@Test
	public void update_element_role() {
		
		ElementRoleSettings role = newElementRoleSettings()
								   .withRoleId(randomElementRoleId())
								   .withRoleName(ElementRoleName.valueOf("update-role"))
								   .withDescription("description")
								   .withDisplayName("New Role")
								   .withPlane(DATA)
								   .withManageable(true)
								   .build();
		
		transaction(()->{
			assertTrue(service.storeElementRole(role));
		});

		ElementRoleSettings update = newElementRoleSettings()
									 .withRoleId(role.getRoleId())
									 .withRoleName(ElementRoleName.valueOf("updated-role"))
									 .withDescription("new description")
									 .withDisplayName("Updated role")
									 .withPlane(DATA)
									 .withManageable(true)
									 .build();
		transaction(()->{
			assertFalse(service.storeElementRole(update));
		});
	
		transaction(()->{
			assertEquals(update,service.getElementRole(update.getRoleName()));
			assertEquals(update,service.getElementRole(update.getRoleId()));
		});
	
		
	}
	
	@Test
	public void remove_element_role_identified_by_id() {
	
		
		ElementRoleSettings role = newElementRoleSettings()
								   .withRoleId(randomElementRoleId())
								   .withRoleName(ElementRoleName.valueOf("role-to-remove"))
								   .withDescription("description")
								   .withDisplayName("New Role")
								   .withPlane(Plane.DATA)
								   .withManageable(true)
								   .build();
		
		transaction(()->{
			assertTrue(service.storeElementRole(role));
		});
		
		transaction(()->{
			assertNotNull(service.getElementRole(role.getRoleId()));
			service.removeElementRole(role.getRoleId());
		});
		
		transaction(() -> {
			exception.expect(EntityNotFoundException.class);
			exception.expect(LeitstandCoreMatchers.reason(IVT0400E_ELEMENT_ROLE_NOT_FOUND));
			
			service.getElementRole(role.getRoleId());
		});
		
	}
	
	@Test
	public void remove_element_role_identified_by_name() {
	
		
		ElementRoleSettings role = newElementRoleSettings()
								   .withRoleId(randomElementRoleId())
								   .withRoleName(ElementRoleName.valueOf("role-to-remove"))
								   .withDescription("description")
								   .withDisplayName("New Role")
								   .withPlane(Plane.DATA)
								   .withManageable(true)
								   .build();
		
		transaction(()->{
			assertTrue(service.storeElementRole(role));
		});
		
		transaction(()->{
			assertNotNull(service.getElementRole(role.getRoleName()));
			service.removeElementRole(role.getRoleName());
		});
		
		transaction(() -> {
			exception.expect(EntityNotFoundException.class);
			exception.expect(LeitstandCoreMatchers.reason(IVT0400E_ELEMENT_ROLE_NOT_FOUND));
			
			service.getElementRole(role.getRoleName());
		});
		
	}
	
	@Test
	public void throw_EntityNotFoundException_for_unknown_elementrole_id() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(LeitstandCoreMatchers.reason(IVT0400E_ELEMENT_ROLE_NOT_FOUND));
		
		service.getElementRole(randomElementRoleId());
	}
	
	@Test
	public void throw_EntityNotFoundException_for_unknown_elementrole_name() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(LeitstandCoreMatchers.reason(IVT0400E_ELEMENT_ROLE_NOT_FOUND));
		
		service.getElementRole(ElementRoleName.valueOf("unknown"));
		
	}
	
	
}

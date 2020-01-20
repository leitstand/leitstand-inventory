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

import static io.leitstand.inventory.model.Element.findElementById;
import static io.leitstand.inventory.model.ElementGroup.findElementGroupById;
import static io.leitstand.inventory.model.ElementRole.findRoleByName;
import static io.leitstand.inventory.model.Element_Environment.removeEnvironments;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.Environment.newEnvironment;
import static io.leitstand.inventory.service.EnvironmentId.randomEnvironmentId;
import static io.leitstand.inventory.service.EnvironmentName.environmentName;
import static io.leitstand.inventory.service.ReasonCode.IVT0300E_ELEMENT_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0390E_ELEMENT_ENVIRONMENT_NOT_FOUND;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static javax.json.Json.createObjectBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import javax.enterprise.event.Event;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementEnvironmentService;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.Environment;
import io.leitstand.inventory.service.EnvironmentId;
import io.leitstand.inventory.service.EnvironmentName;
import io.leitstand.inventory.service.Plane;

public class ElementEnvironmentServiceIT extends InventoryIT {
	
	private static final ElementGroupId GROUP_ID = randomGroupId();
	private static final ElementGroupType GROUP_TYPE = groupType("unittest");
	private static final ElementGroupName GROUP_NAME = groupName(ElementEnvironmentServiceIT.class.getSimpleName());
	private static final ElementId ELEMENT_ID = randomElementId();
	private static final ElementName ELEMENT_NAME = elementName(ElementEnvironmentServiceIT.class.getSimpleName());
	private static final ElementRoleName ELEMENT_ROLE = elementRoleName(ElementEnvironmentServiceIT.class.getSimpleName());
	private static final EnvironmentId ENVIRONMENT_ID = randomEnvironmentId();
	private static final EnvironmentName ENVIRONMENT_NAME = EnvironmentName.environmentName("environment");

	@Rule
	public ExpectedException exception = ExpectedException.none(); 
	
	private ElementEnvironmentService service;
	private Repository repository;
	
	
	@Before
	public void initTestEnvironment() {
		this.repository = new Repository(getEntityManager());
		ElementProvider elements = new ElementProvider(repository);
		ElementEnvironmentManager manager = new ElementEnvironmentManager(repository, 
																		  mock(Event.class), 
																		  mock(Messages.class));
		service = new DefaultElementEnvironmentService(elements, manager);
		
		transaction(()->{
			ElementRole role = repository.addIfAbsent(findRoleByName(ELEMENT_ROLE), 
													  () -> new ElementRole(ELEMENT_ROLE, Plane.DATA));	
			
			ElementGroup group = repository.addIfAbsent(findElementGroupById(GROUP_ID),
														() -> new ElementGroup(GROUP_ID, GROUP_TYPE, GROUP_NAME));
			
			repository.addIfAbsent(findElementById(ELEMENT_ID), 
								   () -> new Element(group, role, ELEMENT_ID, ELEMENT_NAME));
			
		});
		
		
	}
	
	@After
	public void clearTestEnvironment() {
		
		transaction(()->{
			Element element = repository.execute(findElementById(ELEMENT_ID));
			repository.execute(removeEnvironments(element));
		});
		
	}
	
	@Test
	public void create_element_environment() {
		Environment env = newEnvironment()
						  .withEnvironmentId(ENVIRONMENT_ID)
						  .withEnvironmentName(ENVIRONMENT_NAME)
						  .withCategory("category")
						  .withDescription("description")
						  .withType("type")
						  .withVariables(createObjectBuilder().add("foo", "bar").build())
						  .build();
		
		transaction(()->{
			boolean created = service.storeElementEnvironment(ELEMENT_ID, env);
			assertTrue(created);
		});
		
		transaction(()->{
			Environment reloaded = service.getElementEnvironment(ELEMENT_ID, ENVIRONMENT_NAME).getEnvironment();
			assertEquals(env,reloaded);
		});
	}
	
	@Test
	public void update_element_environment() {

		transaction(()->{
			Environment env = newEnvironment()
					  		  .withEnvironmentId(ENVIRONMENT_ID)
					  		  .withEnvironmentName(ENVIRONMENT_NAME)
					  		  .withType("type")
					  		  .withVariables(createObjectBuilder().add("foo", "bar").build())
					  		  .build();
			boolean created = service.storeElementEnvironment(ELEMENT_ID, env);
			assertTrue(created);
		});
		
		Environment env = newEnvironment()
						  .withEnvironmentId(ENVIRONMENT_ID)
						  .withEnvironmentName(ENVIRONMENT_NAME)
						  .withCategory("category")
						  .withDescription("description")
						  .withType("type")
						  .withVariables(createObjectBuilder().add("foo", "bar").build())
						  .build();


		transaction(()->{
			boolean created = service.storeElementEnvironment(ELEMENT_ID, env);
			assertFalse(created);
		});
		
		transaction(()->{
			Environment reloaded = service.getElementEnvironment(ELEMENT_ID, ENVIRONMENT_NAME).getEnvironment();
			assertEquals(env,reloaded);
		});
	}
	
	@Test
	public void remove_element_environment() {
		
		transaction(()->{
			Environment env = newEnvironment()
					  		  .withEnvironmentId(ENVIRONMENT_ID)
					  		  .withEnvironmentName(ENVIRONMENT_NAME)
					  		  .withType("type")
					  		  .withVariables(createObjectBuilder().add("foo", "bar").build())
					  		  .build();
			boolean created = service.storeElementEnvironment(ELEMENT_ID, env);
			assertTrue(created);
		});

		transaction(()->{
			service.removeElementEnvironment(ELEMENT_NAME, ENVIRONMENT_NAME);
		});
		
		transaction(()->{
			exception.expect(EntityNotFoundException.class);
			exception.expect(reason(IVT0390E_ELEMENT_ENVIRONMENT_NOT_FOUND));
			service.getElementEnvironment(ELEMENT_NAME, ENVIRONMENT_NAME);
		});
		
	}
	
	
	@Test
	public void remove_element_environment_by_id() {
		transaction(()->{
			Environment env = newEnvironment()
					  		  .withEnvironmentId(ENVIRONMENT_ID)
					  		  .withEnvironmentName(ENVIRONMENT_NAME)
					  		  .withType("type")
					  		  .withVariables(createObjectBuilder().add("foo", "bar").build())
					  		  .build();
			boolean created = service.storeElementEnvironment(ELEMENT_ID, env);
			assertTrue(created);
		});

		transaction(()->{
			service.removeElementEnvironment(ENVIRONMENT_ID);
		});
		
		transaction(()->{
			exception.expect(EntityNotFoundException.class);
			exception.expect(reason(IVT0390E_ELEMENT_ENVIRONMENT_NOT_FOUND));
			service.getElementEnvironment(ENVIRONMENT_ID);
		});
	}
	
	@Test
	public void rename_element_environment() {
		transaction(()->{
			Environment env = newEnvironment()
					  		  .withEnvironmentId(ENVIRONMENT_ID)
					  		  .withEnvironmentName(ENVIRONMENT_NAME)
					  		  .withType("type")
					  		  .withVariables(createObjectBuilder().add("foo", "bar").build())
					  		  .build();
			boolean created = service.storeElementEnvironment(ELEMENT_ID, env);
			assertTrue(created);
		});
		
		Environment env = newEnvironment()
						  .withEnvironmentId(ENVIRONMENT_ID)
						  .withEnvironmentName(environmentName("new-name"))
						  .withCategory("category")
						  .withDescription("description")
						  .withType("type")
						  .withVariables(createObjectBuilder().add("foo", "bar").build())
						  .build();


		transaction(()->{
			boolean created = service.storeElementEnvironment(ELEMENT_ID, env);
			assertFalse(created);
		});
		
		transaction(()->{
			Environment reloaded = service.getElementEnvironment(ELEMENT_ID, environmentName("new-name")).getEnvironment();
			assertEquals(env,reloaded);
		});
		
	}

	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_store_environment_for_unknown_element_id() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0300E_ELEMENT_NOT_FOUND));
		
		service.storeElementEnvironment(randomElementId(), mock(Environment.class));
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_store_environment_for_unknown_element_name() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0300E_ELEMENT_NOT_FOUND));
		
		service.storeElementEnvironment(elementName("UNKNOWN"), mock(Environment.class));		
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_read_environment_from_unknown_element_id() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0300E_ELEMENT_NOT_FOUND));
		
		service.getElementEnvironment(randomElementId(), ENVIRONMENT_NAME);
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_read_environment_from_unknown_element_name() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0300E_ELEMENT_NOT_FOUND));
		
		service.getElementEnvironment(elementName("UNKNOWN"), ENVIRONMENT_NAME);
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_read_unknown_environment_id() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0390E_ELEMENT_ENVIRONMENT_NOT_FOUND));
		
		service.getElementEnvironment(randomEnvironmentId());
	}
	
	
}

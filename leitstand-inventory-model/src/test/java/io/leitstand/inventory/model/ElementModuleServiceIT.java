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

import static io.leitstand.inventory.model.Element.findElementByName;
import static io.leitstand.inventory.model.ElementGroup.findElementGroupByName;
import static io.leitstand.inventory.model.ElementRole.findRoleByName;
import static io.leitstand.inventory.model.Element_Module.removeModules;
import static io.leitstand.inventory.model.ModuleDataMother.testModule;
import static io.leitstand.inventory.service.AdministrativeState.RETIRED;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.ModuleName.moduleName;
import static io.leitstand.inventory.service.Plane.DATA;
import static io.leitstand.inventory.service.ReasonCode.IVT0300E_ELEMENT_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0310E_ELEMENT_MODULE_NOT_FOUND;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.hasSizeOf;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementModuleService;
import io.leitstand.inventory.service.ElementModules;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ModuleData;
import io.leitstand.inventory.service.ModuleName;

public class ElementModuleServiceIT extends InventoryIT {

	private static final ElementName ELEMENT_NAME = elementName("module_test");
	private static final ElementId   ELEMENT_ID   = randomElementId();
	private static final ModuleName MODULE_NAME = moduleName("module");
	private static final ElementName UNKNOWN_ELEMENT = elementName("unknown");
	private static final ModuleName UNKNOWN_MODULE = moduleName("unknown_module");

	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	private ElementModuleService service;
	
	@Before
	public void initTestEnvironment() {
		Repository repository = new Repository(getEntityManager());
		ElementProvider elements = new ElementProvider(repository);
		ElementModuleManager modules = new ElementModuleManager(repository,mock(Messages.class));
		service = new DefaultElementModuleService(elements,modules);
		
		transaction(()->{
			ElementGroup moduleTestGroup = repository.addIfAbsent(findElementGroupByName(groupType("unittest"), 
																						 groupName("module_test")), 
																  () -> new ElementGroup(randomGroupId(), 
																		  				 groupType("unittest"), 
																		  				 groupName("module_test")));
			
			ElementRole role = repository.addIfAbsent(findRoleByName(elementRoleName("module_test")), 
							 						   () -> new ElementRole(elementRoleName("module_test"),DATA)); 
			
			repository.addIfAbsent(findElementByName(ELEMENT_NAME), 
								   () -> new Element(moduleTestGroup,
									  			     role, 
													 ELEMENT_ID,
													 ELEMENT_NAME));
		
		});
		
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_add_module_for_non_existent_element_id() {
		transaction(()->{
			exception.expect(EntityNotFoundException.class);
			exception.expect(reason(IVT0300E_ELEMENT_NOT_FOUND));
			
			service.storeElementModule(randomElementId(), 
									   MODULE_NAME, 
									   mock(ModuleData.class));
		});
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_add_module_for_non_existent_element_name() {
		transaction(()->{
			exception.expect(EntityNotFoundException.class);
			exception.expect(reason(IVT0300E_ELEMENT_NOT_FOUND));
			
			service.storeElementModule(UNKNOWN_ELEMENT, 
									   MODULE_NAME, 
									   mock(ModuleData.class));
		});
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_read_unknown_module_from_element_identified_by_id() {
		transaction(()->{
			exception.expect(EntityNotFoundException.class);
			exception.expect(reason(IVT0310E_ELEMENT_MODULE_NOT_FOUND));

			service.getElementModule(ELEMENT_ID, UNKNOWN_MODULE);
		});
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_read_unknown_module_from_element_identified_by_name() {
		transaction(()->{
			exception.expect(EntityNotFoundException.class);
			exception.expect(reason(IVT0310E_ELEMENT_MODULE_NOT_FOUND));

			service.getElementModule(ELEMENT_NAME, UNKNOWN_MODULE);
		});
	}
	
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_read_elemements_from_unknown_element_id() {
		transaction(()->{
			exception.expect(EntityNotFoundException.class);
			exception.expect(reason(IVT0300E_ELEMENT_NOT_FOUND));
			
			service.getElementModule(randomElementId(),
									 MODULE_NAME);
		});
	}

	@Test
	public void throws_EntityNotFoundException_when_attempting_to_read_elemements_from_unknown_element_name() {
		transaction(()->{
			exception.expect(EntityNotFoundException.class);
			exception.expect(reason(IVT0300E_ELEMENT_NOT_FOUND));
			
			service.getElementModule(UNKNOWN_ELEMENT,
									 MODULE_NAME);
		});
	}
	
	
	@Test
	public void throws_EntityNotFoundException_when_adding_module_for_non_existent_element_identified_by_id() {
		transaction(()->{
			exception.expect(EntityNotFoundException.class);
			exception.expect(reason(IVT0300E_ELEMENT_NOT_FOUND));

			service.storeElementModule(randomElementId(),
									   MODULE_NAME,
									   mock(ModuleData.class));
		});
	}
	
	@Test
	public void throws_EntityNotFoundException_when_adding_module_for_non_existent_element_identified_by_name() {
		transaction(()->{
			exception.expect(EntityNotFoundException.class);
			exception.expect(reason(IVT0300E_ELEMENT_NOT_FOUND));

			service.storeElementModule(UNKNOWN_ELEMENT,
									   MODULE_NAME,
									   mock(ModuleData.class));
		});
	}
	
	@Test
	public void add_module_without_parent() {
		ModuleData module = testModule("can_add_module_without_parent");
		transaction(()->{
			service.storeElementModule(ELEMENT_NAME, 
									   module.getModuleName(), 
									   module);
		});
		
		transaction(()->{
			assertEquals(module,service.getElementModule(ELEMENT_NAME, 
													 	 module.getModuleName()).getModule() );
		});
	}

	
	@Test
	public void add_module_with_existing_parent() {
		ModuleData parent = testModule("can_add_module_with_parent_parent");
		ModuleData module = testModule(parent,"can_add_module_with_parent_child");

		// Store parent module
		transaction(()->{
			service.storeElementModule(ELEMENT_NAME, 
									   parent.getModuleName(), 
									   parent);
		});
		
		// Store child module
		transaction(()->{
			service.storeElementModule(ELEMENT_NAME, 
									   module.getModuleName(), 
									   module);
		});
		
		// Verify child module creation succeeded.
		transaction(()->{
			assertEquals(module,service.getElementModule(ELEMENT_NAME, module.getModuleName()).getModule());
		});
		
	}
	
	@Test
	public void add_module_without_existing_parent() {
		ModuleData parent = testModule("can_add_module_without_existing_parent_parent");
		ModuleData module = testModule(parent,"can_add_module_without_existing_parent_child");


		// Create child module only
		transaction(()->{
			service.storeElementModule(ELEMENT_NAME, 
									   module.getModuleName(), 
									   module);
		});
		
		// Verify child and parent module exist
		transaction(()->{
			assertEquals(module,service.getElementModule(ELEMENT_NAME, module.getModuleName()).getModule());
			assertNotNull(service.getElementModule(ELEMENT_NAME, parent.getModuleName()));

		});
		
	}

	@Test
	public void merge_modules() {
		
		
		ModuleData moduleA = testModule("Module A");
		ModuleData moduleB = testModule("Module B");
		ModuleData moduleC = testModule("Module C");
		
		transaction(() -> {
			service.storeElementModules(ELEMENT_ID, asList(moduleA,moduleB));
		});
		
		transaction(() -> {
			ElementModules modules = service.getElementModules(ELEMENT_ID);
			assertTrue(modules.getModules().contains(moduleA));
			assertTrue(modules.getModules().contains(moduleB));
			assertThat(modules.getModules(),hasSizeOf(2));
			service.storeElementModules(ELEMENT_ID, asList(moduleA,moduleC));
		});

		transaction(() -> {
			ElementModules elementModules = service.getElementModules(ELEMENT_ID);
			Map<ModuleName,ModuleData> modules = elementModules.getModules().stream().collect(Collectors.toMap(ModuleData::getModuleName, Function.identity()));
			assertTrue(modules.containsKey(moduleA.getModuleName()));
			assertTrue(modules.containsKey(moduleB.getModuleName()));
			assertThat(modules.get(moduleB.getModuleName()).getAdministrativeState(),is(RETIRED));
			assertTrue(modules.containsKey(moduleC.getModuleName()));
			assertThat(elementModules.getModules(),hasSizeOf(3));

			
		});

		
	}
	
	

	@Test
	public void remove_module() {
		ModuleData module = testModule("can_add_module_without_parent");
		transaction(()->{
			service.storeElementModule(ELEMENT_NAME, 
									   module.getModuleName(), 
									   module);
		});
		
		transaction(()->{
			assertNotNull(service.getElementModule(ELEMENT_NAME,module.getModuleName()));
			service.removeElementModule(ELEMENT_NAME, module.getModuleName());
		});
		
		transaction(()->{
			try {
				service.getElementModule(ELEMENT_NAME,module.getModuleName());
				fail("Exception expected");
			} catch (EntityNotFoundException e) {
				assertEquals(IVT0310E_ELEMENT_MODULE_NOT_FOUND, e.getReason());
			}
		});
	}
}


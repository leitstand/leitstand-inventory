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

import static io.leitstand.inventory.model.ElementGroup.findElementGroupById;
import static io.leitstand.inventory.model.ElementRole.findRoleByName;
import static io.leitstand.inventory.model.ElementSettingsMother.element;
import static io.leitstand.inventory.model.Platform.findByVendor;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementManagementInterface.newElementManagementInterface;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementPlatformInfo.newPlatformInfo;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.Plane.DATA;
import static io.leitstand.inventory.service.PlatformId.randomPlatformId;
import static io.leitstand.inventory.service.ReasonCode.IVT0100E_GROUP_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0300E_ELEMENT_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0307E_ELEMENT_NAME_ALREADY_IN_USE;
import static io.leitstand.inventory.service.ReasonCode.IVT0400E_ELEMENT_ROLE_NOT_FOUND;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import javax.enterprise.event.Event;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.UniqueKeyConstraintViolationException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementAlias;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementPlatformInfo;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ElementSettings;
import io.leitstand.inventory.service.ElementSettingsService;
import io.leitstand.inventory.service.PlatformService;

public class ElementSettingsServiceIT extends InventoryIT {

	private static final ElementId ELEMENT_ID = randomElementId();
	private static final ElementRoleName ROLE_A = ElementRoleName.valueOf(ElementSettingsServiceIT.class.getSimpleName()+".A");
	private static final ElementRoleName ROLE_B = ElementRoleName.valueOf(ElementSettingsServiceIT.class.getSimpleName()+".B");
	private static final ElementPlatformInfo PLATFORM_A = newPlatformInfo()
														  .withVendorName("ElementSettingsServiceIT")
														  .withModelName("A")
														  .build();
	private static final ElementPlatformInfo PLATFORM_B = newPlatformInfo()
														  .withVendorName("ElementSettingsServiceIT")
														  .withModelName("B")
														  .build();
	private static final ElementPlatformInfo UNKNOWN_PLATFORM = newPlatformInfo()
																.withVendorName("ElementSettingsServiceIT")
																.withModelName("UNKNOWN")
																.build();

	
	private static final ElementRoleName UNKNOWN_ROLE = elementRoleName("unknown");
	private static final ElementName UNKNOWN_ELEMENT = elementName("unknown");
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	private ElementSettingsService service;
	private PlatformService platforms;
	
	private ElementSettings seed;
	
	@Before
	public void initTestEnvironment() {
		Repository repository = new Repository(getEntityManager());
		ElementGroupProvider groups = new ElementGroupProvider(repository);
		ElementProvider elements = new ElementProvider(repository);
		ElementRoleProvider roles = new ElementRoleProvider(repository);
		PlatformProvider platforms = new PlatformProvider(repository);
		ElementSettingsManager manager = new ElementSettingsManager(repository, 
																	groups,
																	roles,
																	platforms,
																	elements,
																    mock(Messages.class), 
																    mock(Event.class));
		
		this.service = new DefaultElementSettingsService(manager,elements,mock(Event.class));
		this.platforms = new DefaultPlatformService(repository,mock(Messages.class));
		this.seed = element(ELEMENT_ID,"StoreElement");

		transaction(() -> {
			repository.addIfAbsent(findRoleByName(ROLE_A),
								   () ->  new ElementRole(ROLE_A, DATA));
	
			repository.addIfAbsent(findRoleByName(ROLE_B),
								   () ->  new ElementRole(ROLE_B, DATA));
	
			repository.addIfAbsent(findRoleByName(seed.getElementRole()),
								   () -> new ElementRole(seed.getElementRole(),DATA));
			
			repository.addIfAbsent(findElementGroupById(seed.getGroupId()),
					   			   () -> {ElementGroup group = new ElementGroup(seed.getGroupId(),
					   					   									    seed.getGroupType(),
					   					   									    seed.getGroupName());
					   			          return group;});
			
			repository.addIfAbsent(findByVendor(PLATFORM_A), 
								   () -> new Platform(randomPlatformId(), 
										   			  PLATFORM_A.getVendorName(), 
										   			  PLATFORM_A.getModelName()));
			
			repository.addIfAbsent(findByVendor(PLATFORM_B), 
									() -> new Platform(randomPlatformId(), 
							   			  PLATFORM_B.getVendorName(), 
							   			  PLATFORM_B.getModelName()));
			
		});
		
	}
	
	@Test
	public void throws_EntityNotFoundException_when_element_group_does_not_exist() {
		ElementSettings unknownGroup = element(seed)
									   .withGroupId(randomGroupId())
									   .withGroupType(groupType("pod"))
									   .withGroupName(groupName("unknown"))
									   .build();
		
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0100E_GROUP_NOT_FOUND));
		
		transaction(() -> {
			service.storeElementSettings(unknownGroup);
		});
		
	}
	
	
	@Test
	public void rename_existing_element() {
		transaction(() -> {
			// Store an initial element and use a unique name to avoid conflicts with other tests
			service.storeElementSettings(element(seed)
										 .withElementName(elementName("rename_existing_element"))
										 .build());
		});
		
		ElementSettings renamed = element(seed)
								  .withElementName(elementName("rename_existing_element__renamed"))
								  .build();
		
		transaction(()->{
			boolean created = service.storeElementSettings(renamed);
			assertFalse(created);

		});	
		transaction(()->{
			
			ElementSettings reloaded = service.getElementSettings(seed.getElementId());
			assertNotSame(renamed.getElementName(), reloaded.getElementName());
			assertEquals(renamed.getElementName(),reloaded.getElementName());
		});
	}
	
	@Test
	public void change_alias_of_existing_element() {
		transaction(() -> {
			// Store an initial element and use a unique name to avoid conflicts with other tests
			service.storeElementSettings(element(seed)
										 .withElementAlias(ElementAlias.valueOf("alias_of_existing_element"))
										 .build());
		});
		
		ElementSettings renamed = element(seed)
								  .withElementAlias(ElementAlias.valueOf("new_alias_of_existing_element"))
								  .build();
		transaction(()->{
			boolean created = service.storeElementSettings(renamed);
			assertFalse(created);
		});
		
		transaction(() ->{
			ElementSettings reloaded = service.getElementSettings(seed.getElementId());
			assertNotSame(renamed.getElementAlias(), reloaded.getElementAlias());
			assertEquals(renamed.getElementAlias(),reloaded.getElementAlias());
			commitTransaction();
		});
	}
	
	
	@Test
	public void add_management_interface() {
		transaction(()->{
			ElementSettings initial = element(seed)
					  				  .withManagementInterfaces(newElementManagementInterface()
					  						  					.withName("REST")
					  						  					.withProtocol("http"),
					  						  					newElementManagementInterface()
					  						  					.withName("SSH")
					  						  					.withPort(22))
					  				  .build();
			
	
			service.storeElementSettings(initial);
		});
		
		transaction(()->{
			assertNotNull(service.getElementSettings(seed.getElementId()).getManagementInterface("SSH"));
			assertNotNull(service.getElementSettings(seed.getElementId()).getManagementInterface("REST"));

		});
	}
	

	
	@Test
	public void remove_management_interface() {
		transaction(()->{
			ElementSettings initial = element(seed)
					  				  .withManagementInterfaces(newElementManagementInterface()
					  						  					.withName("REST")
					  						  					.withProtocol("http"),
					  						  					newElementManagementInterface()
					  						  					.withName("SSH")
					  						  					.withPort(22))
					  				  .build();
	
			service.storeElementSettings(initial);
		});
		
		
		transaction(()->{
			ElementSettings updated = element(seed)
					 				  .withManagementInterfaces(newElementManagementInterface()
									   					 	    .withName("REST")
									   					 	    .withProtocol("http"))
					 				  .build();
	
			service.storeElementSettings(updated);
		});

		transaction(()->{
			assertNull(service.getElementSettings(seed.getElementId()).getManagementInterface("SSH"));
		});
	}
	
	@Test
	public void change_element_role() {
		transaction(() -> {
			// Store an initial element and use a unique name to avoid conflicts with other tests
			service.storeElementSettings(element(seed)
										 .withElementRole(ROLE_A)
										 .build());
		});
		
		// Attempt to assign an unknown role.
		ElementSettings newRole = element(seed)
								  .withElementRole(ROLE_B)
								  .build();
		transaction(() -> {
			ElementSettings reloaded = service.getElementSettings(newRole.getElementId());
			assertNotNull(reloaded);
			assertThat(reloaded.getElementRole(), is(ROLE_A));
			service.storeElementSettings(newRole);
		});

		transaction(() -> {
			ElementSettings reloaded = service.getElementSettings(newRole.getElementId());
			assertNotNull(reloaded);
			assertThat(reloaded.getElementRole(), is(ROLE_B));
			service.storeElementSettings(newRole);
		});

	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_assign_an_unknown_role() {
		transaction(() -> {
			// Store an initial element and use a unique name to avoid conflicts with other tests
			service.storeElementSettings(seed);
		});
		
		
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0400E_ELEMENT_ROLE_NOT_FOUND));

		// Attempt to assign an unknown role.
		ElementSettings newRole = element(seed)
								  .withElementRole(UNKNOWN_ROLE)
								  .build();
		transaction(() -> {
			service.storeElementSettings(newRole);
		});
		
	}
	
	@Test
	public void change_element_platform() {
		transaction(() -> {
			// Store an initial element and use a unique name to avoid conflicts with other tests
			service.storeElementSettings(element(seed)
										 .withPlatform(PLATFORM_A)
										 .build());
		});
		
		// Attempt to assign an unknown role.
		ElementSettings newPlatform = element(seed)
								  	  .withPlatform(PLATFORM_B)
								  	  .build();
		transaction(() -> {
			ElementSettings reloaded = service.getElementSettings(newPlatform.getElementId());
			assertNotNull(reloaded);
			assertThat(reloaded.getPlatform(), is(PLATFORM_A));
			service.storeElementSettings(newPlatform);
		});

		transaction(() -> {
			ElementSettings reloaded = service.getElementSettings(newPlatform.getElementId());
			assertNotNull(reloaded);
			assertThat(reloaded.getPlatform(), is(PLATFORM_B));
			service.storeElementSettings(newPlatform);
		});
		
	}
	
	@Test
	public void create_new_platform_when_element_with_unknown_platform_was_registered() {
		
		ElementSettings elementWithUnknownPlatform = element(seed)
													 .withPlatform(UNKNOWN_PLATFORM)
													 .build();
		
		transaction(() -> {
			// Store an initial element and use a unique name to avoid conflicts with other tests
			service.storeElementSettings(elementWithUnknownPlatform);
		});
		
		transaction(() -> {
			// Verify that unknown platform was created
			assertNotNull(platforms.getPlatform(UNKNOWN_PLATFORM.getVendorName(), 
												UNKNOWN_PLATFORM.getModelName()));
		});
		
	}
	

	
	@Test
	public void get_element_settings_by_name() {
		ElementId elementId = randomElementId();
		ElementName elementName = elementName("get_element_settings_by_name");
		ElementSettings settings = element(seed)
								   .withElementId(elementId)
								   .withElementName(elementName)
								   .build();
		transaction(() -> {
			boolean created = service.storeElementSettings(settings);
			assertTrue(created);
		});
		
		transaction( () -> {
			ElementSettings reloaded = service.getElementSettings(elementName);
			assertNotSame(settings,reloaded);
			assertEquals(settings,reloaded);
		});
		
	}
	
	@Test
	public void get_element_settings_by_id() {
		ElementId elementId = randomElementId();
		ElementName elementName = elementName("get_settings_by_id");
		ElementSettings settings = element(seed)
								   .withElementId(elementId)
								   .withElementName(elementName)
								   .build();
		transaction(() -> {
			boolean created = service.storeElementSettings(settings);
			assertTrue(created);
		});
		
		transaction( () -> {
			ElementSettings reloaded = service.getElementSettings(elementId);
			assertNotSame(settings,reloaded);
			assertEquals(settings,reloaded);
		});
		
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_access_unknown_element_id() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0300E_ELEMENT_NOT_FOUND));
		
		service.getElementSettings(randomElementId());
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_access_unknown_element_name() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0300E_ELEMENT_NOT_FOUND));
		
		service.getElementSettings(UNKNOWN_ELEMENT);
	}
	
	@Test
	public void throws_ConflictException_when_alias_matches_the_name_of_another_element() {
		ElementSettings namedElement = element(seed)
									   .withElementId(randomElementId())
									   .withElementName(elementName("name_alias_conflict_test_name"))
									   .build();
		
		transaction(() -> {
			service.storeElementSettings(namedElement);
		});
		
		exception.expect(UniqueKeyConstraintViolationException.class);
		exception.expect(reason(IVT0307E_ELEMENT_NAME_ALREADY_IN_USE));
		ElementSettings aliasedElement = element(seed)
										 .withElementId(randomElementId())
										 .withElementName(elementName("throws_ConflictException_when_name_and_alias_of_two_different_elements_are_equal"))
										 .withElementAlias(ElementAlias.valueOf("name_alias_conflict_test_name"))
										 .build();

		transaction(()->{
			service.storeElementSettings(aliasedElement);
		});
	}
	
	@Test
	public void throws_ConflictException_when_name_matches_the_alias_of_another_element() {
		ElementSettings namedElement = element(seed)
									   .withElementId(randomElementId())
									   .withElementName(elementName("alias_name_conflict_test_name"))
									   .withElementAlias(ElementAlias.valueOf("alias_name_conflict_test_alias"))
									   .build();
		
		transaction(() -> {
			service.storeElementSettings(namedElement);
		});
		
		exception.expect(UniqueKeyConstraintViolationException.class);
		exception.expect(reason(IVT0307E_ELEMENT_NAME_ALREADY_IN_USE));
		ElementSettings aliasedElement = element(seed)
										 .withElementId(randomElementId())
										 .withElementName(elementName("alias_name_conflict_test_alias"))
										 .build();

		transaction(()->{
			service.storeElementSettings(aliasedElement);
		});
	}
	
	@Test
	public void throws_ConflictException_when_attempting_to_store_two_elements_with_the_same_name() {
		ElementSettings namedElement = element(seed)
									   .withElementId(randomElementId())
									   .withElementName(elementName("name_conflict_test_name"))
									   .build();
		
		transaction(() -> {
			service.storeElementSettings(namedElement);
		});
		
		exception.expect(UniqueKeyConstraintViolationException.class);
		exception.expect(reason(IVT0307E_ELEMENT_NAME_ALREADY_IN_USE));
		ElementSettings clonedElement = element(namedElement)
										 .withElementId(randomElementId())
										 .build();

		transaction(()->{
			service.storeElementSettings(clonedElement);
		});
	}
	
	@Test
	public void name_and_alias_can_be_equal_for_the_same_element() {
		ElementSettings namedElement = element(seed)
									   .withElementId(randomElementId())
									   .withElementName(elementName("name_and_alias_can_be_equal_for_the_same_element"))
									   .withElementAlias(ElementAlias.valueOf("name_and_alias_can_be_equal_for_the_same_element"))
									   .build();

		transaction(() -> {
			assertTrue(service.storeElementSettings(namedElement));
		});
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_move_element_to_unknown_group_id() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0100E_GROUP_NOT_FOUND));
		
		
		ElementSettings elementWithUnknownGroup = element(seed)
										  		  .withGroupId(randomGroupId())
										  		  .build();
		transaction(() -> {
			service.storeElementSettings(elementWithUnknownGroup);
		});		
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_move_element_to_unknown_group_name() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0100E_GROUP_NOT_FOUND));
		
		
		ElementSettings elementWithUnknownGroup = element(seed)
										  		  .withGroupId(null)
										  		  .withGroupName(ElementGroupName.valueOf("UNKNOWN"))
										  		  .build();
		
		transaction(() -> {
			service.storeElementSettings(elementWithUnknownGroup);
		});		
		
	}

	
}

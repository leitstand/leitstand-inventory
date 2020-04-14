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

import static io.leitstand.commons.db.DatabaseService.prepare;
import static io.leitstand.inventory.model.ElementGroup.findElementGroupById;
import static io.leitstand.inventory.model.ElementRole.findRoleByName;
import static io.leitstand.inventory.model.ElementSettingsMother.element;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.Plane.DATA;
import static io.leitstand.inventory.service.PlatformId.randomPlatformId;
import static io.leitstand.inventory.service.PlatformName.platformName;
import static io.leitstand.inventory.service.PlatformSettings.newPlatformSettings;
import static io.leitstand.inventory.service.ReasonCode.IVT0900E_PLATFORM_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0902I_PLATFORM_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0903E_PLATFORM_NOT_REMOVABLE;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.hasSizeOf;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.List;

import javax.enterprise.event.Event;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.db.DatabaseService;
import io.leitstand.commons.messages.Message;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ElementSettings;
import io.leitstand.inventory.service.ElementSettingsService;
import io.leitstand.inventory.service.Plane;
import io.leitstand.inventory.service.PlatformId;
import io.leitstand.inventory.service.PlatformName;
import io.leitstand.inventory.service.PlatformService;
import io.leitstand.inventory.service.PlatformSettings;

public class PlatformServiceIT extends InventoryIT{
	
	private static final ElementGroupId	  GROUP_ID	 = randomGroupId();
	private static final ElementGroupName GROUP_NAME = groupName(PlatformServiceIT.class.getSimpleName());
	private static final ElementGroupType GROUP_TYPE = groupType("pod");
	private static final ElementRoleName  ELEMENT_ROLE = elementRoleName(PlatformServiceIT.class.getSimpleName());
	private static final String 		  VENDOR 	 = PlatformServiceIT.class.getSimpleName();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();

	private ElementSettingsService elements;
	private PlatformService service;
	private ArgumentCaptor<Message> message;
	private Messages messages;
	
	@Before
	public void initTestEnvironment() {
		Repository repository = new Repository(getEntityManager());
		message = ArgumentCaptor.forClass(Message.class);
		messages = mock(Messages.class);
		doNothing().when(messages).add(message.capture());
		service = new DefaultPlatformService(repository,
										     messages);
		
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
		
		this.elements = new DefaultElementSettingsService(manager, 
														  elements, 
														  mock(Event.class));
		
		transaction(()->{
			repository.addIfAbsent(findElementGroupById(GROUP_ID),
								   () -> new ElementGroup(GROUP_ID, GROUP_TYPE, GROUP_NAME));
			
			repository.addIfAbsent(findRoleByName(ELEMENT_ROLE), 
								   () -> new ElementRole(ELEMENT_ROLE, DATA));	
		});
	}
	
	@After
	public void remove_created_platforms() {
		
		transaction(()->{
			DatabaseService db = getDatabase();
			
			db.executeUpdate(prepare("DELETE FROM inventory.element e WHERE e.platform_id IN (SELECT p.id FROM inventory.platform p WHERE p.vendor = ?)", VENDOR));
			db.executeUpdate(prepare("DELETE FROM inventory.platform p WHERE p.vendor = ?", VENDOR));
			
		});
	}
	
	@Test
	public void throws_EntityNotFoundException_when_platform_id_is_unknown() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0900E_PLATFORM_NOT_FOUND));
		
		service.getPlatform(randomPlatformId());
	}
	
	@Test
	public void throws_EntityNotFoundException_when_platform_name_is_unknown() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0900E_PLATFORM_NOT_FOUND));
		
		service.getPlatform(platformName("unknown"));
	}
	
	@Test
	public void add_platform() {
		PlatformId platformId = randomPlatformId();
		PlatformName platformName = platformName("platform");
				
		PlatformSettings newPlatform = newPlatformSettings()
									   .withPlatformId(platformId)
									   .withPlatformName(platformName)
									   .withVendorName(VENDOR)
									   .withModelName("model")
									   .build();

		transaction(() -> {
			boolean created = service.storePlatform(newPlatform);
			assertTrue(created);
		});
		
		transaction(() -> {
			PlatformSettings reloaded = service.getPlatform(newPlatform.getPlatformId());
			assertNotSame(newPlatform,reloaded);
			assertEquals(newPlatform,reloaded);
		});
		
	}
	
	@Test
	public void get_platform_by_id() {
		PlatformId platformId = randomPlatformId();
		PlatformName platformName = platformName("platform");
		
		PlatformSettings platform = newPlatformSettings()
						     		.withPlatformId(platformId)
									.withPlatformName(platformName)
									.withVendorName(VENDOR)
									.withModelName("model")
									.build();

		// Create platform to test remove function
		transaction(() -> {
			boolean created = service.storePlatform(platform);
			assertTrue(created);
		});

		transaction(() -> {
			PlatformSettings reloaded = service.getPlatform(platformId);
			assertNotSame(platform,reloaded);
			assertEquals(platform,reloaded);
		});	
	}
	
	@Test
	public void get_platform_by_name() {
		PlatformId platformId = randomPlatformId();
		PlatformName platformName = platformName("platform");
		
		PlatformSettings platform = newPlatformSettings()
						     		.withPlatformId(platformId)
									.withPlatformName(platformName)
									.withVendorName(VENDOR)
									.withModelName("model")
									.build();

		// Create platform to test remove function
		transaction(() -> {
			boolean created = service.storePlatform(platform);
			assertTrue(created);
		});

		transaction(() -> {
			PlatformSettings reloaded = service.getPlatform(platformName);
			assertNotSame(platform,reloaded);
			assertEquals(platform,reloaded);
		});
		
				
	}
	
	@Test
	public void remove_platform_by_id() {
		PlatformId platformId = randomPlatformId();
		PlatformName platformName = platformName("platform");
		
		PlatformSettings platform = newPlatformSettings()
									.withPlatformId(platformId)
									.withPlatformName(platformName)
									.withVendorName(VENDOR)
									.withModelName("model")
									.build();

		// Create platform to test remove function
		transaction(() -> {
			boolean created = service.storePlatform(platform);
			assertTrue(created);
		});

		transaction(() -> {
			service.removePlatform(platform.getPlatformId());
		});
		
		assertThat(message.getValue().getReason(),is(IVT0902I_PLATFORM_REMOVED.getReasonCode()));
				
	}


	@Test
	public void remove_platform_by_name() {
		PlatformId platformId = randomPlatformId();
		PlatformName platformName = platformName("platform");
		
		PlatformSettings platform = newPlatformSettings()
									.withPlatformId(platformId)
									.withPlatformName(platformName)
									.withVendorName(VENDOR)
									.withModelName("model")
									.build();

		// Create platform to test remove function
		transaction(() -> {
			boolean created = service.storePlatform(platform);
			assertTrue(created);
		});

		transaction(() -> {
			service.removePlatform(platform.getPlatformName());
		});
		
		assertThat(message.getValue().getReason(),is(IVT0902I_PLATFORM_REMOVED.getReasonCode()));
				
	}
	
	@Test
	public void do_nothing_when_removing_an_unknown_platform_id() {
		service.removePlatform(randomPlatformId());
		verify(messages,never()).add(any(Message.class));
	}
	
	@Test
	public void do_nothing_when_removing_an_unknown_platform_name() {
		service.removePlatform(platformName("unknown"));
		verify(messages,never()).add(any(Message.class));
	}
	
	@Test
	public void update_platform() {
		PlatformId platformId = randomPlatformId();
		PlatformName platformName = platformName("platform");
		
		PlatformSettings platform = newPlatformSettings()
									.withPlatformId(platformId)
									.withPlatformName(platformName)
				   				  	.withVendorName(VENDOR)
				   				  	.build();

		transaction(() -> {
			boolean created = service.storePlatform(platform);
			assertTrue(created);
		});
	
		PlatformSettings update = newPlatformSettings()
								  .withPlatformId(platform.getPlatformId())
								  .withPlatformName(platformName("new_platform_name"))
								  .withVendorName(VENDOR)
				  				  .withModelName("updated_model_name")
				  				  .withHalfRackSize(true)
				  				  .withRackUnits(10)
				  				  .withDescription("updated description")
				  				  .build();

		
		
		transaction(() -> {
			PlatformSettings reloaded = service.getPlatform(platform.getPlatformId());
			assertEquals(platform,reloaded);
			boolean created = service.storePlatform(update);
			assertFalse(created);
		});
		
		transaction(() -> {
			PlatformSettings reloaded = service.getPlatform(platform.getPlatformId());
			assertEquals(reloaded,update);
		});

	}

	@Test
	public void filter_platforms_by_platform_name() {
		
		PlatformSettings platformA = newPlatformSettings()
									 .withPlatformName(platformName("A"))
								     .withVendorName(VENDOR)
									 .withModelName("model_a")
									 .build();

		PlatformSettings platformB = newPlatformSettings()
									 .withPlatformName(platformName("specific_name"))
									 .withVendorName(VENDOR)
									 .withModelName("model_b")
									 .build();

		transaction(()->{
			service.storePlatform(platformA);
			service.storePlatform(platformB);
		});
		
		transaction(()->{
			List<PlatformSettings> platforms = service.getPlatforms("specific_\\w+");
			assertThat(platforms,hasSizeOf(1));
			assertEquals(platformName("specific_name"),platforms.get(0).getPlatformName());
			
		});
		
	}
	
	@Test
	public void filter_platforms_by_vendor_name() {
		
		PlatformSettings platformA = newPlatformSettings()
									 .withPlatformName(platformName("A"))
								     .withVendorName(VENDOR)
									 .withModelName("model_a")
									 .build();

		PlatformSettings platformB = newPlatformSettings()
									 .withPlatformName(platformName("B"))
									 .withVendorName(VENDOR)
									 .withModelName("model_b")
									 .build();
		
		
		transaction(()->{
			service.storePlatform(platformA);
			service.storePlatform(platformB);
		});
		
		transaction(()->{
			List<PlatformSettings> platforms = service.getPlatforms(VENDOR);
			assertThat(platforms,hasSizeOf(2));
			assertEquals(platformName("A"),platforms.get(0).getPlatformName());
			assertEquals("model_a",platforms.get(0).getModelName());
			assertEquals(platformName("B"),platforms.get(1).getPlatformName());
			assertEquals("model_b",platforms.get(1).getModelName());
			
		});
		
	}
	
	@Test
	public void filter_platforms_by_model_name() {
		
		PlatformSettings platformA = newPlatformSettings()
				 					 .withPlatformName(platformName("A"))
				 					 .withVendorName(VENDOR)
				 					 .withModelName("model_a")
				 					 .build();

		PlatformSettings platformB = newPlatformSettings()
				 					 .withPlatformName(platformName("B"))
				 					 .withVendorName(VENDOR)
				 					 .withModelName("model_b")
				 					 .build();

		transaction(()->{
			service.storePlatform(platformA);
			service.storePlatform(platformB);
		});
		
		transaction(()->{
			List<PlatformSettings> platforms = service.getPlatforms("model_a");
			assertThat(platforms,hasSizeOf(1));
			assertEquals("model_a",platforms.get(0).getModelName());
			
		});
		
	}
	
	
	@Test
	public void cannot_remove_platform_in_use() {
		PlatformId platformId = randomPlatformId();
		PlatformName platformName = platformName("platform");
		
		PlatformSettings platform = newPlatformSettings()
									.withPlatformId(platformId)
									.withPlatformName(platformName)
									.build();
		
		ElementSettings element = element(element("element_with_platform"))
								  .withGroupId(GROUP_ID)
								  .withElementRole(ELEMENT_ROLE)
								  .withPlatformId(platformId)
								  .withPlatformName(platformName)
								  .build();
		
		transaction(() -> {
			service.storePlatform(platform);
			elements.storeElementSettings(element);
		});
		
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT0903E_PLATFORM_NOT_REMOVABLE));
		
		transaction(() -> {
			service.removePlatform(platformId);
		});
		
	}
	
	
}

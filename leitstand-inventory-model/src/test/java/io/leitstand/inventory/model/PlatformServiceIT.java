/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.model.ElementSettingsMother.element;
import static io.leitstand.inventory.service.ElementPlatformInfo.newPlatformInfo;
import static io.leitstand.inventory.service.PlatformId.randomPlatformId;
import static io.leitstand.inventory.service.PlatformSettings.newPlatformSettings;
import static io.leitstand.inventory.service.ReasonCode.IVT0900E_PLATFORM_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0902I_PLATFORM_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0903E_PLATFORM_NOT_REMOVABLE;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.hasSizeOf;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.isEmptyCollection;
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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Message;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementSettings;
import io.leitstand.inventory.service.ElementSettingsService;
import io.leitstand.inventory.service.PlatformService;
import io.leitstand.inventory.service.PlatformSettings;

public class PlatformServiceIT extends InventoryIT{
	
	private static final String VENDOR = PlatformServiceIT.class.getSimpleName();
	
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
	}
	
	@Test
	public void throws_EntityNotFoundException_when_platform_id_is_unknown() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0900E_PLATFORM_NOT_FOUND));
		
		service.getPlatform(randomPlatformId());
	}
	
	@Test
	public void throws_EntityNotFoundException_when_platform__model_is_unknown() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0900E_PLATFORM_NOT_FOUND));
		
		service.getPlatform(VENDOR,"unknown");
	}
	
	@Test
	public void add_platform() {
		PlatformSettings newPlatform = newPlatformSettings()
									   .withVendorName(VENDOR)
									   .withModelName("new_model")
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
	public void remove_platform() {
		PlatformSettings removePlatform = newPlatformSettings()
									      .withVendorName(VENDOR)
									      .withModelName("remove")
									      .build();

		// Create platform to test remove function
		transaction(() -> {
			boolean created = service.storePlatform(removePlatform);
			assertTrue(created);
		});

		transaction(() -> {
			service.removePlatform(removePlatform.getPlatformId());
		});
		
		assertThat(message.getValue().getReason(),is(IVT0902I_PLATFORM_REMOVED.getReasonCode()));
		
	}

	@Test
	public void do_nothing_when_removing_an_unknown_platform() {
		service.removePlatform(randomPlatformId());
		verify(messages,never()).add(any(Message.class));
	}
	
	@Test
	public void update_platform() {
		PlatformSettings platform = newPlatformSettings()
				   				  	.withVendorName(VENDOR)
				   				  	.withModelName("update_init")
				   				  	.build();

		transaction(() -> {
			boolean created = service.storePlatform(platform);
			assertTrue(created);
		});
	
		PlatformSettings update = newPlatformSettings()
								  .withPlatformId(platform.getPlatformId())
				  				  .withVendorName("updated_vendor_name")
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
	public void get_platform_by_vendor_and_model() {

		PlatformSettings platform = newPlatformSettings()
									.withVendorName(VENDOR)
									.withModelName("vendor_model_query")
									.build();

		transaction(() -> {
			boolean created = service.storePlatform(platform);
			assertTrue(created);
		});

		transaction(() -> {
			PlatformSettings reloaded = service.getPlatform(platform.getVendorName(), platform.getModelName());
			assertEquals(platform,reloaded);
		});

		
	}
	
	@Test
	public void find_platforms_by_vendor() {
		
		PlatformSettings platformA = newPlatformSettings()
								     .withVendorName("GROUP_BY_"+VENDOR)
									 .withModelName("model_a")
									 .build();
		
		PlatformSettings platformB = newPlatformSettings()
									 .withVendorName("GROUP_BY_"+VENDOR)
									 .withModelName("model_b")
									 .build();

		
		transaction(()->{
			assertThat(service.getPlatforms("GROUP_BY_"+VENDOR),isEmptyCollection());
			service.storePlatform(platformA);
			service.storePlatform(platformB);
		});
		
		transaction(()->{
			List<PlatformSettings> platforms = service.getPlatforms("GROUP_BY_"+VENDOR);
			assertThat(platforms,hasSizeOf(2));
			assertEquals("model_a",platforms.get(0).getModelName());
			assertEquals("model_b",platforms.get(1).getModelName());
			
		});
		
		
		
	}
	
	@Test
	public void cannot_remove_platform_in_use() {
		PlatformSettings platform = newPlatformSettings()
									.withVendorName(VENDOR)
									.withModelName("platform_in_use")
									.build();
		
		ElementSettings element = element(element("element_with_platform"))
								  .withPlatform(newPlatformInfo()
										  		.withVendorName(VENDOR)
										  		.withModelName("platform_in_use"))
								  .build();
		
		transaction(() -> {
			service.storePlatform(platform);
			elements.storeElementSettings(element);
		});
		
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT0903E_PLATFORM_NOT_REMOVABLE));
		
		transaction(() -> {
			service.removePlatform(platform.getPlatformId());
		});
		
	}
	
	
}

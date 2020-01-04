/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;
import static io.leitstand.inventory.model.Element.findElementByName;
import static io.leitstand.inventory.model.ElementGroup.findElementGroupByName;
import static io.leitstand.inventory.model.ElementRole.findRoleByName;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupSettings.newElementGroupSettings;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.Geolocation.newGeolocation;
import static io.leitstand.inventory.service.Plane.DATA;
import static io.leitstand.inventory.service.ReasonCode.IVT0100E_GROUP_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0101I_GROUP_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0102I_GROUP_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0103E_GROUP_NOT_REMOVABLE;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

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
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupSettings;
import io.leitstand.inventory.service.ElementGroupSettingsService;
import io.leitstand.inventory.service.ElementRoleName;

public class ElementGroupSettingsServiceIT extends InventoryIT{

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	private ElementGroupSettingsService service;
	private ArgumentCaptor<Message> messageCaptor;
	private Repository repository;
	private Messages messages;
	
	@Before
	public void initTestEnvironment() {
		messages = mock(Messages.class);
		repository = new Repository(getEntityManager());
		ElementGroupManager manager = new ElementGroupManager(repository, getDatabase(), messages);
		ElementGroupProvider groups = new ElementGroupProvider(repository);
		service = new DefaultElementGroupSettingsService(manager, 
														 groups);
		messageCaptor = ArgumentCaptor.forClass(Message.class);
		doNothing().when(messages).add(messageCaptor.capture());
		
	}
	
	@Test
	public void raise_exception_when_group_by_id_does_not_exist() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0100E_GROUP_NOT_FOUND));
		transaction(() ->{
			service.getGroupSettings(randomGroupId());
		});
	}
	
	
	@Test
	public void raise_exception_when_group_name_id_does_not_exist() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0100E_GROUP_NOT_FOUND));
		transaction(() -> {
			service.getGroupSettings(groupType("unittest"),
									 groupName("unknown"));
		});
	}
	
	@Test
	public void add_group_without_tags() {
		ElementGroupSettings group = newElementGroupSettings()
									 .withGroupId(randomGroupId())
									 .withGroupType(groupType("unittest"))
									 .withGroupName(groupName("new_group_without_tags"))
									 .withDescription("unittest group")
									 .build();
		
		transaction(()->{
			boolean created = service.storeElementGroupSettings(group);
			assertTrue(created);
			assertEquals(IVT0101I_GROUP_STORED.getReasonCode(),
						 messageCaptor.getValue().getReason());
		});
		
		transaction(()->{
			ElementGroupSettings storedGroup = service.getGroupSettings(group.getGroupId());
			assertEquals(group,storedGroup);
			assertNotSame(group,storedGroup);
			assertTrue(storedGroup.getTags().isEmpty());
		});
	}
	
	@Test
	public void add_group_with_tags() {
		ElementGroupSettings group = newElementGroupSettings()
									 .withGroupId(randomGroupId())
									 .withGroupType(groupType("unittest"))
									 .withGroupName(groupName("new_group_with_tags"))
									 .withDescription("unittest group")
									 .withTags("a","b")
									 .build();
		
		transaction(()->{
			boolean created = service.storeElementGroupSettings(group);
			assertTrue(created);
			assertEquals(IVT0101I_GROUP_STORED.getReasonCode(),
						 messageCaptor.getValue().getReason());
		});
		
		transaction(()->{
			ElementGroupSettings storedGroup = service.getGroupSettings(group.getGroupId());
			assertEquals(group,storedGroup);
			assertNotSame(group,storedGroup);
			assertFalse(storedGroup.getTags().isEmpty());
			assertTrue(storedGroup.getTags().contains("a"));
			assertTrue(storedGroup.getTags().contains("b"));
		});
	}
	
	
	@Test
	public void rename_existing_group() {
		ElementGroupSettings group = newElementGroupSettings()
									 .withGroupId(randomGroupId())
									 .withGroupType(groupType("unittest"))
									 .withGroupName(groupName("can_rename_existing_group"))
									 .build();

		transaction(()->{
			service.storeElementGroupSettings(group);
		});
		
		transaction(()->{
			ElementGroupSettings renamed = newElementGroupSettings()
					 					   .withGroupId(group.getGroupId())
					 					   .withGroupType(groupType("unittest"))
					 					   .withGroupName(groupName("renamed_group"))
					 					   .build();
			boolean created = service.storeElementGroupSettings(renamed);
			assertFalse(created);
			assertEquals(IVT0101I_GROUP_STORED.getReasonCode(),
						 messageCaptor.getValue().getReason());
			
		});
	
		transaction(()->{
			assertEquals(groupName("renamed_group"),service.getGroupSettings(group.getGroupId()).getGroupName());
		});
		
	}
	
	@Test
	public void set_geolocations() {
		ElementGroupSettings group = newElementGroupSettings()
									 .withGroupId(randomGroupId())
									 .withGroupType(groupType("unittest"))
									 .withGroupName(groupName("can_rename_existing_group"))
									 .build();

		transaction(()->{
			service.storeElementGroupSettings(group);
		});

		transaction(()->{
			ElementGroupSettings located = newElementGroupSettings()
					 					   .withGroupId(group.getGroupId())
					 					   .withGroupType(groupType("unittest"))
					 					   .withGroupName(groupName("can_rename_existing_group"))
					 					   .withLocation("Hofgarten Innsbruck")
					 					   .withGeolocation(newGeolocation()
					 							   			.withLongitude(47.2714364)
					 							   			.withLatitude(11.3977359)
					 							   			.build())
					 					   .build();
			boolean created = service.storeElementGroupSettings(located);
			assertFalse(created);
			assertEquals(IVT0101I_GROUP_STORED.getReasonCode(),
						 messageCaptor.getValue().getReason());
		});
		
	
		transaction(()->{
			ElementGroupSettings locatedGroup = service.getGroupSettings(group.getGroupType(), 
																		 group.getGroupName());
			
			assertEquals("Hofgarten Innsbruck",locatedGroup.getLocation());
			assertEquals(47.2714364d,locatedGroup.getGeolocation().getLongitude(),0.0005);
			assertEquals(11.3977359d,locatedGroup.getGeolocation().getLatitude(),0.0005);
		});
		
	}
	
	@Test
	public void remove_empty_group_by_id() {
		
		ElementGroupId groupId = randomGroupId();
		
		transaction(()->{
			repository.add(new ElementGroup(groupId,
											groupType("unittest"),
											groupName("can_remove_empty_group_by_id")));
		});
		
		transaction(()->{
			assertNotNull(service.getGroupSettings(groupId));
			service.remove(groupId);
			assertEquals(IVT0102I_GROUP_REMOVED.getReasonCode(),messageCaptor.getValue().getReason());
		});
		
		transaction(()->{
			try {
				service.getGroupSettings(groupId);
				fail("EntityNotFoundException expected");
			} catch (EntityNotFoundException e) {
				assertEquals(IVT0100E_GROUP_NOT_FOUND,e.getReason());
			}
		});
		
	}
	
	@Test
	public void remove_empty_group_by_name() {
		
		transaction(()->{
			repository.add(new ElementGroup(randomGroupId(),
											groupType("unittest"),
											groupName("can_remove_empty_group_by_name")));
		});

		transaction(()->{
			assertNotNull(service.getGroupSettings(groupType("unittest"),
												   groupName("can_remove_empty_group_by_name")));
			service.remove(groupType("unittest"),
						   groupName("can_remove_empty_group_by_name"));
			assertEquals(IVT0102I_GROUP_REMOVED.getReasonCode(),messageCaptor.getValue().getReason());
		});
		
		transaction(()->{
			try {
				service.getGroupSettings(groupType("unittest"),
										 groupName("can_remove_empty_group_by_name"));
				fail("EntityNotFoundException expected");
			} catch (EntityNotFoundException e) {
				assertEquals(IVT0100E_GROUP_NOT_FOUND,e.getReason());
			}
		});
		
	}
	
	@Test
	public void do_nothing_when_removing_nonexistent_group() {
		service.remove(randomGroupId());
		service.remove(groupType("unittest"),
					   groupName("non-existent"));	
		verifyZeroInteractions(messages);
	}
	
	@Test
	public void cannot_remove_nonempty_group_by_id() {
		
		ElementGroupId groupId = randomGroupId();
		transaction(()->{

			ElementGroup group = repository.addIfAbsent(findElementGroupByName(groupType("unittest"),
												   				   			   groupName("cannot_remove_nonempty_group_by_id")) , 
														() -> new ElementGroup(groupId,
																			   groupType("unittest"),
																			   groupName("cannot_remove_nonempty_group_by_id")));

			ElementRole role = repository.addIfAbsent(findRoleByName(ElementRoleName.valueOf("unittest")),
													  () -> new ElementRole(ElementRoleName.valueOf("unittest"),
															  				DATA));

			repository.addIfAbsent(findElementByName(elementName("cannot_remove_nonempty_group_by_id")),
								   () -> new Element(group,
								 		   			 role,
								 		   			 randomElementId(),
								 		   			 elementName("cannot_remove_nonempty_group_by_id")));
		});
		
		transaction(() -> {
			try {
				service.remove(groupId);
				fail("ConflictException expected!");
			} catch (ConflictException e) {
				assertEquals(IVT0103E_GROUP_NOT_REMOVABLE,e.getReason());
			}
		});
	}
	
	@Test
	public void cannot_remove_nonempty_group_by_name() {
		
		transaction(()->{

			ElementGroup group = repository.addIfAbsent(findElementGroupByName(groupType("unittest"),
												   				   			   groupName("group")) , 
														() -> new ElementGroup(randomGroupId(),
																			   groupType("unittest"),
																			   groupName("group")));

			ElementRole role = repository.addIfAbsent(findRoleByName(ElementRoleName.valueOf("unittest")),
													  () -> new ElementRole(elementRoleName("unittest"),
															  				DATA));

			repository.addIfAbsent(findElementByName(elementName("unittest")),
								   () -> new Element(group,
								 		   			 role,
								 		   			 randomElementId(),
								 		   			 elementName("unittest")));
		});
		
		transaction(() -> {
			try {
				service.remove(groupType("unittest"),
							   groupName("group"));
				fail("ConflictException expected!");
			} catch (ConflictException e) {
				assertEquals(IVT0103E_GROUP_NOT_REMOVABLE,e.getReason());
			}
		});
	}
	

}

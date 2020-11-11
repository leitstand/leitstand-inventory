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
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupSettings.newElementGroupSettings;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
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
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupSettings;
import io.leitstand.inventory.service.ElementGroupSettingsService;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementRoleName;

public class ElementGroupSettingsServiceIT extends InventoryIT{
	
    private static final ElementGroupId GROUP_ID = randomGroupId();
    private static final ElementGroupType GROUP_TYPE = groupType("unittest");
    private static final ElementGroupName GROUP_NAME = groupName("group");
    private static final ElementRoleName ELEMENT_ROLE = elementRoleName("role");
    private static final ElementId ELEMENT_ID = randomElementId();
    private static final ElementName ELEMENT_NAME = elementName("element");
    
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
		ElementGroupManager manager = new ElementGroupManager(repository, 
															  getDatabase(), 
															  new FacilityProvider(repository),
															  messages);
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
			service.getGroupSettings(GROUP_ID);
		});
	}
	
	
	@Test
	public void raise_exception_when_group_name_id_does_not_exist() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0100E_GROUP_NOT_FOUND));
		transaction(() -> {
			service.getGroupSettings(GROUP_TYPE,
									 GROUP_NAME);
		});
	}
	
	@Test
	public void add_group_without_tags() {
		ElementGroupSettings group = newElementGroupSettings()
									 .withGroupId(GROUP_ID)
									 .withGroupType(GROUP_TYPE)
									 .withGroupName(GROUP_NAME)
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
									 .withGroupId(GROUP_ID)
									 .withGroupType(GROUP_TYPE)
									 .withGroupName(GROUP_NAME)
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
									 .withGroupId(GROUP_ID)
									 .withGroupType(GROUP_TYPE)
									 .withGroupName(GROUP_NAME)
									 .build();

		transaction(()->{
			service.storeElementGroupSettings(group);
		});
		
		transaction(()->{
			ElementGroupSettings renamed = newElementGroupSettings()
					 					   .withGroupId(GROUP_ID)
					 					   .withGroupType(GROUP_TYPE)
					 					   .withGroupName(groupName("renamed_group"))
					 					   .build();
			boolean created = service.storeElementGroupSettings(renamed);
			assertFalse(created);
			assertEquals(IVT0101I_GROUP_STORED.getReasonCode(),
						 messageCaptor.getValue().getReason());
			
		});
	
		transaction(()->{
			assertEquals(groupName("renamed_group"),service.getGroupSettings(GROUP_ID).getGroupName());
		});
		
	}
	
	@Test
	public void remove_empty_group_by_id() {
		
		
		transaction(()->{
			repository.add(new ElementGroup(GROUP_ID,
			                                GROUP_TYPE,
			                                GROUP_NAME));
		});
		
		transaction(()->{
			assertNotNull(service.getGroupSettings(GROUP_ID));
			service.remove(GROUP_ID);
			assertEquals(IVT0102I_GROUP_REMOVED.getReasonCode(),messageCaptor.getValue().getReason());
		});
		
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0100E_GROUP_NOT_FOUND));
		
		transaction(()->{
			service.getGroupSettings(GROUP_ID);
		});
		
	}
	
	@Test
	public void remove_empty_group_by_name() {
		
		transaction(()->{
			repository.add(new ElementGroup(GROUP_ID,
											GROUP_TYPE,
											GROUP_NAME));
		});

		transaction(()->{
			assertNotNull(service.getGroupSettings(GROUP_TYPE,
												   GROUP_NAME));
			service.remove(GROUP_TYPE,
						   GROUP_NAME);
			assertEquals(IVT0102I_GROUP_REMOVED.getReasonCode(),messageCaptor.getValue().getReason());
		});
		
        exception.expect(EntityNotFoundException.class);
        exception.expect(reason(IVT0100E_GROUP_NOT_FOUND));
        
        transaction(()->{
            service.getGroupSettings(GROUP_ID);
        });
        
		
	}
	
	@Test
	public void do_nothing_when_removing_nonexistent_group() {
		transaction(()->{
		    service.remove(GROUP_ID);
		    service.remove(GROUP_TYPE,
		                   GROUP_NAME);	
		});
		verifyZeroInteractions(messages);
	}
	
	@Test
	public void cannot_remove_nonempty_group_by_id() {
		
		transaction(()->{

			ElementGroup group = repository.addIfAbsent(findElementGroupByName(GROUP_TYPE,
												   				   			   GROUP_NAME), 
														() -> new ElementGroup(GROUP_ID,
																			   GROUP_TYPE,
																			   GROUP_NAME));

			ElementRole role = repository.addIfAbsent(findRoleByName(ELEMENT_ROLE),
													  () -> new ElementRole(ELEMENT_ROLE,
															  				DATA));

			repository.addIfAbsent(findElementByName(ELEMENT_NAME),
								   () -> new Element(group,
								 		   			 role,
								 		   			 ELEMENT_ID,
								 		   			 ELEMENT_NAME));
		});
		
	    exception.expect(ConflictException.class);
	    exception.expect(reason(IVT0103E_GROUP_NOT_REMOVABLE));
		
		transaction(() -> {
			service.remove(GROUP_ID);
		});
	}
	
	@Test
	public void cannot_remove_nonempty_group_by_name() {
		
		transaction(()->{

			ElementGroup group = repository.addIfAbsent(findElementGroupByName(GROUP_TYPE,
												   				   			   GROUP_NAME), 
														() -> new ElementGroup(GROUP_ID,
																			   GROUP_TYPE,
																			   GROUP_NAME));

			ElementRole role = repository.addIfAbsent(findRoleByName(ELEMENT_ROLE),
													  () -> new ElementRole(ELEMENT_ROLE,
															  				DATA));

			repository.addIfAbsent(findElementByName(ELEMENT_NAME),
								   () -> new Element(group,
								 		   			 role,
								 		   			 ELEMENT_ID,
								 		   			 ELEMENT_NAME));
		});
		
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT0103E_GROUP_NOT_REMOVABLE));
		
		transaction(() -> {
			service.remove(groupType("unittest"),
						   groupName("group"));
		});
	}
	

}

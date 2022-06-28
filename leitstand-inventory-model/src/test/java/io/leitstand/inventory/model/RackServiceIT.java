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
import static io.leitstand.inventory.model.Platform.findPlatformById;
import static io.leitstand.inventory.service.AdministrativeState.ACTIVE;
import static io.leitstand.inventory.service.AdministrativeState.NEW;
import static io.leitstand.inventory.service.ElementAlias.elementAlias;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.FacilityId.randomFacilityId;
import static io.leitstand.inventory.service.FacilityName.facilityName;
import static io.leitstand.inventory.service.FacilityType.facilityType;
import static io.leitstand.inventory.service.Plane.DATA;
import static io.leitstand.inventory.service.PlatformChipsetName.platformChipsetName;
import static io.leitstand.inventory.service.PlatformId.randomPlatformId;
import static io.leitstand.inventory.service.PlatformName.platformName;
import static io.leitstand.inventory.service.RackId.randomRackId;
import static io.leitstand.inventory.service.RackItemData.newRackItemData;
import static io.leitstand.inventory.service.RackItemData.Face.REAR;
import static io.leitstand.inventory.service.RackItemId.randomRackItemId;
import static io.leitstand.inventory.service.RackName.rackName;
import static io.leitstand.inventory.service.RackSettings.newRackSettings;
import static io.leitstand.inventory.service.ReasonCode.IVT0300E_ELEMENT_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0600E_FACILITY_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0800E_RACK_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0802I_RACK_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0803E_RACK_NOT_REMOVABLE;
import static io.leitstand.inventory.service.ReasonCode.IVT0805I_RACK_ITEM_REMOVED;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.hasSizeOf;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
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
import io.leitstand.inventory.service.ElementAlias;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.FacilityId;
import io.leitstand.inventory.service.FacilityName;
import io.leitstand.inventory.service.FacilityType;
import io.leitstand.inventory.service.PlatformChipsetName;
import io.leitstand.inventory.service.PlatformId;
import io.leitstand.inventory.service.PlatformName;
import io.leitstand.inventory.service.RackId;
import io.leitstand.inventory.service.RackItem;
import io.leitstand.inventory.service.RackItemData;
import io.leitstand.inventory.service.RackItemId;
import io.leitstand.inventory.service.RackItems;
import io.leitstand.inventory.service.RackName;
import io.leitstand.inventory.service.RackService;
import io.leitstand.inventory.service.RackSettings;

public class RackServiceIT extends InventoryIT {
	
	private static final RackId 			 RACK_ID 		  = randomRackId();
	private static final RackName 			 RACK_NAME 		  = rackName("rack");
	private static final RackItemId			 RACK_ITEM_ID	  = randomRackItemId();
	private static final ElementId 			 ELEMENT_ID 	  = randomElementId();
	private static final ElementName 		 ELEMENT_NAME 	  = elementName("element");
	private static final ElementAlias 		 ELEMENT_ALIAS 	  = elementAlias("alias");
	private static final ElementRoleName	 ELEMENT_ROLE	  = elementRoleName("role");
	private static final ElementGroupId 	 GROUP_ID 		  = randomGroupId();
	private static final ElementGroupName 	 GROUP_NAME 	  = groupName("group");
	private static final ElementGroupType	 GROUP_TYPE 	  = groupType("type");
	private static final PlatformId 		 PLATFORM_ID	  = randomPlatformId();
	private static final PlatformName 		 PLATFORM_NAME	  = platformName("platform");
	private static final PlatformChipsetName PLATFORM_CHIPSET = platformChipsetName("chipset");
	private static final FacilityId          FACILITY_ID      = randomFacilityId();
	private static final FacilityName        FACILITY_NAME    = facilityName("facility");   
	private static final FacilityType        FACILITY_TYPE    = facilityType("unittest");
	
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	private Repository repository;
	private RackService service;
	private Messages messages;
	
	@Before
	public void initTestEnvironment() {
		repository = new Repository(getEntityManager());
		messages = mock(Messages.class);
		
		service = new DefaultRackService(repository,
										 new RackProvider(repository),
										 new ElementProvider(repository),
										 new FacilityProvider(repository,messages),
										 messages);
		
		transaction(()->{
			Platform platform = repository.addIfAbsent(findPlatformById(PLATFORM_ID), 
													   () -> new Platform(PLATFORM_ID,
															   			  PLATFORM_NAME,
															   			  PLATFORM_CHIPSET));

			ElementRole role = repository.addIfAbsent(findRoleByName(ELEMENT_ROLE), 
													  () -> new ElementRole(ELEMENT_ROLE,DATA));

			ElementGroup group = repository.addIfAbsent(findElementGroupById(GROUP_ID), 
														() -> new ElementGroup(GROUP_ID, GROUP_TYPE, GROUP_NAME));
			
			repository.addIfAbsent(findElementById(ELEMENT_ID), 
								   () -> { Element element = new Element(group,role,platform,ELEMENT_ID,ELEMENT_NAME);
										   element.setElementAlias(ELEMENT_ALIAS);
										   return element; });
			
		});
		
		
	}
	
	@Test
	public void reading_rack_settings_of_unknown_rack_id_throws_EntityNotFoundException() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0800E_RACK_NOT_FOUND));
		
		transaction(()->{
			service.getRackSettings(randomRackId());
		});
	}
	
	@Test
	public void reading_rack_settings_of_unknown_rack_name_throws_EntityNotFoundException() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0800E_RACK_NOT_FOUND));
		
		transaction(()->{
			service.getRackItems(rackName("unknown"));
		});
	}
	
	@Test
	public void reading_rack_items_of_unknown_rack_id_throws_EntityNotFoundException() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0800E_RACK_NOT_FOUND));
		
		transaction(()->{
			service.getRackSettings(randomRackId());
		});
	}
	
	@Test
	public void reading_rack_items_of_unknown_rack_name_throws_EntityNotFoundException() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0800E_RACK_NOT_FOUND));
		
		transaction(()->{
			service.getRackItems(rackName("unknown"));
		});
	}
	
	@Test
	public void attempt_to_add_rack_item_to_unknown_rack_id_throws_EntityNotFoundException() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0800E_RACK_NOT_FOUND));
		transaction(()->{
			service.storeRackItem(randomRackId(), mock(RackItemData.class));
		});
	}
	
	@Test
	public void attempt_to_add_rack_item_to_unknown_rack_name_throws_EntityNotFoundException() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0800E_RACK_NOT_FOUND));
		transaction(()->{
			service.storeRackItem(rackName("unknown"), mock(RackItemData.class));
		});
	}

	
	@Test
	public void create_rack() {
		RackSettings rack = newRackSettings()
						    .withAscending(true)
						    .withAssetId("asset-123")
						    .withDescription("Unit test rack")
						    .withLocation("Unit test location")
						    .withRackId(RACK_ID)
						    .withRackName(RACK_NAME)
						    .withAdministrativeState(ACTIVE)
						    .withRackType("Unit test rack type")
						    .withSerialNumber("X-1234-TEST")
						    .withUnits(42)
						    .build();
		
		transaction(()->{
			boolean created = service.storeRack(rack);
			assertTrue(created);
		});
		
		transaction(()->{
			RackSettings reloaded = service.getRackSettings(RACK_NAME);
			assertNotSame(rack,reloaded);
			assertEquals(rack,reloaded);
		});
		
	}
	
	@Test
	public void update_rack() {
		RackSettings rack = newRackSettings()
							.withAscending(true)
							.withAssetId("asset-123")
							.withDescription("Unit test rack")
							.withLocation("Unit test location")
							.withRackId(RACK_ID)
							.withRackName(RACK_NAME)
							.withAdministrativeState(ACTIVE)
							.withRackType("Unit test rack type")
							.withSerialNumber("X-1234-TEST")
							.withUnits(42)
							.build();

		transaction(()->{
			boolean created = service.storeRack(rack);
			assertTrue(created);
		});

		RackSettings updated = newRackSettings()
		    			   	   .withAscending(false)
		    			   	   .withAssetId("asset-123 UPDATED")
		    			   	   .withDescription("Unit test rack UPDATED")
		    			   	   .withLocation("Unit test location UPDATED")
		    			   	   .withRackId(RACK_ID)
		    			   	   .withRackName(rackName("UPDATED"))
		    			   	   .withAdministrativeState(NEW)
		    			   	   .withRackType("Unit test type UPDATED")
		    			   	   .withSerialNumber("X-1234-TEST UPDATED")
		    			   	   .withUnits(47)
		    			   	   .build();

		transaction(()->{
			boolean created = service.storeRack(updated);
			assertFalse(created);
		});

		transaction(()->{
			RackSettings reloaded = service.getRackSettings(RACK_ID);
			assertNotSame(updated,reloaded);
			assertEquals(updated,reloaded);
		});
	}
	
	@Test
	public void remove_empty_rack_by_rack_id() {
		RackSettings rack = newRackSettings()
						    .withAscending(true)
						    .withAssetId("asset-123")
						    .withDescription("Unit test rack")
						    .withLocation("Unit test location")
						    .withRackId(RACK_ID)
						    .withRackName(RACK_NAME)
						    .withAdministrativeState(ACTIVE)
						    .withRackType("Unit test rack type")
						    .withSerialNumber("X-1234-TEST")
						    .withUnits(42)
						    .build();

		transaction(()->{
			boolean created = service.storeRack(rack);
			assertTrue(created);
		});
		
		transaction(()->{
			ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
			doNothing().when(messages).add(messageCaptor.capture());
			service.removeRack(RACK_ID);
			Message message = messageCaptor.getValue();
			assertThat(message.getReason(),is(IVT0802I_RACK_REMOVED.getReasonCode()));
		});
	}
	
	@Test
	public void remove_empty_rack_by_rack_name() {
		RackSettings rack = newRackSettings()
							.withRackId(RACK_ID)
							.withRackName(RACK_NAME)
							.build();
	
		transaction(()->{
			boolean created = service.storeRack(rack);
			assertTrue(created);
		});
		
		transaction(()->{
			ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
			doNothing().when(messages).add(messageCaptor.capture());
			service.removeRack(RACK_NAME);
			Message message = messageCaptor.getValue();
			assertThat(message.getReason(),is(IVT0802I_RACK_REMOVED.getReasonCode()));
		});
	}
	
	@Test
	public void remove_does_not_fail_for_unknown_rack_id() {
		transaction(()->{
			service.removeRack(RACK_ID);
			verifyZeroInteractions(messages);
		});
	}
	
	@Test
	public void remove_does_not_fail_for_unknown_rack_name() {
		transaction(()->{
			service.removeRack(RACK_NAME);
			verifyZeroInteractions(messages);
		});
	}
	
	@Test
	public void force_remove_does_not_fail_for_unknown_rack_id() {
		transaction(()->{
			service.removeRack(RACK_ID);
			verifyZeroInteractions(messages);
		});
	}
	
	@Test
	public void force_remove_does_not_fail_for_unknown_rack_name() {
		transaction(()->{
			service.removeRack(RACK_NAME);
			verifyZeroInteractions(messages);
		});
	}

	@Test
	public void store_rack_item_without_element_reference() {
		RackSettings rack = newRackSettings()
							.withRackId(RACK_ID)
							.withRackName(RACK_NAME)
							.build();
		transaction(()->{
			boolean created = service.storeRack(rack);
			assertTrue(created);
		});
		
		RackItemData item = newRackItemData()
							.withRackItemName("Cable Manager")
							.withPosition(1)
							.build();
		
		transaction(()->{
			service.storeRackItem(RACK_ID, item);
		});
		
		transaction(()->{
			RackItems rackItems = service.getRackItems(RACK_ID);
			assertTrue(rackItems.getItems().contains(item));
		});
		
	}
	
	@Test
	public void attempt_to_store_rack_item_with_unknown_element_throws_EntityNotFoundException() {
		
		RackSettings rack = newRackSettings()
							.withRackId(RACK_ID)
							.withRackName(RACK_NAME)
							.build();
		transaction(()->{
			boolean created = service.storeRack(rack);
			assertTrue(created);
		});
		
		RackItemData item = newRackItemData()
							.withElementId(randomElementId())
							.withPosition(1)
							.build();

		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0300E_ELEMENT_NOT_FOUND));
		
		transaction(()->{
			service.storeRackItem(RACK_ID, item);
		});
		
	}
	
	@Test
	public void add_rack_item_with_element_reference() {
		
		RackSettings rack = newRackSettings()
							.withRackId(RACK_ID)
							.withRackName(RACK_NAME)
							.build();
		transaction(()->{
			boolean created = service.storeRack(rack);
			assertTrue(created);
		});
		
		RackItemData item = newRackItemData()
							.withElementId(ELEMENT_ID)
							.withElementName(ELEMENT_NAME)
							.withPosition(1)
							.withFace(REAR)
							.build();
		
		transaction(()->{
			service.storeRackItem(RACK_ID, item);
		});
		
		transaction(()->{
			RackItems rackItems = service.getRackItems(RACK_ID);
			RackItemData reloaded = rackItems.getItems().get(0);
			assertEquals(GROUP_ID,reloaded.getGroupId());
			assertEquals(GROUP_TYPE,reloaded.getGroupType());
			assertEquals(GROUP_NAME,reloaded.getGroupName());
			assertEquals(ELEMENT_ID,reloaded.getElementId());
			assertEquals(ELEMENT_NAME,reloaded.getElementName());
			assertEquals(ELEMENT_ALIAS,reloaded.getElementAlias());
			assertEquals(ELEMENT_ROLE,reloaded.getElementRole());
			assertEquals(PLATFORM_ID,reloaded.getPlatformId());
			assertEquals(PLATFORM_NAME,reloaded.getPlatformName());
			assertEquals(1,reloaded.getPosition());
			assertEquals(REAR,reloaded.getFace());
		});
		
	}
	
	@Test
	public void store_rack_item_with_element_reference() {
		
		RackSettings rack = newRackSettings()
							.withRackId(RACK_ID)
							.withRackName(RACK_NAME)
							.build();
		transaction(()->{
			boolean created = service.storeRack(rack);
			assertTrue(created);
		});
		
		RackItemData item = newRackItemData()
							.withElementId(ELEMENT_ID)
							.withElementName(ELEMENT_NAME)
							.withPosition(1)
							.build();
		
		transaction(()->{
			service.storeRackItem(RACK_ID, item);
		});
		
		RackItemData update = newRackItemData()
						  	  .withElementId(ELEMENT_ID)
						  	  .withElementName(ELEMENT_NAME)
						  	  .withPosition(2)
						  	  .build();

		transaction(()->{
			service.storeRackItem(RACK_ID, update);
		});
		
		transaction(()->{
			RackItems rackItems = service.getRackItems(RACK_ID);
			RackItemData reloaded = rackItems.getItems().get(0);
			assertEquals(GROUP_ID,reloaded.getGroupId());
			assertEquals(GROUP_TYPE,reloaded.getGroupType());
			assertEquals(GROUP_NAME,reloaded.getGroupName());
			assertEquals(ELEMENT_ID,reloaded.getElementId());
			assertEquals(ELEMENT_NAME,reloaded.getElementName());
			assertEquals(ELEMENT_ALIAS,reloaded.getElementAlias());
			assertEquals(ELEMENT_ROLE,reloaded.getElementRole());
			assertEquals(PLATFORM_ID,reloaded.getPlatformId());
			assertEquals(PLATFORM_NAME,reloaded.getPlatformName());
			assertEquals(2,reloaded.getPosition());
		});
		
	}
	
	@Test
	public void remove_rack_item_element_reference() {
		
		RackSettings rack = newRackSettings()
							.withRackId(RACK_ID)
							.withRackName(RACK_NAME)
							.build();
		transaction(()->{
			boolean created = service.storeRack(rack);
			assertTrue(created);
		});
		
		RackItemData item = newRackItemData()
							.withRackItemId(RACK_ITEM_ID)
							.withElementId(ELEMENT_ID)
							.withElementName(ELEMENT_NAME)
							.withPosition(1)
							.build();
		
		transaction(()->{
			service.storeRackItem(RACK_ID, item);
		});
		
		RackItemData update = newRackItemData()
							  .withRackItemId(RACK_ITEM_ID)
							  .withRackItemName("Cable Manager")
							  .withPosition(1)
							  .build();

		transaction(()->{
			service.storeRackItem(RACK_ID, update);
		});
		
		transaction(()->{
			RackItems rackItems = service.getRackItems(RACK_ID);
			RackItemData reloaded = rackItems.getItems().get(0);
			assertNull(reloaded.getGroupId());
			assertNull(reloaded.getGroupType());
			assertNull(reloaded.getGroupName());
			assertNull(reloaded.getElementId());
			assertNull(reloaded.getElementName());
			assertNull(reloaded.getElementAlias());
			assertNull(reloaded.getElementRole());
			assertNull(reloaded.getPlatformId());
			assertNull(reloaded.getPlatformName());
			assertEquals(1,reloaded.getPosition());
			assertEquals("Cable Manager",reloaded.getRackItemName());
		});
		
	}
	
	@Test
	public void remove_rack_item_with_element_reference_by_id() {
		
		RackSettings rack = newRackSettings()
							.withRackId(RACK_ID)
							.withRackName(RACK_NAME)
							.build();
		transaction(()->{
			boolean created = service.storeRack(rack);
			assertTrue(created);
		});
		
		RackItemData item = newRackItemData()
							.withElementId(ELEMENT_ID)
							.withElementName(ELEMENT_NAME)
							.withPosition(1)
							.build();
		
		transaction(()->{
			service.storeRackItem(RACK_ID, item);
		});
		
		transaction(()->{
			ArgumentCaptor<Message> messageCatpor = ArgumentCaptor.forClass(Message.class);
			doNothing().when(messages).add(messageCatpor.capture());
			service.removeRackItem(RACK_ID, 1);
			Message message = messageCatpor.getValue();
			assertThat(message.getReason(),is(IVT0805I_RACK_ITEM_REMOVED.getReasonCode()));
		});
		
	}
	
	@Test
	public void remove_rack_item_with_element_reference_by_name() {
		
		RackSettings rack = newRackSettings()
							.withRackId(RACK_ID)
							.withRackName(RACK_NAME)
							.build();
		transaction(()->{
			boolean created = service.storeRack(rack);
			assertTrue(created);
		});
		
		RackItemData item = newRackItemData()
							.withElementId(ELEMENT_ID)
							.withElementName(ELEMENT_NAME)
							.withPosition(1)
							.build();
		
		transaction(()->{
			service.storeRackItem(RACK_ID, item);
		});
		
		transaction(()->{
			ArgumentCaptor<Message> messageCatpor = ArgumentCaptor.forClass(Message.class);
			doNothing().when(messages).add(messageCatpor.capture());
			service.removeRackItem(RACK_ID, 1);
			Message message = messageCatpor.getValue();
			assertThat(message.getReason(),is(IVT0805I_RACK_ITEM_REMOVED.getReasonCode()));
		});
		
	}
	
	
	@Test
	public void move_rack_item_with_element_reference() {
		
		RackSettings rack = newRackSettings()
							.withRackId(RACK_ID)
							.withRackName(RACK_NAME)
							.build();
		transaction(()->{
			boolean created = service.storeRack(rack);
			assertTrue(created);
		});
		
		RackItemData item = newRackItemData()
							.withRackItemId(RACK_ITEM_ID)
							.withElementId(ELEMENT_ID)
							.withElementName(ELEMENT_NAME)
							.withPosition(1)
							.build();
		
		transaction(()->{
			service.storeRackItem(RACK_ID, item);
		});
		
		RackItemData moved = newRackItemData()
							 .withRackItemId(RACK_ITEM_ID)
							 .withElementId(ELEMENT_ID)
							 .withElementName(ELEMENT_NAME)
							 .withPosition(25)
							 .build();

		transaction(()->{
			service.storeRackItem(RACK_ID, moved);
		});
		
		transaction(()->{
			RackItems items = service.getRackItems(RACK_ID);
			// Make sure that the old item was removed. An element cannot be installed in two slots at a time.
			assertThat(items.getItems(),hasSizeOf(1));
		});
		
	}
	
	
	@Test
	public void attempt_to_remove_unknown_rack_item_does_nothing() {
		RackSettings rack = newRackSettings()
							.withRackId(RACK_ID)
							.withRackName(RACK_NAME)
							.withAdministrativeState(ACTIVE)
							.build();

		transaction(()->{
			boolean created = service.storeRack(rack);
			assertTrue(created);
		});
		reset(messages);
		transaction(()->{
			service.removeRackItem(RACK_ID, 10);
			service.removeRackItem(RACK_NAME, 10);
			verifyZeroInteractions(messages);
		});
		
	}
	
	@Test
	public void cannot_remove_rack_with_rack_items_by_rack_id() {
		RackSettings rack = newRackSettings()
							.withRackId(RACK_ID)
							.withRackName(RACK_NAME)
							.build();
		transaction(()->{
			boolean created = service.storeRack(rack);
			assertTrue(created);
		});
		
		RackItemData item = newRackItemData()
							.withRackItemName("Cable Manager")
							.withPosition(1)
							.build();
		
		transaction(()->{
			service.storeRackItem(RACK_ID, item);
		});
		
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT0803E_RACK_NOT_REMOVABLE));
		transaction(()->{
			service.removeRack(RACK_ID);
		});
	}
	
	@Test
	public void cannot_remove_rack_with_rack_items_by_rack_name() {
		RackSettings rack = newRackSettings()
							.withRackId(RACK_ID)
							.withRackName(RACK_NAME)
							.build();
		transaction(()->{
			boolean created = service.storeRack(rack);
			assertTrue(created);
		});
		
		RackItemData item = newRackItemData()
							.withRackItemName("Cable Manager")
							.withPosition(1)
							.build();
		
		transaction(()->{
			service.storeRackItem(RACK_ID, item);
		});
		
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT0803E_RACK_NOT_REMOVABLE));
		transaction(()->{
			service.removeRack(RACK_NAME);
		});
	}
	
	
	@Test
	public void force_remove_rack_with_rack_items_by_rack_id() {
		RackSettings rack = newRackSettings()
							.withRackId(RACK_ID)
							.withRackName(RACK_NAME)
							.build();
		transaction(()->{
			boolean created = service.storeRack(rack);
			assertTrue(created);
		});
		
		RackItemData item = newRackItemData()
							.withRackItemName("Cable Manager")
							.withPosition(1)
							.build();
		
		transaction(()->{
			service.storeRackItem(RACK_ID, item);
		});
		
		ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
		doNothing().when(messages).add(messageCaptor.capture());
		
		transaction(()->{
			service.forceRemoveRack(RACK_ID);
			Message message = messageCaptor.getValue();
			assertThat(message.getReason(),is(IVT0802I_RACK_REMOVED.getReasonCode()));
		});
	}
	
	@Test
	public void force_remove_rack_with_rack_items_by_rack_name() {
		RackSettings rack = newRackSettings()
							.withRackId(RACK_ID)
							.withRackName(RACK_NAME)
							.build();
		transaction(()->{
			boolean created = service.storeRack(rack);
			assertTrue(created);
		});
		
		RackItemData item = newRackItemData()
							.withRackItemName("Cable Manager")
							.withPosition(1)
							.build();
		
		transaction(()->{
			service.storeRackItem(RACK_ID, item);
		});
		
		ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
		doNothing().when(messages).add(messageCaptor.capture());
		
		transaction(()->{
			service.forceRemoveRack(RACK_ID);
			Message message = messageCaptor.getValue();
			assertThat(message.getReason(),is(IVT0802I_RACK_REMOVED.getReasonCode()));
		});
	}
	
	@Test
	public void get_element_rack_item_by_rack_id() {
	
		RackSettings rack = newRackSettings()
						    .withAscending(true)
						    .withAssetId("asset-123")
						    .withDescription("Unit test rack")
						    .withLocation("Unit test location")
						    .withRackId(RACK_ID)
						    .withRackName(RACK_NAME)
						    .withAdministrativeState(ACTIVE)
						    .withRackType("Unit test rack type")
						    .withSerialNumber("X-1234-TEST")
						    .withUnits(42)
						    .build();
		
		transaction(()->{
			boolean created = service.storeRack(rack);
			assertTrue(created);
		});

		RackItemData item = newRackItemData()
							.withElementId(ELEMENT_ID)
							.withElementName(ELEMENT_NAME)
							.withPosition(1)
							.build();

		transaction(()->{
			service.storeRackItem(RACK_ID, item);
		});
		
		transaction(()->{
			RackItem reloaded = service.getRackItem(RACK_ID, 1);
			assertEquals(RACK_ID, reloaded.getRackId());
			assertEquals(RACK_NAME, reloaded.getRackName());
			assertTrue(reloaded.isAscending());
			assertEquals(1,reloaded.getItem().getPosition());
			assertEquals(GROUP_ID,reloaded.getItem().getGroupId());
			assertEquals(GROUP_TYPE,reloaded.getItem().getGroupType());
			assertEquals(GROUP_NAME,reloaded.getItem().getGroupName());
			assertEquals(ELEMENT_ID,reloaded.getItem().getElementId());
			assertEquals(ELEMENT_NAME,reloaded.getItem().getElementName());
			assertEquals(ELEMENT_ALIAS,reloaded.getItem().getElementAlias());
			assertEquals(ELEMENT_ROLE,reloaded.getItem().getElementRole());
			assertEquals(PLATFORM_ID,reloaded.getItem().getPlatformId());
			assertEquals(PLATFORM_NAME,reloaded.getItem().getPlatformName());
		});
		
	}

	
	@Test
	public void get_element_rack_item_by_rack_name() {
	
		RackSettings rack = newRackSettings()
						    .withAscending(true)
						    .withAssetId("asset-123")
						    .withDescription("Unit test rack")
						    .withLocation("Unit test location")
						    .withRackId(RACK_ID)
						    .withRackName(RACK_NAME)
						    .withAdministrativeState(ACTIVE)
						    .withRackType("Unit test rack type")
						    .withSerialNumber("X-1234-TEST")
						    .withUnits(42)
						    .build();
		
		transaction(()->{
			boolean created = service.storeRack(rack);
			assertTrue(created);
		});

		RackItemData item = newRackItemData()
							.withElementId(ELEMENT_ID)
							.withElementName(ELEMENT_NAME)
							.withPosition(1)
							.build();

		transaction(()->{
			service.storeRackItem(RACK_NAME, item);
		});
		
		transaction(()->{
			RackItem reloaded = service.getRackItem(RACK_NAME, 1);
			assertEquals(RACK_ID, reloaded.getRackId());
			assertEquals(RACK_NAME, reloaded.getRackName());
			assertTrue(reloaded.isAscending());
			assertEquals(1,reloaded.getItem().getPosition());
			assertEquals(GROUP_ID,reloaded.getItem().getGroupId());
			assertEquals(GROUP_TYPE,reloaded.getItem().getGroupType());
			assertEquals(GROUP_NAME,reloaded.getItem().getGroupName());
			assertEquals(ELEMENT_ID,reloaded.getItem().getElementId());
			assertEquals(ELEMENT_NAME,reloaded.getItem().getElementName());
			assertEquals(ELEMENT_ALIAS,reloaded.getItem().getElementAlias());
			assertEquals(ELEMENT_ROLE,reloaded.getItem().getElementRole());
			assertEquals(PLATFORM_ID,reloaded.getItem().getPlatformId());
			assertEquals(PLATFORM_NAME,reloaded.getItem().getPlatformName());
		});
		
	}
	
	
	@Test
	public void get_descriptive_rack_item_by_rack_id() {
	
		RackSettings rack = newRackSettings()
						    .withAscending(true)
						    .withAssetId("asset-123")
						    .withDescription("Unit test rack")
						    .withLocation("Unit test location")
						    .withRackId(RACK_ID)
						    .withRackName(RACK_NAME)
						    .withAdministrativeState(ACTIVE)
						    .withRackType("Unit test rack type")
						    .withSerialNumber("X-1234-TEST")
						    .withUnits(42)
						    .build();
		
		transaction(()->{
			boolean created = service.storeRack(rack);
			assertTrue(created);
		});

		RackItemData item = newRackItemData()
							.withRackItemName("Cable Manager")
							.withPosition(1)
							.build();

		transaction(()->{
			service.storeRackItem(RACK_ID, item);
		});
		
		transaction(()->{
			RackItem reloaded = service.getRackItem(RACK_ID, 1);
			assertEquals(RACK_ID, reloaded.getRackId());
			assertEquals(RACK_NAME, reloaded.getRackName());
			assertTrue(reloaded.isAscending());
			assertEquals(1,reloaded.getItem().getPosition());
			assertEquals("Cable Manager",reloaded.getItem().getRackItemName());
			assertNull(reloaded.getItem().getGroupId());
			assertNull(reloaded.getItem().getGroupType());
			assertNull(reloaded.getItem().getGroupName());
			assertNull(reloaded.getItem().getElementId());
			assertNull(reloaded.getItem().getElementName());
			assertNull(reloaded.getItem().getElementAlias());
			assertNull(reloaded.getItem().getElementRole());
			assertNull(reloaded.getItem().getPlatformId());
			assertNull(reloaded.getItem().getPlatformName());
		});
		
	}
	
	@Test
	public void get_descriptive_rack_item_by_rack_name() {
	
		RackSettings rack = newRackSettings()
						    .withAscending(true)
						    .withAssetId("asset-123")
						    .withDescription("Unit test rack")
						    .withLocation("Unit test location")
						    .withRackId(RACK_ID)
						    .withRackName(RACK_NAME)
						    .withAdministrativeState(ACTIVE)
						    .withRackType("Unit test rack type")
						    .withSerialNumber("X-1234-TEST")
						    .withUnits(42)
						    .build();
		
		transaction(()->{
			boolean created = service.storeRack(rack);
			assertTrue(created);
		});

		RackItemData item = newRackItemData()
							.withRackItemName("Cable Manager")
							.withPosition(1)
							.build();

		transaction(()->{
			service.storeRackItem(RACK_NAME, item);
		});
		
		transaction(()->{
			RackItem reloaded = service.getRackItem(RACK_NAME, 1);
			assertEquals(RACK_ID, reloaded.getRackId());
			assertEquals(RACK_NAME, reloaded.getRackName());
			assertTrue(reloaded.isAscending());
			assertEquals(1,reloaded.getItem().getPosition());
			assertEquals("Cable Manager",reloaded.getItem().getRackItemName());
			assertNull(reloaded.getItem().getGroupId());
			assertNull(reloaded.getItem().getGroupType());
			assertNull(reloaded.getItem().getGroupName());
			assertNull(reloaded.getItem().getElementId());
			assertNull(reloaded.getItem().getElementName());
			assertNull(reloaded.getItem().getElementAlias());
			assertNull(reloaded.getItem().getElementRole());
			assertNull(reloaded.getItem().getPlatformId());
			assertNull(reloaded.getItem().getPlatformName());
		});
		
	}
	@Test
	public void throws_EntityNotFoundException_when_adding_a_rack_with_unknown_facility() {
	    exception.expect(EntityNotFoundException.class);
	    exception.expect(reason(IVT0600E_FACILITY_NOT_FOUND));
    
	    RackSettings rack = newRackSettings()
                            .withRackId(RACK_ID)
                            .withRackName(RACK_NAME)
                            .withFacilityId(FACILITY_ID)
                            .build();
        
        transaction(()->{
            service.storeRack(rack);
        });
	}
	
	@Test
	public void create_rack_with_facility() {
        transaction(()->{
           Facility facility = new Facility(FACILITY_ID, 
                                            FACILITY_TYPE, 
                                            FACILITY_NAME);
           repository.add(facility);
        });
        
        RackSettings rack = newRackSettings()
                            .withAscending(true)
                            .withAssetId("asset-123")
                            .withDescription("Unit test rack")
                            .withLocation("Unit test location")
                            .withRackId(RACK_ID)
                            .withRackName(RACK_NAME)
                            .withAdministrativeState(ACTIVE)
                            .withFacilityId(FACILITY_ID)
                            .withRackType("Unit test rack type")
                            .withSerialNumber("X-1234-TEST")
                            .withUnits(42)
                            .build();
        
        transaction(()->{
            boolean created = service.storeRack(rack);
            assertTrue(created);
        });
        
        transaction(()->{
           RackSettings reloaded = service.getRackSettings(RACK_ID);
           assertEquals(FACILITY_ID,reloaded.getFacilityId());
           assertEquals(FACILITY_TYPE,reloaded.getFacilityType());
           assertEquals(FACILITY_NAME,reloaded.getFacilityName());
        });
        
	}
	
}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.commons.db.DatabaseService.prepare;
import static io.leitstand.inventory.model.ElementGroup.findElementGroupById;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.RackSettings.newRackSettings;
import static io.leitstand.inventory.service.ReasonCode.IVT0800E_RACK_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0802I_RACK_REMOVED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Message;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupRackService;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.RackName;
import io.leitstand.inventory.service.RackSettings;
import io.leitstand.testing.ut.LeitstandCoreMatchers;

public class ElementGroupRackServiceIT extends InventoryIT {
	
	private static final ElementGroupId GROUP_ID = ElementGroupId.randomGroupId();
	private static final ElementGroupType GROUP_TYPE = groupType("unittest");
	private static final ElementGroupName GROUP_NAME = groupName("ElementGroupRackServiceIT");
	private static final RackName RACK_NAME = RackName.valueOf("rack");
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	private ElementGroupRackService service;
	private Messages messages;
	
	@Before
	public void initTestEnvironment() {
		Repository repository = new Repository(getEntityManager());
		ElementGroupProvider groups = new ElementGroupProvider(repository);
		messages = mock(Messages.class);
		
		service = new DefaultElementGroupRackService(repository,
													 groups,
													 messages);
		
		transaction(()->{
			repository.addIfAbsent(findElementGroupById(GROUP_ID),
								   () -> new ElementGroup(GROUP_ID, GROUP_TYPE, GROUP_NAME));
		});
	}
	
	@After
	public void removeRacks() {
		getDatabase().executeUpdate(prepare("DELETE FROM inventory.elementgroup_rack"));
	}
	
	@Test
	public void create_rack_for_group_identified_by_id() {
		RackSettings rack = newRackSettings()
							.withRackName(RACK_NAME)
							.withLocation("location")
							.withDescription("description")
							.withUnits(48) 
							.build();
		
		transaction(() -> {
			boolean created = service.storeRack(GROUP_ID, RACK_NAME, rack);
			assertTrue(created);
		});
		
		transaction(() -> {
			RackSettings reloaded = service.getRack(GROUP_ID, RACK_NAME).getRack();
			assertEquals(rack,reloaded);
		});
	}
	
	
	@Test
	public void create_rack_for_group_identified_by_name() {
		RackSettings rack = newRackSettings()
							.withRackName(RACK_NAME)
							.withLocation("location")
							.withDescription("description")
							.withUnits(48) 
							.build();
			
		transaction(() -> {
			boolean created = service.storeRack(GROUP_TYPE, GROUP_NAME, RACK_NAME, rack);
			assertTrue(created);
		});
			
		transaction(() -> {
			RackSettings reloaded = service.getRack(GROUP_TYPE, GROUP_NAME, RACK_NAME).getRack();
			assertEquals(rack,reloaded);
		});
		
	}
	
	@Test
	public void update_rack_for_group_identified_by_id() {
		transaction(() -> {
			RackSettings rack = newRackSettings()
								.withRackName(RACK_NAME)
								.build();
			boolean created = service.storeRack(GROUP_ID, RACK_NAME, rack);
			assertTrue(created);
		});
		
		RackSettings rack = newRackSettings()
							.withRackName(RACK_NAME)
							.withLocation("location")
							.withDescription("description")
							.withUnits(48) 
							.build();
	
		transaction(() -> {
			boolean created = service.storeRack(GROUP_ID, RACK_NAME, rack);
			assertFalse(created);
		});
		
		transaction(() -> {
			RackSettings reloaded = service.getRack(GROUP_ID, RACK_NAME).getRack();
			assertEquals(rack,reloaded);
		});
	}
	

	@Test
	public void update_rack_for_group_identified_by_name() {
		transaction(() -> {
			RackSettings rack = newRackSettings()
								.withRackName(RACK_NAME)
								.build();
			boolean created = service.storeRack(GROUP_TYPE, GROUP_NAME, RACK_NAME, rack);
			assertTrue(created);
		});
		
		RackSettings rack = newRackSettings()
							.withRackName(RACK_NAME)
							.withLocation("location")
							.withDescription("description")
							.withUnits(48) 
							.build();
	
		transaction(() -> {
			boolean created = service.storeRack(GROUP_TYPE, GROUP_NAME, RACK_NAME, rack);
			assertFalse(created);
		});
		
		transaction(() -> {
			RackSettings reloaded = service.getRack(GROUP_TYPE, GROUP_NAME, RACK_NAME).getRack();
			assertEquals(rack,reloaded);
		});
	}
	
	
	@Test
	public void remove_rack_for_group_identified_by_id() {
		RackSettings rack = newRackSettings()
							.withRackName(RACK_NAME)
							.withLocation("location")
							.withDescription("description")
							.withUnits(48) 
							.build();
		
		transaction(() -> {
			boolean created = service.storeRack(GROUP_ID, RACK_NAME, rack);
			assertTrue(created);
		});
		
		transaction(() -> {
			ArgumentCaptor<Message> message = ArgumentCaptor.forClass(Message.class);
			doNothing().when(messages).add(message.capture());
			service.removeRack(GROUP_ID, RACK_NAME);
			assertEquals(IVT0802I_RACK_REMOVED.getReasonCode(),message.getValue().getReason());
		});
	}
	
	
	@Test
	public void remove_rack_for_group_identified_by_name() {
		RackSettings rack = newRackSettings()
							.withRackName(RACK_NAME)
							.withLocation("location")
							.withDescription("description")
							.withUnits(48) 
							.build();
			
		transaction(() -> {
			boolean created = service.storeRack(GROUP_TYPE, GROUP_NAME, RACK_NAME, rack);
			assertTrue(created);
		});
			
		transaction(() -> {
			ArgumentCaptor<Message> message = ArgumentCaptor.forClass(Message.class);
			doNothing().when(messages).add(message.capture());
			service.removeRack(GROUP_ID, RACK_NAME);
			assertEquals(IVT0802I_RACK_REMOVED.getReasonCode(),message.getValue().getReason());
		});
		
	}
	
	
	@Test
	public void throws_EntityNotFoundException_when_rack_does_not_exist_for_group_identified_by_id() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(LeitstandCoreMatchers.reason(IVT0800E_RACK_NOT_FOUND));
		
		transaction(()->{
			service.getRack(GROUP_ID, RACK_NAME);
		});
	}
	
	@Test
	public void throws_EntityNotFoundException_when_rack_does_not_exist_for_group_identified_by_name() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(LeitstandCoreMatchers.reason(IVT0800E_RACK_NOT_FOUND));
		
		transaction(()->{
			service.getRack(GROUP_TYPE, GROUP_NAME, RACK_NAME);
		});
	}
	
	
}

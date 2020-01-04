/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.model.ElementGroupSettingsMother.newTestElementGroup;
import static io.leitstand.inventory.model.ElementGroupSettingsMother.updateSettings;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupSettings;

public class StoreElementGroupIT extends InventoryIT{

	private ElementGroupManager service;
	private ElementGroupProvider groups;
	private Messages messages;
	
	@Before
	public void setup_service(){
		Repository repository = new Repository(getEntityManager());
		groups = new ElementGroupProvider(repository);
		messages = mock(Messages.class);
		this.service = new ElementGroupManager(repository, getDatabase(),messages);
	}
	
	@Test
	public void store_new_group(){
		ElementGroupSettings settings = newTestElementGroup();
		transaction(()->{
			service.createElementGroup(settings);
		});
		transaction(()->{
			ElementGroupSettings created = service.getGroupSettings(groups.fetchElementGroup(settings.getGroupId()));
			assertEquals(settings,created);
			assertNotSame(settings,created);
		});
	}
	
	@Test
	public void can_repeatedly_store_the_same_group(){
		ElementGroupSettings settings = newTestElementGroup();
		transaction(()->{
			service.createElementGroup(settings);
		});
		transaction(()->{
			ElementGroup group = groups.fetchElementGroup(settings.getGroupId());
			service.storeElementGroupSettings(group,settings);
			commitTransaction();
			beginTransaction();
			group = groups.fetchElementGroup(settings.getGroupId());
			ElementGroupSettings created = service.getGroupSettings(group);
			assertEquals(settings,created);
			assertNotSame(settings,created);
		});
	}
		
	
	@Test
	public void update_group_name(){
		transaction(()->{
			ElementGroupSettings set = newTestElementGroup(); 
			beginTransaction();
			service.createElementGroup(set);
			commitTransaction();
			
			beginTransaction();
			ElementGroup group = groups.fetchElementGroup(set.getGroupId());
			set = updateSettings(set).withGroupName(new ElementGroupName("new_name")).build();
			service.storeElementGroupSettings(group,set);
			commitTransaction();
			
			beginTransaction();
			group = groups.fetchElementGroup(set.getGroupId());
			ElementGroupSettings updated = service.getGroupSettings(group);
			assertEquals(new ElementGroupName("new_name"),updated.getGroupName());
			commitTransaction();
		});
	}
	
}
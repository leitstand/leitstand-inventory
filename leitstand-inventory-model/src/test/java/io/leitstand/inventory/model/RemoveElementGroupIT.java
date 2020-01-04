/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;


import static io.leitstand.inventory.model.ElementGroupSettingsMother.newTestElementGroup;
import static io.leitstand.inventory.service.ReasonCode.IVT0100E_GROUP_NOT_FOUND;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementGroupSettings;


public class RemoveElementGroupIT extends InventoryIT {

	private ElementGroupManager service;
	private ElementGroupProvider groups;
	private Messages messages;
	
	@Before
	public void initTestEnvironment(){
		Repository repository = new Repository(getEntityManager());
		groups = new ElementGroupProvider(repository);
		messages = mock(Messages.class);
		this.service = new ElementGroupManager(repository,getDatabase(),messages);
	}
	
	@Test
	public void can_remove_empty_group() throws Exception{
		ElementGroupSettings a = newTestElementGroup();
		service.createElementGroup(a);
		transaction(()->{
			ElementGroup createdElementGroup = groups.fetchElementGroup(a.getGroupId());
			ElementGroupSettings created = service.getGroupSettings(createdElementGroup);
			assertNotNull(created);
			assertEquals(a,created);
			service.removeElementGroup(createdElementGroup);
		});
		transaction(()->{
			try{
				groups.fetchElementGroup(a.getGroupId());
				fail("Exception expected!");
			} catch (EntityNotFoundException e){
				assertEquals(IVT0100E_GROUP_NOT_FOUND,e.getReason());
			}
		});
		
	}
}

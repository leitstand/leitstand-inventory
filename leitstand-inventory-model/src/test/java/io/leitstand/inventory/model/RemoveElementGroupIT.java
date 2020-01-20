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

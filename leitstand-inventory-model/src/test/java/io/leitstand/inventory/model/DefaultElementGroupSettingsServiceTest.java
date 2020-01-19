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
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupSettings;
import io.leitstand.inventory.service.ElementGroupSettingsService;

public class DefaultElementGroupSettingsServiceTest {

	private ElementGroupManager inventory;
	private ElementGroupProvider groups;
	
	private ElementGroupSettingsService service;
	
	@Before
	public void initTestEnvironment() {
		inventory = mock(ElementGroupManager.class);
		groups = mock(ElementGroupProvider.class);
		service = new DefaultElementGroupSettingsService(inventory, groups);
	}
	
	@Test
	public void create_new_group_if_no_group_with_specified_groupid_exists() {
		ElementGroupSettings settings = newTestElementGroup();

		boolean created = service.storeElementGroupSettings(settings);
		
		assertTrue(created);
		verify(inventory).createElementGroup(settings);
		verify(groups).tryFetchElementGroup(settings.getGroupId());
		verifyNoMoreInteractions(inventory,groups);
	}
	
	
	@Test
	public void update_existing_group() {
		ElementGroup group = mock(ElementGroup.class);
		ElementGroupSettings settings = newTestElementGroup();
		when(groups.tryFetchElementGroup(settings.getGroupId())).thenReturn(group);
		
		boolean created = service.storeElementGroupSettings(settings);
		
		assertFalse(created);
		verify(inventory).storeElementGroupSettings(group, settings);
		verify(groups).tryFetchElementGroup(settings.getGroupId());
		verifyNoMoreInteractions(inventory,groups);
	}
	
	@Test
	public void attempt_to_remove_a_non_existent_group_raises_no_error() {
		ElementGroupId groupId = randomGroupId();
		
		service.remove(groupId);
		
		verify(groups).tryFetchElementGroup(groupId);
		verify(inventory,never()).removeElementGroup(any(ElementGroup.class));
		verifyNoMoreInteractions(inventory,groups);

	}
	
	@Test
	public void remove_existing_group_identified_by_group_id() {
		ElementGroup group = mock(ElementGroup.class);
		ElementGroupSettings settings = newTestElementGroup();
		when(groups.tryFetchElementGroup(settings.getGroupId())).thenReturn(group);
		
		service.remove(settings.getGroupId());
		
		verify(groups).tryFetchElementGroup(settings.getGroupId());
		verify(inventory).removeElementGroup(group);
		verifyNoMoreInteractions(inventory,groups);
	}
	
	@Test
	public void remove_existing_group_identified_by_group_name() {
		ElementGroup group = mock(ElementGroup.class);
		ElementGroupSettings settings = newTestElementGroup();
		when(groups.tryFetchElementGroup(settings.getGroupType(), settings.getGroupName())).thenReturn(group);
		
		service.remove(settings.getGroupType(),settings.getGroupName());
				
		verify(groups).tryFetchElementGroup(settings.getGroupType(), settings.getGroupName());
		verify(inventory).removeElementGroup(group);
		verifyNoMoreInteractions(inventory,groups);
	}
	
}

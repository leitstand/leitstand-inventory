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

import static io.leitstand.inventory.model.ElementGroup.findElementGroupByName;
import static io.leitstand.inventory.model.ElementRole.findRoleByName;
import static io.leitstand.inventory.service.AdministrativeState.ACTIVE;
import static io.leitstand.inventory.service.AdministrativeState.NEW;
import static io.leitstand.inventory.service.AdministrativeState.RETIRED;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupSettings.newElementGroupSettings;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.OperationalState.DOWN;
import static io.leitstand.inventory.service.OperationalState.MAINTENANCE;
import static io.leitstand.inventory.service.OperationalState.UP;
import static io.leitstand.inventory.service.Plane.DATA;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.hasSizeOf;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.isEmptyCollection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupService;
import io.leitstand.inventory.service.ElementGroupSettings;
import io.leitstand.inventory.service.ElementGroupStatistics;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.ElementRoleName;

public class ElementGroupServiceIT extends InventoryIT {
	
	private static final ElementGroupType GROUP_TYPE = groupType("unittest");
	private static final ElementGroupId   GROUP_ID 	 = randomGroupId();
	private static final ElementGroupName GROUP_NAME = groupName(ElementGroupServiceIT.class.getSimpleName());
	private static final ElementRoleName  ROLE_NAME  = elementRoleName(ElementGroupServiceIT.class.getSimpleName());
	
	
	private ElementGroupService service;
	private ElementGroupManager manager;
	private Repository repository;

	@Before
	public void initTestEnvironment() {
		this.repository = new Repository(getEntityManager());
		manager = new ElementGroupManager(repository,
				 						  getDatabase(),
				 						  mock(Messages.class));
		service = new DefaultElementGroupService(manager);
		
		transaction(() -> {
			ElementGroup group = repository.addIfAbsent(findElementGroupByName(GROUP_TYPE, GROUP_NAME),
														() -> new ElementGroup(GROUP_ID, GROUP_TYPE, GROUP_NAME));



			
		});
		
	}
	
	@Test
	public void find_group() {
		List<ElementGroupSettings> groups = service.findGroups(GROUP_TYPE, GROUP_NAME.toString(), 0, 100);
		assertThat(groups,hasSizeOf(1));
		assertEquals(GROUP_ID,groups.get(0).getGroupId());
		assertEquals(GROUP_TYPE,groups.get(0).getGroupType());
		assertEquals(GROUP_NAME,groups.get(0).getGroupName());
		
	}
	
	@Test
	public void get_empty_list_if_no_matching_group_name_exist() {
		List<ElementGroupSettings> groups = service.findGroups(GROUP_TYPE, "unknown group",	0, 100);
		assertThat(groups, isEmptyCollection());
	}
	
	@Test
	public void get_empty_list_if_no_matching_group_type_exist() {
		List<ElementGroupSettings> groups = service.findGroups(groupType("UNKNOWN"), "group_filter_a",	0, 100);
		assertThat(groups,isEmptyCollection());
	}
	
	@Test
	public void filter_groups_by_name() {
		// Add a few element groups.
		transaction(() -> {
			manager.createElementGroup(newElementGroupSettings()
									   .withGroupType(GROUP_TYPE)
									   .withGroupName(groupName("group_filter_a"))
									   .build());
			manager.createElementGroup(newElementGroupSettings()
									   .withGroupType(GROUP_TYPE)
					  				   .withGroupName(groupName("group_filter_b"))
					  				   .build());
		});
		
		transaction(() -> {
			List<ElementGroupSettings> groups = service.findGroups(GROUP_TYPE, "group_filter_a", 0, 1);
			assertThat(groups,hasSizeOf(1));
			assertEquals(GROUP_TYPE,groups.get(0).getGroupType());
			assertEquals(groupName("group_filter_a"),groups.get(0).getGroupName());
		});
		
	}
	
	@Test
	public void read_element_group_statistics() {

		
		transaction(() -> {
			ElementGroup group = repository.execute(findElementGroupByName(GROUP_TYPE, GROUP_NAME));
			
			ElementRole role = repository.addIfAbsent(findRoleByName(ROLE_NAME),
													  () -> {
														  ElementRole newRole = new ElementRole(ROLE_NAME, DATA );
														  newRole.setManageable(true);
														  return newRole;
													  });
			
			Element element = new Element(group,role,randomElementId(), elementName(getClass().getSimpleName()+".A" ));
			element.setAdministrativeState(ACTIVE);
			element.setOperationalState(DOWN);
			repository.add(element);
			element = new Element(group,role,randomElementId(), elementName(getClass().getSimpleName()+".B" ));
			element.setAdministrativeState(ACTIVE);
			element.setOperationalState(UP);
			repository.add(element);
			element = new Element(group,role,randomElementId(), elementName(getClass().getSimpleName()+".C" ));
			element.setAdministrativeState(ACTIVE);
			element.setOperationalState(MAINTENANCE);
			repository.add(element);
			element = new Element(group,role,randomElementId(), elementName(getClass().getSimpleName()+".D" ));
			element.setAdministrativeState(NEW);
			repository.add(element);
			element = new Element(group,role,randomElementId(), elementName(getClass().getSimpleName()+".E" ));
			element.setAdministrativeState(NEW);
			repository.add(element);
			element = new Element(group,role,randomElementId(), elementName(getClass().getSimpleName()+".F" ));
			element.setAdministrativeState(RETIRED);
			repository.add(element);

		});
		
		transaction(()->{
			ElementGroupStatistics stats = service.getGroupStatistics(GROUP_TYPE, null).get(0);
			assertEquals(1,stats.getActiveElements().get(UP).intValue());
			assertEquals(1,stats.getActiveElements().get(DOWN).intValue());
			assertEquals(1,stats.getActiveElements().get(MAINTENANCE).intValue());
			assertEquals(2,stats.getNewElements());
			assertEquals(1,stats.getRetiredElements());
		});
	}
	
}

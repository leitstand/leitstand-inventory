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

import static io.leitstand.commons.model.ObjectUtil.asSet;
import static io.leitstand.inventory.model.Element.findElementById;
import static io.leitstand.inventory.model.Element.findElementByName;
import static io.leitstand.inventory.model.ElementGroup.findElementGroupById;
import static io.leitstand.inventory.model.ElementRole.findRoleByName;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementManagementInterface.newElementManagementInterface;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.ReasonCode.IVT0303E_ELEMENT_NOT_REMOVABLE;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.isEmptyList;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;

import javax.enterprise.event.Event;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.AdministrativeState;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ElementService;
import io.leitstand.inventory.service.ElementSettings;
import io.leitstand.inventory.service.Plane;

public class ElementServiceIT extends InventoryIT {
	
	private static final ElementId ACTIVE_ELEMENT_ID = randomElementId();
	private static final ElementName ACTIVE_ELEMENT_NAME = elementName("active");
	private static final ElementId NEW_ELEMENT_ID = randomElementId();
	private static final ElementName NEW_ELEMENT_NAME = elementName("new");
	private static final ElementId RETIRED_ELEMENT_ID = randomElementId();
	private static final ElementName RETIRED_ELEMENT_NAME = elementName("retired");
	private static final ElementGroupId GROUP_ID = randomGroupId();
	private static final ElementGroupType GROUP_TYPE = groupType("unittest");
	private static final ElementGroupName GROUP_NAME = groupName("group");
	private static final ElementRoleName ROLE_NAME = elementRoleName("role");
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	private ElementService service;
	private Repository repository;

	@Before
	public void initTestEnvironment() {
		this.repository = new Repository(getEntityManager());
		ElementProvider elements = new ElementProvider(repository);
		ElementManager manager = new ElementManager(repository, 
													mock(Event.class),
													mock(Messages.class));
		
		service = new DefaultElementService(manager,
											elements);
		
		transaction(()->{
			ElementRole role = repository.addIfAbsent(findRoleByName(ROLE_NAME),
													  () -> new ElementRole(ROLE_NAME, Plane.DATA));
			ElementGroup group = repository.addIfAbsent(findElementGroupById(GROUP_ID), 
														() -> new ElementGroup(GROUP_ID,GROUP_TYPE,GROUP_NAME));
			
			repository.addIfAbsent(findElementById(ACTIVE_ELEMENT_ID), 
								   () -> {
									   	Element element = new Element(group,role,ACTIVE_ELEMENT_ID,ACTIVE_ELEMENT_NAME);
									   	element.setAdministrativeState(AdministrativeState.ACTIVE);
									   	element.setDescription("Active Element");
									   	element.setAssetId("asset-1");
									   	element.setTags(asSet("test-tag"));
									   	element.setSerialNumber("serial-123");
									   	element.setElementManagementInterfaces(asSet(newElementManagementInterface()
									   											     .withName("SSH")
									   											     .withHostname("192.168.0.1")
									   											     .build()));
									   	return element;
								   });

			repository.addIfAbsent(findElementById(NEW_ELEMENT_ID), 
					   () -> {
						   	Element element = new Element(group,role,NEW_ELEMENT_ID,NEW_ELEMENT_NAME);
						   	element.setAdministrativeState(AdministrativeState.NEW);
						   	element.setDescription("New Element");
						   	return element;
					   	});
		
			
			repository.addIfAbsent(findElementById(RETIRED_ELEMENT_ID), 
					   () -> {
						   	Element element = new Element(group,role,RETIRED_ELEMENT_ID,RETIRED_ELEMENT_NAME);
						   	element.setAdministrativeState(AdministrativeState.RETIRED);
						   	element.setDescription("Retired Element");
						   	return element;
					   });
			
		});
	}
	
	@Test
	public void return_empty_list_if_no_matching_elements_exist() {
		
		transaction(()-> {
			List<ElementSettings> elements = service.findElementsByName("unknown_name", 0, 100);
			assertThat(elements,isEmptyList());
		});

		transaction(()-> {
			List<ElementSettings> elements = service.findElementsByNameOrTag("unknown_name_or_tag", 0, 100);
			assertThat(elements,isEmptyList());
		});
		
		transaction(()-> {
			List<ElementSettings> elements = service.findElementsBySerialNumber("unknown_serial", 0, 100);
			assertThat(elements,isEmptyList());
		});
		
		
		transaction(()-> {
			List<ElementSettings> elements = service.findElementsByAssetId("unknown_assetid", 0, 100);
			assertThat(elements,isEmptyList());
		});
		
		
		transaction(()-> {
			List<ElementSettings> elements = service.findElementsByManagementIP("unknown_ip", 0, 100);
			assertThat(elements,isEmptyList());
		});
		
		
		transaction(()-> {
			List<ElementSettings> elements = service.findElementsByName("unknown_element", 0, 100);
			assertThat(elements,isEmptyList());
		});
	
	}
	
	@Test
	public void find_elements_by_name_pattern() {
		
		transaction(()->{
			List<ElementSettings> elements = service.findElementsByName("act.*", 0, 100);
			assertEquals(GROUP_ID, elements.get(0).getGroupId());
			assertEquals(GROUP_TYPE, elements.get(0).getGroupType());
			assertEquals(GROUP_NAME, elements.get(0).getGroupName());
			assertEquals(ACTIVE_ELEMENT_ID, elements.get(0).getElementId());
			assertEquals(ACTIVE_ELEMENT_NAME, elements.get(0).getElementName());
			assertEquals(ROLE_NAME, elements.get(0).getElementRole());
			assertEquals("Active Element", elements.get(0).getDescription());
		});
		
	}

	@Test
	public void find_elements_by_tag_pattern() {
		
		transaction(()->{
			List<ElementSettings> elements = service.findElementsByNameOrTag("test-tag", 0, 100);
			assertEquals(GROUP_ID, elements.get(0).getGroupId());
			assertEquals(GROUP_TYPE, elements.get(0).getGroupType());
			assertEquals(GROUP_NAME, elements.get(0).getGroupName());
			assertEquals(ACTIVE_ELEMENT_ID, elements.get(0).getElementId());
			assertEquals(ACTIVE_ELEMENT_NAME, elements.get(0).getElementName());
			assertEquals(ROLE_NAME, elements.get(0).getElementRole());
			assertEquals("Active Element", elements.get(0).getDescription());
		});
		
	}
	
	@Test
	public void find_elements_by_serial_number() {
		
		transaction(()->{
			List<ElementSettings> elements = service.findElementsBySerialNumber("serial-123", 0, 100);
			assertEquals(GROUP_ID, elements.get(0).getGroupId());
			assertEquals(GROUP_TYPE, elements.get(0).getGroupType());
			assertEquals(GROUP_NAME, elements.get(0).getGroupName());
			assertEquals(ACTIVE_ELEMENT_ID, elements.get(0).getElementId());
			assertEquals(ACTIVE_ELEMENT_NAME, elements.get(0).getElementName());
			assertEquals(ROLE_NAME, elements.get(0).getElementRole());
			assertEquals("Active Element", elements.get(0).getDescription());
			assertEquals("serial-123",elements.get(0).getSerialNumber());
		});
		
	}
	
	@Test
	public void find_elements_by_asset_id() {
		
		transaction(()->{
			List<ElementSettings> elements = service.findElementsByAssetId("asset-1", 0, 100);
			assertEquals(GROUP_ID, elements.get(0).getGroupId());
			assertEquals(GROUP_TYPE, elements.get(0).getGroupType());
			assertEquals(GROUP_NAME, elements.get(0).getGroupName());
			assertEquals(ACTIVE_ELEMENT_ID, elements.get(0).getElementId());
			assertEquals(ACTIVE_ELEMENT_NAME, elements.get(0).getElementName());
			assertEquals(ROLE_NAME, elements.get(0).getElementRole());
			assertEquals("Active Element", elements.get(0).getDescription());
			assertEquals("asset-1",elements.get(0).getAssetId());
		});
		
	}
	
	@Test
	public void find_elements_by_mgmt_IP() {
		
		transaction(()->{
			List<ElementSettings> elements = service.findElementsByManagementIP("192.168.*", 0, 100);
			assertEquals(GROUP_ID, elements.get(0).getGroupId());
			assertEquals(GROUP_TYPE, elements.get(0).getGroupType());
			assertEquals(GROUP_NAME, elements.get(0).getGroupName());
			assertEquals(ACTIVE_ELEMENT_ID, elements.get(0).getElementId());
			assertEquals(ACTIVE_ELEMENT_NAME, elements.get(0).getElementName());
			assertEquals(ROLE_NAME, elements.get(0).getElementRole());
			assertEquals("Active Element", elements.get(0).getDescription());
			assertEquals("asset-1",elements.get(0).getAssetId());
		});
		
	}
	
	
	@Test
	public void cannot_remove_active_element_by_id() {
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT0303E_ELEMENT_NOT_REMOVABLE));
		
		transaction(()->{
			service.removeElement(ACTIVE_ELEMENT_ID);
		});
	}

	@Test
	public void cannot_remove_active_element_by_name() {
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT0303E_ELEMENT_NOT_REMOVABLE));
		
		transaction(()->{
			service.removeElement(ACTIVE_ELEMENT_NAME);
		});
	}
	
	@Test
	public void remove_retired_element_by_id() {
		transaction(()->{
			service.removeElement(RETIRED_ELEMENT_ID);
		});
		
		transaction(() -> {
			assertNull(repository.execute(findElementById(RETIRED_ELEMENT_ID)));
		});
	}
	
	@Test
	public void remove_new_element_by_id() {
		transaction(()->{
			service.removeElement(NEW_ELEMENT_ID);
		});
		
		transaction(() -> {
			assertNull(repository.execute(findElementById(NEW_ELEMENT_ID)));
		});
	}
	
	@Test
	public void remove_retired_element_by_name() {
		transaction(()->{
			service.removeElement(RETIRED_ELEMENT_NAME);
		});
		
		transaction(() -> {
			assertNull(repository.execute(findElementByName(RETIRED_ELEMENT_NAME)));
		});
	}
	
	@Test
	public void remove_new_element_by_name() {
		transaction(()->{
			service.removeElement(NEW_ELEMENT_NAME);
		});
		
		transaction(() -> {
			assertNull(repository.execute(findElementByName(NEW_ELEMENT_NAME)));
		});
	}
	
	@Test
	public void cannot_force_remove_active_element_by_id() {
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT0303E_ELEMENT_NOT_REMOVABLE));
		
		transaction(()->{
			service.forceRemoveElement(ACTIVE_ELEMENT_ID);
		});
	}

	@Test
	public void cannot_force_remove_active_element_by_name() {
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT0303E_ELEMENT_NOT_REMOVABLE));
		
		transaction(()->{
			service.forceRemoveElement(ACTIVE_ELEMENT_NAME);
		});
	}
	
	@Test
	public void force_remove_retired_element_by_id() {
		transaction(()->{
			service.forceRemoveElement(RETIRED_ELEMENT_ID);
		});
		
		transaction(() -> {
			assertNull(repository.execute(findElementById(RETIRED_ELEMENT_ID)));
		});
	}
	
	@Test
	public void force_remove_new_element_by_id() {
		transaction(()->{
			service.forceRemoveElement(NEW_ELEMENT_ID);
		});
		
		transaction(() -> {
			assertNull(repository.execute(findElementById(NEW_ELEMENT_ID)));
		});
	}
	
	@Test
	public void force_remove_retired_element_by_name() {
		transaction(()->{
			service.forceRemoveElement(RETIRED_ELEMENT_NAME);
		});
		
		transaction(() -> {
			assertNull(repository.execute(findElementByName(RETIRED_ELEMENT_NAME)));
		});
	}
	
	@Test
	public void force_remove_new_element_by_name() {
		transaction(()->{
			service.removeElement(NEW_ELEMENT_NAME);
		});
		
		transaction(() -> {
			assertNull(repository.execute(findElementByName(NEW_ELEMENT_NAME)));
		});
	}
}

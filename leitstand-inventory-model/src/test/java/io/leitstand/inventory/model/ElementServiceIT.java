/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.model.Element.findElementById;
import static io.leitstand.inventory.model.Element.findElementByName;
import static io.leitstand.inventory.model.ElementGroup.findElementGroupById;
import static io.leitstand.inventory.model.ElementRole.findRoleByName;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
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
	private static final ElementName ACTIVE_ELEMENT_NAME = ElementName.elementName(ElementServiceIT.class.getSimpleName()+"_ACTIVE");
	private static final ElementId NEW_ELEMENT_ID = randomElementId();
	private static final ElementName NEW_ELEMENT_NAME = ElementName.elementName(ElementServiceIT.class.getSimpleName()+"_NEW");
	private static final ElementId RETIRED_ELEMENT_ID = randomElementId();
	private static final ElementName RETIRED_ELEMENT_NAME = ElementName.elementName(ElementServiceIT.class.getSimpleName()+"_RETIRED");
	private static final ElementGroupId GROUP_ID = randomGroupId();
	private static final ElementGroupType GROUP_TYPE = groupType("unittest");
	private static final ElementGroupName GROUP_NAME = groupName(ElementServiceIT.class.getSimpleName());
	private static final ElementRoleName ROLE_NAME = elementRoleName(ElementServiceIT.class.getSimpleName());
	
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
			List<ElementSettings> elements = service.findElements("unknown_elements", 0, 100);
			assertThat(elements,isEmptyList());
		});
	}
	
	@Test
	public void find_elements_by_name_pattern() {
		
		transaction(()->{
			List<ElementSettings> elements = service.findElements(".*_ACTIVE", 0, 100);
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
	
	
}

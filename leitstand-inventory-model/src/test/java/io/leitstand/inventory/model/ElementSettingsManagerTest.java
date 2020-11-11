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

import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.OperationalState.DOWN;
import static io.leitstand.inventory.service.OperationalState.UP;
import static io.leitstand.inventory.service.PlatformId.randomPlatformId;
import static io.leitstand.inventory.service.PlatformName.platformName;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.enterprise.event.Event;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.event.ElementMovedEvent;
import io.leitstand.inventory.event.ElementOperationalStateChangedEvent;
import io.leitstand.inventory.event.ElementRoleChangedEvent;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ElementSettings;
import io.leitstand.inventory.service.PlatformId;
import io.leitstand.inventory.service.PlatformName;

@RunWith(MockitoJUnitRunner.class)
public class ElementSettingsManagerTest {
	
	private static final ElementId ELEMENT_ID = randomElementId();
	private static final ElementName ELEMENT_NAME = elementName("element");
	private static final ElementRoleName ELEMENT_ROLE = elementRoleName("role");
	private static final ElementGroupId GROUP_ID = randomGroupId();
	private static final ElementGroupName GROUP_NAME = groupName("group");
	private static final PlatformId PLATFORM_ID = randomPlatformId();
	private static final PlatformName PLATFORM_NAME = platformName("platform");
	private static final ElementRoleName NEW_ELEMENT_ROLE = elementRoleName("new-role");
	private static final ElementGroupId NEW_GROUP_ID = randomGroupId();
	private static final ElementGroupName NEW_GROUP_NAME = groupName("new-group");

	
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private ElementSettings settings;
	
	@Mock
	private Element element;
	
	@Mock
	private ElementProvider elements;

	@Mock
	private ElementGroupProvider groups;
	
	@Mock
	private ElementRoleProvider roles;
	
	@Mock
	private PlatformProvider platforms;
	
	@Mock
	private Event event;
	
	@Mock
	private Messages messages;
	
	@Mock
	private Repository repository;
	
	@InjectMocks
	private ElementSettingsManager manager = new ElementSettingsManager();
	
	@Before
	public void initTestEnvironment() {
		when(settings.getElementId()).thenReturn(ELEMENT_ID);
	}
	
	@Test
	public void save_settings_throws_UnprocessableEntityException_when_attempting_to_change_element_id() {
		when(element.hasElementId(ELEMENT_ID)).thenReturn(false);
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));
		
		manager.storeElementSettings(element, settings);
		
	}
	
	@Test
	public void save_settings_fires_event_when_operational_state_changes() {
		when(settings.getElementId()).thenReturn(ELEMENT_ID);
		when(settings.getElementName()).thenReturn(ELEMENT_NAME);
		when(settings.getOperationalState()).thenReturn(UP);
		when(element.hasElementId(ELEMENT_ID)).thenReturn(true);
		when(element.setOperationalState(UP)).thenReturn(DOWN);
		when(element.getElementId()).thenReturn(ELEMENT_ID);
		when(element.getElementName()).thenReturn(ELEMENT_NAME);
		when(element.getElementRoleName()).thenReturn(ELEMENT_ROLE);
		when(element.getGroupName()).thenReturn(GROUP_NAME);
		when(element.setElementName(ELEMENT_NAME)).thenReturn(ELEMENT_NAME);
		when(element.getOperationalState()).thenReturn(UP);
		
		
		ArgumentCaptor<ElementOperationalStateChangedEvent> firedEvent = ArgumentCaptor.forClass(ElementOperationalStateChangedEvent.class);
		doNothing().when(event).fire(firedEvent.capture());
		
		manager.storeElementSettings(element, settings);
		ElementOperationalStateChangedEvent event = firedEvent.getValue();
		assertEquals(ELEMENT_ID,event.getElementId());
		assertEquals(ELEMENT_NAME,event.getElementName());
		assertEquals(GROUP_NAME,event.getGroupName());
		assertEquals(ELEMENT_ROLE,event.getElementRole());
		assertEquals(UP,event.getOperationalState());
		assertEquals(DOWN,event.getPreviousOperationalState());

	}
	
	@Test
	public void save_settings_fires_event_when_element_role_changes() {
		when(settings.getElementId()).thenReturn(ELEMENT_ID);
		when(settings.getElementName()).thenReturn(ELEMENT_NAME);
		when(settings.getElementRole()).thenReturn(NEW_ELEMENT_ROLE);
		when(element.hasElementId(ELEMENT_ID)).thenReturn(true);
		when(element.getElementId()).thenReturn(ELEMENT_ID);
		when(element.getElementName()).thenReturn(ELEMENT_NAME);
		when(element.getElementRoleName()).thenReturn(ELEMENT_ROLE).thenReturn(ELEMENT_ROLE).thenReturn(NEW_ELEMENT_ROLE);
		when(element.getGroupName()).thenReturn(GROUP_NAME);
		when(element.setElementName(ELEMENT_NAME)).thenReturn(ELEMENT_NAME);
		
		
		ArgumentCaptor<ElementRoleChangedEvent> firedEvent = ArgumentCaptor.forClass(ElementRoleChangedEvent.class);
		doNothing().when(event).fire(firedEvent.capture());
		
		manager.storeElementSettings(element, settings);
		ElementRoleChangedEvent event = firedEvent.getValue();
		assertEquals(ELEMENT_ID,event.getElementId());
		assertEquals(ELEMENT_NAME,event.getElementName());
		assertEquals(GROUP_NAME,event.getGroupName());
		assertEquals(NEW_ELEMENT_ROLE,event.getElementRole());
		assertEquals(ELEMENT_ROLE,event.getPreviousElementRole());
	}
	
	@Test
	public void save_settings_fires_event_when_element_group_changes() {
        ElementGroup group = mock(ElementGroup.class);
        when(group.getGroupId()).thenReturn(GROUP_ID);
        when(group.getGroupName()).thenReturn(GROUP_NAME);
        when(groups.fetchElementGroup(GROUP_ID)).thenReturn(group);
        
        
        ElementGroup newGroup = mock(ElementGroup.class);
        when(newGroup.getGroupId()).thenReturn(NEW_GROUP_ID);
        when(newGroup.getGroupName()).thenReturn(NEW_GROUP_NAME);
        when(groups.fetchElementGroup(NEW_GROUP_ID)).thenReturn(newGroup);
	    
	    when(settings.getElementId()).thenReturn(ELEMENT_ID);
        when(settings.getElementName()).thenReturn(ELEMENT_NAME);
        when(settings.getElementRole()).thenReturn(ELEMENT_ROLE);
        when(settings.getGroupId()).thenReturn(NEW_GROUP_ID);
        when(settings.getGroupName()).thenReturn(NEW_GROUP_NAME);
        when(element.hasElementId(ELEMENT_ID)).thenReturn(true);
        when(element.getElementId()).thenReturn(ELEMENT_ID);
        when(element.getElementName()).thenReturn(ELEMENT_NAME);
        when(element.getElementRoleName()).thenReturn(ELEMENT_ROLE);
        when(element.getGroup()).thenReturn(group);
        when(element.setElementName(ELEMENT_NAME)).thenReturn(ELEMENT_NAME);
        
        
        ArgumentCaptor<ElementMovedEvent> firedEvent = ArgumentCaptor.forClass(ElementMovedEvent.class);
        doNothing().when(event).fire(firedEvent.capture());
        
        manager.storeElementSettings(element, settings);
        ElementMovedEvent event = firedEvent.getValue();
        assertEquals(ELEMENT_ID,event.getElementId());
        assertEquals(ELEMENT_NAME,event.getElementName());
        assertEquals(GROUP_ID,event.getFrom().getGroupId());
        assertEquals(GROUP_NAME,event.getFrom().getGroupName());
        assertEquals(ELEMENT_ROLE,event.getElementRole());
        assertEquals(NEW_GROUP_ID,event.getTo().getGroupId());
        assertEquals(NEW_GROUP_NAME,event.getTo().getGroupName());
	}
	
	@Test
	public void save_settings_creates_platform_if_platform_is_unknown() {
        when(settings.getElementId()).thenReturn(ELEMENT_ID);
        when(settings.getElementName()).thenReturn(ELEMENT_NAME);
        when(settings.getElementRole()).thenReturn(ELEMENT_ROLE);
        when(settings.getPlatformId()).thenReturn(PLATFORM_ID);
        when(settings.getPlatformName()).thenReturn(PLATFORM_NAME);
        when(element.hasElementId(ELEMENT_ID)).thenReturn(true);
        when(element.getElementId()).thenReturn(ELEMENT_ID);
        when(element.getElementName()).thenReturn(ELEMENT_NAME);
        when(element.getElementRoleName()).thenReturn(ELEMENT_ROLE);
        when(element.getGroupName()).thenReturn(GROUP_NAME);
        
        manager.storeElementSettings(element, settings);
        
        verify(platforms).findOrCreatePlatform(PLATFORM_ID,PLATFORM_NAME,null);
	}
	
}

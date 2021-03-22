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
package io.leitstand.inventory.rs;

import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ReasonCode.IVT0103E_GROUP_NAME_ALREADY_IN_USE;
import static io.leitstand.testing.ut.Answers.ROLLBACK;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.UniqueKeyConstraintViolationException;
import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupSettings;
import io.leitstand.inventory.service.ElementGroupSettingsService;
import io.leitstand.inventory.service.ElementGroupType;

@RunWith(MockitoJUnitRunner.class)
public class ElementGroupSettingsResourceTest {
	
	private static final ElementGroupId    GROUP_ID   = randomGroupId();
	private static final ElementGroupType  GROUP_TYPE = groupType("type");
	private static final ElementGroupName  GROUP_NAME = groupName("group");
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private Messages messages;
	
	@Mock
	private ElementGroupSettingsService service;
	
	@InjectMocks
	private ElementGroupSettingsResource resource = new ElementGroupSettingsResource();
	
	@Test
	public void get_element_group_by_id() {
	    ElementGroupSettings group = mock(ElementGroupSettings.class);
	    when(service.getGroupSettings(GROUP_ID)).thenReturn(group);
	    assertEquals(group,resource.getGroupSettings(GROUP_ID));
	}

    @Test
    public void get_element_group_by_name() {
        ElementGroupSettings group = mock(ElementGroupSettings.class);
        when(service.getGroupSettings(GROUP_TYPE, GROUP_NAME)).thenReturn(group);
        assertEquals(group,resource.getGroupSettings(GROUP_TYPE, GROUP_NAME));
    }	
	
	@Test
	public void cannot_change_group_id() {
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));
		
		ElementGroupSettings group = mock(ElementGroupSettings.class);
		when(group.getGroupId()).thenReturn(GROUP_ID);
		
		resource.storeElementGroup(randomGroupId(), group);
	}
	
	@Test
	public void add_element_group_with_group_id() {
	    ElementGroupSettings group = mock(ElementGroupSettings.class);
		when(group.getGroupId()).thenReturn(GROUP_ID);
		when(service.storeElementGroupSettings(group)).thenReturn(true);
		
		assertEquals(201,resource.storeElementGroup(GROUP_ID,group).getStatus());
	}

    @Test
    public void add_element_group_with_group_name() {
        ElementGroupSettings group = mock(ElementGroupSettings.class);
        when(group.getGroupId()).thenReturn(GROUP_ID);
        when(group.getGroupType()).thenReturn(GROUP_TYPE);
        when(group.getGroupName()).thenReturn(GROUP_NAME);
        when(service.storeElementGroupSettings(group)).thenReturn(true);
        
        assertEquals(201,resource.storeElementGroup(GROUP_TYPE, GROUP_NAME, group).getStatus());
    }

    @Test
    public void report_group_name_uniqe_key_constraint_violation() {
        exception.expect(UniqueKeyConstraintViolationException.class);
        exception.expect(reason(IVT0103E_GROUP_NAME_ALREADY_IN_USE));
        
        ElementGroupSettings group = mock(ElementGroupSettings.class);
        when(group.getGroupId()).thenReturn(GROUP_ID);
        when(group.getGroupType()).thenReturn(GROUP_TYPE);
        when(group.getGroupName()).thenReturn(GROUP_NAME);
        
        when(service.storeElementGroupSettings(group)).then(ROLLBACK);
        when(service.getGroupSettings(GROUP_TYPE, GROUP_NAME)).thenReturn(group);
        
        resource.storeElementGroup(group);        
    }
    
    @Test
    public void remove_group_by_id() {
        resource.removeElementGroup(GROUP_ID);
        verify(service).removeElementGroup(GROUP_ID);
    }

    @Test
    public void remove_group_by_name() {
        resource.removeElementGroup(GROUP_TYPE, GROUP_NAME);
        verify(service).removeElementGroup(GROUP_TYPE,GROUP_NAME);
    }
	
}

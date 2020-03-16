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

import static io.leitstand.inventory.rs.Scopes.IVT;
import static io.leitstand.inventory.rs.Scopes.IVT_ELEMENT;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.security.auth.ReasonCode.AUT0002E_SCOPE_ACCESS_DENIED;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.AccessDeniedException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementService;
import io.leitstand.security.auth.UserContext;

@RunWith(MockitoJUnitRunner.class)
public class ElementResourceTest {
	
	private static final ElementId 	 ELEMENT_ID   = randomElementId();
	private static final ElementName ELEMENT_NAME = elementName("element");

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private Messages messages;
	
	@Mock
	private UserContext user;
	
	@Mock
	private ElementService service;
	
	@InjectMocks
	private ElementResource resource = new ElementResource();
	
	@Test
	public void settings_scope_is_not_allowed_to_force_remove_element_identified_by_id() {
		exception.expect(AccessDeniedException.class);
		exception.expect(reason(AUT0002E_SCOPE_ACCESS_DENIED));

		resource.removeElement(ELEMENT_ID, true);
		verify(service).forceRemoveElement(ELEMENT_ID);
	}
	
	@Test
	public void settings_scope_is_not_allowed_to_force_remove_element_identified_by_element() {
		exception.expect(AccessDeniedException.class);
		exception.expect(reason(AUT0002E_SCOPE_ACCESS_DENIED));

		resource.removeElement(ELEMENT_NAME, true);
		verify(service).forceRemoveElement(ELEMENT_NAME);
	}
	
	@Test
	public void force_remove_element_identified_by_id() {
		when(user.scopesIncludeOneOf(IVT,IVT_ELEMENT)).thenReturn(true);
		
		resource.removeElement(ELEMENT_ID, true);
		verify(service).forceRemoveElement(ELEMENT_ID);
	}
	
	@Test
	public void force_remove_element_identified_by_element() {
		when(user.scopesIncludeOneOf(IVT,IVT_ELEMENT)).thenReturn(true);

		resource.removeElement(ELEMENT_NAME, true);
		verify(service).forceRemoveElement(ELEMENT_NAME);
	}
	
	@Test
	public void remove_element_identified_by_id() {
		resource.removeElement(ELEMENT_ID, false);
		verify(service).removeElement(ELEMENT_ID);
	}
	
	@Test
	public void remove_element_identified_by_name() {
		resource.removeElement(ELEMENT_NAME, false);
		verify(service).removeElement(ELEMENT_NAME);
	}
	
}

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

import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ReasonCode.IVT0300E_ELEMENT_NOT_FOUND;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;

@RunWith(MockitoJUnitRunner.class)
public class ElementProviderTest {
	
    private static final ElementId ELEMENT_ID = randomElementId();
    private static final ElementName ELEMENT_NAME = elementName("element");
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private Repository repository;
	
	@InjectMocks
	private ElementProvider elements = new ElementProvider();
	
	@Test
	public void tryFetchElement_returns_null_for_non_existing_element() {
		assertNull(elements.tryFetchElement(ELEMENT_ID));
		assertNull(elements.tryFetchElement(ELEMENT_NAME));
	}
	
	@Test
	public void fetchElement_throws_EntityNotFoundException_when_attempting_to_read_non_existing_element_by_id() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0300E_ELEMENT_NOT_FOUND));
		elements.fetchElement(ELEMENT_ID);
	}
	
	@Test
	public void fetchElement_throws_EntityNotFoundException_when_attempting_to_read_non_existing_element_by_name() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0300E_ELEMENT_NOT_FOUND));
		elements.fetchElement(ELEMENT_NAME);
	}
	
	@Test
	public void fetch_element_by_id_and_name_does_nothing_if_id_and_name_are_null() {
	    assertNull(elements.fetchElement(null,null));
	}

    @Test
    public void fetch_element_by_id_and_name_used_id_both_are_specified() {
        Element element = mock(Element.class);
        when(repository.execute(any(Query.class))).thenReturn(element);
        
        ElementProvider delegate = spy(elements);
        assertSame(element,delegate.fetchElement(ELEMENT_ID,ELEMENT_NAME));
        verify(delegate).fetchElement(ELEMENT_ID);
    }
    
    @Test
    public void fetch_element_by_name_when_id_is_null() {
        Element element = mock(Element.class);
        when(repository.execute(any(Query.class))).thenReturn(element);
        
        ElementProvider delegate = spy(elements);
        assertSame(element,delegate.fetchElement(null,ELEMENT_NAME));
        verify(delegate).fetchElement(ELEMENT_NAME);
        
    }
    
    @Test
    public void fetch_element_by_id_when_name_is_null() {
        Element element = mock(Element.class);
        when(repository.execute(any(Query.class))).thenReturn(element);
        
        ElementProvider delegate = spy(elements);
        assertSame(element,delegate.fetchElement(ELEMENT_ID,null));
        verify(delegate).fetchElement(ELEMENT_ID);
        
    }

	
	
}

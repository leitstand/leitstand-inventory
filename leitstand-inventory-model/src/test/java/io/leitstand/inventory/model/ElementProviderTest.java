/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ReasonCode.IVT0300E_ELEMENT_NOT_FOUND;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.junit.Assert.assertNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.model.Repository;

@RunWith(MockitoJUnitRunner.class)
public class ElementProviderTest {
	
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private Repository repository;
	
	@InjectMocks
	private ElementProvider elements = new ElementProvider();
	
	@Test
	public void tryFetchElement_returns_null_for_non_existing_element() {
		assertNull(elements.tryFetchElement(randomElementId()));
		assertNull(elements.tryFetchElement(elementName("non-existing-element")));
	}
	
	@Test
	public void fetchElement_throws_EntityNotFoundException_when_attempting_to_read_non_existing_element_by_id() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0300E_ELEMENT_NOT_FOUND));
		elements.fetchElement(randomElementId());
	}
	
	@Test
	public void fetchElement_throws_EntityNotFoundException_when_attempting_to_read_non_existing_element_by_name() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0300E_ELEMENT_NOT_FOUND));
		elements.fetchElement(elementName("non-existing-element"));
	}
}

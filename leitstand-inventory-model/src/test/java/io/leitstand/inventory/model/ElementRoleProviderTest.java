/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.ElementRoleId.randomElementRoleId;
import static io.leitstand.inventory.service.ReasonCode.IVT0400E_ELEMENT_ROLE_NOT_FOUND;
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
import io.leitstand.inventory.service.ElementRoleName;

@RunWith(MockitoJUnitRunner.class)
public class ElementRoleProviderTest {
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private Repository repository;
	
	@InjectMocks
	private ElementRoleProvider elements = new ElementRoleProvider();
	
	@Test
	public void tryFetchElementRole_returns_null_for_non_existing_element() {
		assertNull(elements.tryFetchElementRole(randomElementRoleId()));
		assertNull(elements.tryFetchElementRole(ElementRoleName.valueOf("unknown-role")));
	}
	
	@Test
	public void fetchElementRole_throws_EntityNotFoundException_when_attempting_to_read_non_existing_element_by_id() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0400E_ELEMENT_ROLE_NOT_FOUND));
		elements.fetchElementRole(randomElementRoleId());
	}
	
	@Test
	public void fetchElementRole_throws_EntityNotFoundException_when_attempting_to_read_non_existing_element_by_name() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0400E_ELEMENT_ROLE_NOT_FOUND));
		elements.fetchElementRole(ElementRoleName.valueOf("unknown-role"));
	}
	
	
}

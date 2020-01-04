/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ReasonCode.IVT0100E_GROUP_NOT_FOUND;
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
public class ElementGroupProviderTest {
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private Repository repository;
	
	@InjectMocks
	private ElementGroupProvider groups = new ElementGroupProvider();
	
	@Test
	public void tryFetchElementGroup_returns_null_for_non_existing_element() {
		assertNull(groups.tryFetchElementGroup(randomGroupId()));
		assertNull(groups.tryFetchElementGroup(groupType("pod"),
											   groupName("unknown-group")));
	}
	
	@Test
	public void fetchElementGroup_throws_EntityNotFoundException_when_attempting_to_read_non_existing_element_by_id() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0100E_GROUP_NOT_FOUND));
		groups.fetchElementGroup(randomGroupId());
	}
	
	@Test
	public void fetchElement_throws_EntityNotFoundException_when_attempting_to_read_non_existing_element_by_name() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0100E_GROUP_NOT_FOUND));
		groups.fetchElementGroup(groupType("pod"), 
								 groupName("unknown-group"));
	}
	
	
}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.PlatformId.randomPlatformId;
import static io.leitstand.inventory.service.ReasonCode.IVT0900E_PLATFORM_NOT_FOUND;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementPlatformInfo;

@RunWith(MockitoJUnitRunner.class)
public class PlatformProviderTest {
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private Repository repository;
	
	@InjectMocks
	private PlatformProvider provider = new PlatformProvider();

	@Test
	public void returns_null_if_platform_does_not_exists() {
		assertNull(provider.tryFetchPlatform(mock(ElementPlatformInfo.class)));
	}
	
	@Test
	public void throws_EntityNotFoundException_if_plaform_id_is_unknown() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0900E_PLATFORM_NOT_FOUND));
		
		provider.fetchPlatform(randomPlatformId());
	}
	
	@Test
	public void throws_EntityNotFoundException_if_platform_is_unknon() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0900E_PLATFORM_NOT_FOUND));
		
		provider.fetchPlatform(mock(ElementPlatformInfo.class));
	}
	
}

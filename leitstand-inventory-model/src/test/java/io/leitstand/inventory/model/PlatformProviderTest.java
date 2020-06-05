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

import static io.leitstand.inventory.service.PlatformChipsetName.platformChipsetName;
import static io.leitstand.inventory.service.PlatformId.randomPlatformId;
import static io.leitstand.inventory.service.PlatformName.platformName;
import static io.leitstand.inventory.service.ReasonCode.IVT0900E_PLATFORM_NOT_FOUND;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.PlatformChipsetName;
import io.leitstand.inventory.service.PlatformId;
import io.leitstand.inventory.service.PlatformName;

@RunWith(MockitoJUnitRunner.class)
public class PlatformProviderTest {
	
	private static final PlatformChipsetName PLATFORM_CHIPSET = platformChipsetName("unitest");
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private Repository repository;
	
	@InjectMocks
	private PlatformProvider provider = new PlatformProvider();

	@Test
	public void try_fetch_returns_null_if_platform_id_is_unknown() {
		assertNull(provider.tryFetchPlatform(randomPlatformId()));
	}

	@Test
	public void try_fetch_returns_null_if_platform_name_is_unknown() {
		assertNull(provider.tryFetchPlatform(platformName("unknown")));
	}

	
	@Test
	public void fetch_throws_EntityNotFoundException_if_plaform_id_is_unknown() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0900E_PLATFORM_NOT_FOUND));
		
		provider.fetchPlatform(randomPlatformId());
	}
	
	@Test
	public void fetch_throws_EntityNotFoundException_if_platform_name_is_unknon() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0900E_PLATFORM_NOT_FOUND));
		
		provider.fetchPlatform(platformName("unknown"));
	}

	@Test
	public void findOrCreatePlatform_returns_null_if_neither_id_or_name_is_specified() {
		assertNull(provider.findOrCreatePlatform(null, 
												 null,
												 null));
	}
	
	@Test
	public void findOrCreatePlatform_search_platform_by_name_if_no_id_is_specified() {
		Platform platform = mock(Platform.class);
		PlatformName name = platformName("unitTest");
		when(repository.execute(any(Query.class))).thenReturn(platform);
		
		assertSame(platform,provider.findOrCreatePlatform(null, 
														  name, 
														  PLATFORM_CHIPSET));
		
		verify(repository,never()).add(any(Platform.class)); // Do not add an existing platform
	}
	
	@Test
	public void findOrCreatePlatform_creates_a_new_platform_with_random_ID_if_no_platform_with_the_specified_exists() {
		PlatformId platformId = randomPlatformId();
		PlatformName name = platformName("unitTest");
		ArgumentCaptor<Platform> platformCaptor = ArgumentCaptor.forClass(Platform.class);
		doNothing().when(repository).add(platformCaptor.capture());
		
		Platform platform = provider.findOrCreatePlatform(platformId, 
														  name,
														  PLATFORM_CHIPSET);
		
		Platform created = platformCaptor.getValue();
		assertSame(platform,created);
		assertNotNull(platform.getPlatformId());
		assertEquals(name,platform.getPlatformName());
		verify(repository).execute(any(Query.class));
	}
	
	@Test
	public void findOrCreatedPlatform_search_by_id_has_precedence_over_search_by_name() {
		PlatformId platformId = randomPlatformId();
		PlatformName platformName = platformName("unitTest");
		ArgumentCaptor<Platform> platformCaptor = ArgumentCaptor.forClass(Platform.class);
		doNothing().when(repository).add(platformCaptor.capture());

		PlatformProvider spy = spy(provider);
		
		Platform platform = spy.findOrCreatePlatform(platformId, 
													 platformName,
													 PLATFORM_CHIPSET);
		
		Platform created = platformCaptor.getValue();
		assertSame(platform,created);
		assertEquals(platformId, platform.getPlatformId());
		assertEquals(platformName, platform.getPlatformName());		
	}
	
	@Test
	public void findOrCreatePlatform_search_by_id_returns_the_found_platform() {
		Platform platform = mock(Platform.class);
		PlatformId id = randomPlatformId();
		PlatformName name = platformName("unittest");
		when(repository.execute(any(Query.class))).thenReturn(platform);
		
		assertSame(platform,provider.findOrCreatePlatform(id, 
														  name,
														  PLATFORM_CHIPSET));
		
		verify(repository,never()).add(any(Platform.class)); // Do not add an existing platform
		
	}
	
	
}

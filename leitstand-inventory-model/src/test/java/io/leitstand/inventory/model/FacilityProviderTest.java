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

import static io.leitstand.inventory.service.FacilityId.randomFacilityId;
import static io.leitstand.inventory.service.FacilityName.facilityName;
import static io.leitstand.inventory.service.ReasonCode.IVT0600E_FACILITY_NOT_FOUND;
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
public class FacilityProviderTest {
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private Repository repository;
	
	@InjectMocks
	private FacilityProvider provider = new FacilityProvider();

	@Test
	public void try_fetch_returns_null_if_facility_id_is_unknown() {
		assertNull(provider.tryFetchFacility(randomFacilityId()));
	}

	@Test
	public void try_fetch_returns_null_if_facility_name_is_unknown() {
		assertNull(provider.tryFetchFacility(facilityName("unknown")));
	}

	
	@Test
	public void fetch_throws_EntityNotFoundException_if_plaform_id_is_unknown() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0600E_FACILITY_NOT_FOUND));
		
		provider.fetchFacility(randomFacilityId());
	}
	
	@Test
	public void fetch_throws_EntityNotFoundException_if_facility_name_is_unknon() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0600E_FACILITY_NOT_FOUND));
		
		provider.fetchFacility(facilityName("unknown"));
	}

	

	
	
	
}

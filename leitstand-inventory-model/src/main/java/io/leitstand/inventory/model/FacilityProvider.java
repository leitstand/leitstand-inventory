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

import static io.leitstand.inventory.model.Facility.findFacilityById;
import static io.leitstand.inventory.model.Facility.findFacilityByName;
import static io.leitstand.inventory.service.ReasonCode.IVT0600E_FACILITY_NOT_FOUND;
import static java.lang.String.format;

import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.FacilityId;
import io.leitstand.inventory.service.FacilityName;

@Dependent
public class FacilityProvider {
	
	private static final Logger LOG = Logger.getLogger(FacilityProvider.class.getName());

	private Repository repository;
	
	protected FacilityProvider() {
		// CDI
	}
	
	@Inject
	protected FacilityProvider(@Inventory Repository repository) {
		this.repository = repository;
	}
	
	public Facility tryFetchFacility(FacilityId facilityId) {
		return repository.execute(findFacilityById(facilityId));
	}
	
	public Facility tryFetchFacility(FacilityName facilityName) {
		return repository.execute(findFacilityByName(facilityName));
	}
	
	
	public Facility fetchFacility(FacilityId id) {
		Facility facility = tryFetchFacility(id);
		if(facility == null) {
			LOG.fine(() -> format("%s: Facility %s does not exist!", 
							IVT0600E_FACILITY_NOT_FOUND.getReasonCode(),
							id));
			throw new EntityNotFoundException(IVT0600E_FACILITY_NOT_FOUND,
											  id);
		}
		return facility;
	}
	
	public Facility fetchFacility(FacilityName name) {
		Facility facility = tryFetchFacility(name);
		if(facility == null) {
			LOG.fine(() -> format("%s: Facility %s does not exist!", 
							IVT0600E_FACILITY_NOT_FOUND.getReasonCode(),
							name));
			throw new EntityNotFoundException(IVT0600E_FACILITY_NOT_FOUND,
											  name);
		}
		return facility;
	}
	
}

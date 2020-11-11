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

import static io.leitstand.commons.messages.MessageFactory.createMessage;
import static io.leitstand.commons.model.ObjectUtil.isDifferent;
import static io.leitstand.inventory.model.Facility.findFacilityById;
import static io.leitstand.inventory.model.Facility.findFacilityByName;
import static io.leitstand.inventory.service.ReasonCode.IVT0600E_FACILITY_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0604W_FACILITY_NAME_MISMATCH;
import static io.leitstand.inventory.service.ReasonCode.IVT0606W_FACILITY_TYPE_MISMATCH;
import static java.lang.String.format;
import static java.util.logging.Logger.getLogger;

import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.FacilityId;
import io.leitstand.inventory.service.FacilityName;
import io.leitstand.inventory.service.FacilityType;

@Dependent
public class FacilityProvider {
	
	private static final Logger LOG = getLogger(FacilityProvider.class.getName());

	@Inject
	@Inventory
	private Repository repository;
	
	@Inject
	private Messages messages;
	
	protected FacilityProvider() {
	    // CDI
	}
	
	protected FacilityProvider(Repository repository, Messages messages) {
	    this.repository = repository;
	    this.messages = messages;
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
	
	public Facility fetchFacility(FacilityId facilityId, 
	                              FacilityType facilityType ,
	                              FacilityName facilityName) {
	    if(facilityId != null) {
	        Facility facility = fetchFacility(facilityId);
	        reportNameMismatch(facilityName,facility);
	        reportTypeMismatch(facilityType,facility);
	        return facility;
	    }
	    if(facilityName != null) {
	        Facility facility = fetchFacility(facilityName);
	        reportTypeMismatch(facilityType,facility);
	    }
	    
	    return null;
	    
	}

    void reportNameMismatch(FacilityName facilityName, Facility facility) {
        if(facilityName == null) {
            return;
        }
        if(isDifferent(facilityName, facility.getFacilityName())) {
            LOG.fine(()->format("%s: Facility name %s does not match the expected facility name %s. Facility-ID: %s.",
                                IVT0604W_FACILITY_NAME_MISMATCH.getReasonCode(),
                                facility.getFacilityName(),
                                facilityName,
                                facility.getFacilityId()));
            messages.add(createMessage(IVT0604W_FACILITY_NAME_MISMATCH,
                                       facility.getFacilityName(),
                                       facilityName));
        }
    }

    void reportTypeMismatch(FacilityType facilityType, Facility facility) {
        if(facilityType == null) {
            return;
        }
        if(isDifferent(facilityType, facility.getFacilityType())) {
            LOG.fine(()->format("%s: Facility type %s does not match the expected facility type %s. Facility-ID: %s.",
                                IVT0606W_FACILITY_TYPE_MISMATCH.getReasonCode(),
                                facility.getFacilityType(),
                                facilityType,
                                facility.getFacilityId()));
            messages.add(createMessage(IVT0606W_FACILITY_TYPE_MISMATCH,
                                       facility.getFacilityName(),
                                       facilityType));
        }
        
    }
    
}

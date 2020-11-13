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
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

public class PhysicalInterfaceData extends BaseElementEnvelope {
	
	public static Builder newPhysicalInterfaceData() {
		return new Builder();
	}
	
	public static class Builder extends BaseElementEnvelopeBuilder<PhysicalInterfaceData,Builder> {
		
		protected Builder() {
			super(new PhysicalInterfaceData());
		}
		
		public Builder withIfpName(InterfaceName ifpName) {
			assertNotInvalidated(getClass(), object);
			object.ifpName = ifpName;
			return this;
		}
		
		public Builder withIfpAlias(String ifpAlias) {
			assertNotInvalidated(getClass(), object);
			object.ifpAlias = ifpAlias;
			return this;
		}
		
		public Builder withIfpAdministrativeState(AdministrativeState admState) {
			assertNotInvalidated(getClass(), object);
			object.ifpAdministrativeState = admState;
			return this;
		}
		
		public Builder withIfpOperationalState(OperationalState opState) {
			assertNotInvalidated(getClass(), object);
			object.ifpOperationalState = opState;
			return this;
		}
		
		public Builder withFacilityId(FacilityId facilityId) {
		    assertNotInvalidated(getClass(), object);
		    object.facilityId = facilityId;
		    return this;
		}
		
		public Builder withFacilityName(FacilityName facilityName) {
		    assertNotInvalidated(getClass(), object);
		    object.facilityName = facilityName;
		    return this;
		}
		
		public Builder withLocation(String location) {
		    assertNotInvalidated(getClass(), object);
		    object.location = location;
		    return this;
		}

	}
	
	private FacilityId facilityId;
	private FacilityName facilityName;
	private String location;
	private InterfaceName ifpName;
	private String ifpAlias;
	private OperationalState ifpOperationalState;
	private AdministrativeState ifpAdministrativeState;
	
	public InterfaceName getIfpName() {
		return ifpName;
	}
	
	public String getIfpAlias() {
		return ifpAlias;
	}
	
	public AdministrativeState getIfpAdministrativeState() {
        return ifpAdministrativeState;
    }
	
	public OperationalState getIfpOperationalState() {
        return ifpOperationalState;
    }
	
	public FacilityId getFacilityId() {
        return facilityId;
    }
	
	public FacilityName getFacilityName() {
        return facilityName;
    }
	
	public String getLocation() {
        return location;
    }
}

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
import static io.leitstand.inventory.service.RackId.randomRackId;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.leitstand.commons.model.CompositeValue;

public class BaseRackEnvelope extends CompositeValue{
	
	@SuppressWarnings("unchecked")
	public static class BaseRackEnvelopeBuilder<T extends BaseRackEnvelope, B extends BaseRackEnvelopeBuilder<T, B>> {
		
		protected T rack;
		
		protected BaseRackEnvelopeBuilder(T rack) {
			this.rack = rack;
		}
		
		public B withFacilityId(FacilityId facilityId) {
			assertNotInvalidated(getClass(),rack);
			((BaseRackEnvelope)rack).facilityId = facilityId;
			return (B) this;
		}
		
		public B withFacilityName(FacilityName facilityName) {
			assertNotInvalidated(getClass(),rack);
			((BaseRackEnvelope)rack).facilityName = facilityName;
			return (B) this;
		}
		
		public B withFacilityType(FacilityType facilityType) {
			assertNotInvalidated(getClass(),rack);
			((BaseRackEnvelope)rack).facilityType = facilityType;
			return (B) this;
		}
		
		public B withRackId(RackId rackId) {
			assertNotInvalidated(getClass(),rack);
			((BaseRackEnvelope)rack).rackId = rackId;
			return (B) this;
		}
		
		public B withRackName(RackName rackName) {
			assertNotInvalidated(getClass(), rack);
			((BaseRackEnvelope)rack).rackName = rackName;
			return (B) this;
		}

		public B withRackType(String rackType) {
			assertNotInvalidated(getClass(), rack);
			((BaseRackEnvelope)rack).rackType = rackType;
			return (B) this;
		}

		public B withAdministrativeState(AdministrativeState administrativeState) {
			assertNotInvalidated(getClass(), rack);
			((BaseRackEnvelope)rack).administrativeState = administrativeState;
			return (B) this;
		}
		
		
		public B withAscending(boolean ascending) {
			assertNotInvalidated(getClass(), rack);
			((BaseRackEnvelope)rack).ascending = ascending;
			return (B) this;
		}
		
		public B withDescription(String description) {
			assertNotInvalidated(getClass(), rack);
			((BaseRackEnvelope)rack).description = description;
			return (B) this;
		}

		public B withUnits(int slots) {
			assertNotInvalidated(getClass(), rack);
			((BaseRackEnvelope)rack).units = slots;
			return (B) this;
		}

		public T build() {
			try {
				assertNotInvalidated(getClass(), rack);
				return rack;
			} finally {
				this.rack = null;
			}
		}
		
	}

	@Valid
	private FacilityId facilityId;

	@Valid
	private FacilityName facilityName;

	@Valid
	private FacilityType facilityType;

	@Valid
	@NotNull(message="{rack_id.required}")
	private RackId rackId = randomRackId();

	@Valid
	@NotNull(message="{rack_name.required}")
	private RackName rackName;
	
	@Valid
	private AdministrativeState administrativeState;
	private String rackType;
	private boolean ascending;
	private String description;

	@Min(value=1, message="{units.greater_than_1}")
	@Max(value=100, message="{units.lower_than_100}")
	private int units;
	
	public RackId getRackId() {
		return rackId;
	}
	
	public RackName getRackName() {
		return rackName;
	}
	
	public String getDescription() {
		return description;
	}

	public int getUnits() {
		return units;
	}
	
	public AdministrativeState getAdministrativeState() {
		return administrativeState;
	}
	
	public String getRackType() {
		return rackType;
	}
	
	public boolean isAscending() {
		return ascending;
	}
	
	public FacilityId getFacilityId() {
		return facilityId;
	}
	
	public FacilityName getFacilityName() {
		return facilityName;
	}
	
	public FacilityType getFacilityType() {
		return facilityType;
	}
}

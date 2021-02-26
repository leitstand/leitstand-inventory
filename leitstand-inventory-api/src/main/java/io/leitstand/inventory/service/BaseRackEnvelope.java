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

import io.leitstand.commons.model.ValueObject;

/**
 * Base class for all rack-related value objects.
 */
public class BaseRackEnvelope extends ValueObject{
	
    /**
     * Base builder for all rack-related value object builders.
     *
     * @param <T> the rack value object type
     * @param <B> the rack value objectr builder
     */
    @SuppressWarnings("unchecked")
	public static class BaseRackEnvelopeBuilder<T extends BaseRackEnvelope, B extends BaseRackEnvelopeBuilder<T, B>> {
		
		protected T rack;
		
		/**
		 * Creates a new builder.
		 * @param rack the rack value object under construction
		 */
		protected BaseRackEnvelopeBuilder(T rack) {
			this.rack = rack;
		}
		
		/**
		 * Sets the facility ID.
		 * @param facilityId the facility ID
		 * @return a reference to this builder to continue object creation
		 */
		public B withFacilityId(FacilityId facilityId) {
			assertNotInvalidated(getClass(),rack);
			((BaseRackEnvelope)rack).facilityId = facilityId;
			return (B) this;
		}
		
		/**
		 * Sets the facility name.
		 * @param facilityName the facility name
		 * @return a reference to this builder to continue object creation
		 */
		public B withFacilityName(FacilityName facilityName) {
			assertNotInvalidated(getClass(),rack);
			((BaseRackEnvelope)rack).facilityName = facilityName;
			return (B) this;
		}
		
		/**
		 * Sets the facility type.
		 * @param facilityType the facility type
		 * @return a reference to this builder to continue object creation
		 */
		public B withFacilityType(FacilityType facilityType) {
			assertNotInvalidated(getClass(),rack);
			((BaseRackEnvelope)rack).facilityType = facilityType;
			return (B) this;
		}
		
		/**
		 * Sets the rack ID
		 * @param rackId the rack ID
		 * @return a reference to this builder to continue object creation
		 */
		public B withRackId(RackId rackId) {
			assertNotInvalidated(getClass(),rack);
			((BaseRackEnvelope)rack).rackId = rackId;
			return (B) this;
		}

		/**
		 * Sets the rack name.
		 * @param rackName the rack name
         * @return a reference to this builder to continue object creation
		 */
		public B withRackName(RackName rackName) {
			assertNotInvalidated(getClass(), rack);
			((BaseRackEnvelope)rack).rackName = rackName;
			return (B) this;
		}

		/**
		 * Sets the rack type.
		 * @param rackType the rack type.
         * @return a reference to this builder to continue object creation
		 */
		public B withRackType(String rackType) {
			assertNotInvalidated(getClass(), rack);
			((BaseRackEnvelope)rack).rackType = rackType;
			return (B) this;
		}

		/**
		 * Sets the administrative state of the rack.
		 * @param administrativeState the administrative state.
         * @return a reference to this builder to continue object creation
		 */
		public B withAdministrativeState(AdministrativeState administrativeState) {
			assertNotInvalidated(getClass(), rack);
			((BaseRackEnvelope)rack).administrativeState = administrativeState;
			return (B) this;
		}
		
		/**
		 * Sets whether an ascending or descending rack unit numbering scheme is used.
		 * @param ascending <code>true</code> for ascending rack unit numbering scheme, <code>false</code> for descending numbers.
         * @return a reference to this builder to continue object creation
		 */
		public B withAscending(boolean ascending) {
			assertNotInvalidated(getClass(), rack);
			((BaseRackEnvelope)rack).ascending = ascending;
			return (B) this;
		}
		
		/**
		 * Sets the rack description.
		 * @param description the rack description
         * @return a reference to this builder to continue object creation
		 */
		public B withDescription(String description) {
			assertNotInvalidated(getClass(), rack);
			((BaseRackEnvelope)rack).description = description;
			return (B) this;
		}

		/**
		 * Sets the rack height in rack units.
		 * @param units the number of rack units
         * @return a reference to this builder to continue object creation
		 */
		public B withUnits(int units) {
			assertNotInvalidated(getClass(), rack);
			((BaseRackEnvelope)rack).units = units;
			return (B) this;
		}

		/**
		 * Copies all attributes of the given rack envelope to this envelope.
		 * This is a convenience method to simplify value object construction.
		 * @param rack the rack envelope
         * @return a reference to this builder to continue object creation
		 */
		public B withRack(BaseRackEnvelope rack) {
		    assertNotInvalidated(getClass(), rack);
		    withAdministrativeState(rack.getAdministrativeState());
		    withAscending(rack.isAscending());
		    withDescription(rack.getDescription()); 
		    withFacilityId(rack.getFacilityId());
		    withFacilityName(rack.getFacilityName());
		    withFacilityType(rack.getFacilityType());
		    withRackId(rack.getRackId());
		    withRackName(rack.getRackName());
		    withRackType(rack.getRackType());
		    withUnits(rack.getUnits());
		    return (B) this;
		    
		}
		
		/**
		 * Creates the rack-related value object and invalidates this builder.
		 * Subsequent calls of the <code>build()</code> message raise an exception.
		 * @return an immutable rack-related value object.
		 */
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
	
	/**
	 * Returns the rack ID
	 * @return the rack ID
	 */
	public RackId getRackId() {
		return rackId;
	}
	
	/**
	 * Returns the rack name.
	 * @return the rack name.
	 */
	public RackName getRackName() {
		return rackName;
	}
	
	/**
	 * Returns the rack description.
	 * @return the rack description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the rack height in rack units.
	 * @return the rack height in rack units.
	 */
	public int getUnits() {
		return units;
	}
	
	/**
	 * Returns the administrative state of this rack.
	 * @return the administrative state of this rack.
	 */
	public AdministrativeState getAdministrativeState() {
		return administrativeState;
	}
	
	/**
	 * Returns the rack type.
	 * @return the rack type.
	 */
	public String getRackType() {
		return rackType;
	}
	
	/**
	 * Returns whether ascending or descending rack unit numbering scheme is applied.
	 * @return <code>true</code> of ascending rack unit numbers and <code>false</code> for decending numbering.
	 */
	public boolean isAscending() {
		return ascending;
	}
	
	/**
	 * Returns the facility ID
	 * @return the facility ID
	 */
	public FacilityId getFacilityId() {
		return facilityId;
	}
	
	/**
	 * Returns the facility name.
	 * @return the facility name.
	 */
	public FacilityName getFacilityName() {
		return facilityName;
	}
	
	/**
	 * Returns the facility type.
	 * @return
	 */
	public FacilityType getFacilityType() {
		return facilityType;
	}
}

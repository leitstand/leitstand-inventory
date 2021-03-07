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

/**
 * Physical interface settings.
 */
public class PhysicalInterfaceData extends BaseElementEnvelope {

    /**
     * Creates a builder for an immutable <code>PhysicalInterfaceData</code> value object.
     * @return a builder for an immutable <code>PhysicalInterfaceData</code> value object.
     */
	public static Builder newPhysicalInterfaceData() {
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>PhysicalInterfaceData</code> value object.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<PhysicalInterfaceData,Builder> {
		
	    /**
	     * Creates a builder for an immutable <code>PhysicalInterfaceData</code> value object.
	     */
		protected Builder() {
			super(new PhysicalInterfaceData());
		}
		
		/**
		 * Sets the physical interface name.
		 * @param ifpName the physical interface name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withIfpName(InterfaceName ifpName) {
			assertNotInvalidated(getClass(), object);
			object.ifpName = ifpName;
			return this;
		}
	    
		/**
         * Sets the physical interface alias.
         * @param ifpAlias the physical interface alias.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withIfpAlias(String ifpAlias) {
			assertNotInvalidated(getClass(), object);
			object.ifpAlias = ifpAlias;
			return this;
		}
		
	    /**
         * Sets the administrative state of the physical.
         * @param admState the administrative state of the physical interface.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withIfpAdministrativeState(AdministrativeState admState) {
			assertNotInvalidated(getClass(), object);
			object.ifpAdministrativeState = admState;
			return this;
		}
	    
		/**
         * Sets the operational state of the physical interface.
         * @param opState the operational state of the physical interface.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withIfpOperationalState(OperationalState opState) {
			assertNotInvalidated(getClass(), object);
			object.ifpOperationalState = opState;
			return this;
		}
		
		/**
		 * Sets the facility ID of the network facility where this physical interface is located.
		 * @param facilityId the facility ID.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withFacilityId(FacilityId facilityId) {
		    assertNotInvalidated(getClass(), object);
		    object.facilityId = facilityId;
		    return this;
		}
		
		
	    /**
         * Sets the facility name of the network facility where this physical interface is located.
         * @param facilityName the facility name.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withFacilityName(FacilityName facilityName) {
		    assertNotInvalidated(getClass(), object);
		    object.facilityName = facilityName;
		    return this;
		}
		
		
	    /**
         * Sets the physical interface location.
         * @param location the physical interface location.
         * @return a reference to this builder to continue object creation.
         */
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
	
	/**
	 * Returns the physical interface name.
	 * @return the physical interface name.
	 */
	public InterfaceName getIfpName() {
		return ifpName;
	}
	
	/**
	 * Returns the physical interface alias.
	 * @return the physical interface alias.
	 */
	public String getIfpAlias() {
		return ifpAlias;
	}
	
	/**
	 * Returns the administrative state of the physical interface.
	 * @return the administrative state of the physical interface.
	 */
	public AdministrativeState getIfpAdministrativeState() {
        return ifpAdministrativeState;
    }

	/**
     * Returns the operational state of the physical interface.
     * @return the operational state of the physical interface.
     */
	public OperationalState getIfpOperationalState() {
        return ifpOperationalState;
    }
	
	/**
	 * Returns the network facility ID.
	 * @return the network facility ID.
	 */
	public FacilityId getFacilityId() {
        return facilityId;
    }
	
	/**
	 * Returns the network facility name.
	 * @return the network facility name.
	 */
	public FacilityName getFacilityName() {
        return facilityName;
    }
	
	/**
	 * Returns the physical interface location.
	 * @return the physical interface location.
	 */
	public String getLocation() {
        return location;
    }
}

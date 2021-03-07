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

import javax.json.bind.annotation.JsonbProperty;

import io.leitstand.commons.model.ValueObject;

/**
 * Physical interface reference.
 */
public class PhysicalInterface extends ValueObject implements Comparable<PhysicalInterface>{

     /**
      * Creates a builder for an immutable <code>PhysicalInterface</code> value object.
      * @return a builder for an immutable <code>PhysicalInterface</code> value object.
      */
	 public static Builder newPhysicalInterface() {
		 return new Builder();
	 }
	
	 /**
	  * A builder for an immutable <code>PhysicalInterface</code> value object.
	  */
	public static class Builder {
		
		private PhysicalInterface ifp = new PhysicalInterface();
		
		/**
		 * Sets the physical interface name.
		 * @param name the physical interface name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withIfpName(InterfaceName name) {
            assertNotInvalidated(getClass(), ifp);
			ifp.ifpName = name;
			return this;
		}

		/**
		 * Sets the physical interface alias.
		 * @param alias the physical interface alias.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withIfpAlias(String alias) {
            assertNotInvalidated(getClass(), ifp);
			ifp.ifpAlias = alias;
			return this;
		}
		
		/**
		 * Sets the physical interface category.
		 * @param category the physical interface category.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withCategory(String category) {
            assertNotInvalidated(getClass(), ifp);
			ifp.category = category;
			return this;
		}
		
		/**
		 * Sets the operational state of the physical interface.
		 * @param opState the operational state.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withOperationalState(OperationalState opState) {
            assertNotInvalidated(getClass(), ifp);
			ifp.operationalState = opState;
			return this;
		}

		/**
		 * Sets the administrative state of the physical interface.
		 * @param admState the administrative state.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withAdministrativeState(AdministrativeState admState) {
            assertNotInvalidated(getClass(), ifp);
			ifp.administrativeState = admState;
			return this;
		}
		
		/**
		 * Sets the MAC address of this physical interface.
		 * @param macAddress the MAC address of this physical interface.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withMacAddress(MACAddress macAddress) {
            assertNotInvalidated(getClass(), ifp);
			ifp.macAddress = macAddress;
			return this;
		}
		
		/**
		 * Creates an immutable <code>PhysicalInterface</code> value object and invalidates this builder.
		 * Subsequent invocations of the <code>build()</code> method raise an exception.
		 * @return the immutable <code>PhysicalInterface</code> value object.
		 */
		public PhysicalInterface build() {
			try {
			    assertNotInvalidated(getClass(), ifp);
				return ifp;
			} finally {
				this.ifp = null;
			}
		}
		
	}
	
	@JsonbProperty("ifp_name")
	private InterfaceName ifpName;
	
	private String category;
	private String ifpAlias;
	
	@JsonbProperty("mac_address")
	private MACAddress macAddress;
	
	@JsonbProperty("operational_state")
	private OperationalState operationalState;
	
	@JsonbProperty("administrative_state")
	private AdministrativeState administrativeState;
	
	/**
	 * Returns the operational state of the physical interface.
	 * @return the operational state of the physical interface.
	 */
	public OperationalState getOperationalState() {
		return operationalState;
	}
	
	/**
	 * Returns the administrative state of the physical interface.
	 * @return the administrative state of the physical interface.
	 */
	public AdministrativeState getAdministrativeState() {
		return administrativeState;
	}
	
	/**
	 * Returns the physical interface name.
	 * @return the physical interface name.
	 */
	public InterfaceName getIfpName() {
		return ifpName;
	}
	
	/**
	 * Returns the MAC address of the physical interface.
	 * @return the MAC address of the physical interface.
	 */
	public MACAddress getMacAddress() {
		return macAddress;
	}
	
	/**
	 * Returns the physical interface alias.
	 * @return the physical interface alias.
	 */
	public String getIfpAlias() {
		return ifpAlias;
	}
	
	/**
	 * Returns the physical interface category.
	 * @return the physical interface category.
	 */
	public String getCategory() {
		return category;
	}
	
	/**
	 * Compares two physical interfaces by their name.
	 * @param ifp the physical inteface to be compare with this physical interface.
	 * @returns a negative, zero or positive integer depending on whether this interface is lower, equal or greater than the specified physical interface.
	 */
	@Override
	public int compareTo(PhysicalInterface ifp) {
		return getIfpName().compareTo(ifp.getIfpName());
	}

	
}

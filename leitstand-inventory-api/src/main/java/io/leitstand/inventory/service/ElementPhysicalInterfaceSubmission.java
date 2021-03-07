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

import io.leitstand.commons.model.ValueObject;

/**
 * A submission for storing a physical interface.
 */
public class ElementPhysicalInterfaceSubmission extends ValueObject {
	
    /**
     * Creates a builder for an immutable <code>ElementPhysicalInterfaceSubmission</code> value object.
     * @return a builder for an immutable <code>ElementPhysicalInterfaceSubmission</code> value object.
     */
	public static Builder newPhysicalInterfaceSubmission() {
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>ElementPhysicalInterfaceSubmission</code> value object.
	 */
	public static class Builder {
		
		private ElementPhysicalInterfaceSubmission submission = new ElementPhysicalInterfaceSubmission();
		
		/**
		 * Sets the physical interface name.
		 * @param ifpName the physical interface name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withIfpName(InterfaceName ifpName) {
			assertNotInvalidated(getClass(), submission);
			submission.ifpName = ifpName;
			return this;
		}

	    /**
         * Sets the physical interface alias.
         * @param ifpAlias the physical interface alias.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withIfpAlias(String ifpAlias) {
			assertNotInvalidated(getClass(), submission);
			submission.ifpAlias = ifpAlias;
			return this;
		}
		
	    /**
         * Sets the physical interface category.
         * @param category the physical interface category.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withCategory(String category) {
			assertNotInvalidated(getClass(), submission);
			submission.category = category;
			return this;
		}
		
	    /**
         * Sets the physical interface bandwidth.
         * @param bandwidth the physical interface bandwidth.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withBandwidth(Bandwidth bandwidth) {
			assertNotInvalidated(getClass(), submission);
			submission.bandwidth = bandwidth;
			return this;
		}
		
	    /**
         * Sets the physical interface MAC address.
         * @param macAddress the physical interface MAC address.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withMacAddress(MACAddress macAddress) {
			assertNotInvalidated(getClass(), submission);
			submission.macAddress = macAddress;
			return this;
		}
		
	    /**
         * Sets the operational state of the physical interface.
         * @param operationalState the operational state of the physical interface.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withOperationalState(OperationalState operationalState) {
			assertNotInvalidated(getClass(), submission);
			submission.operationalState = operationalState;
			return this;
		}
		
	    /**
         * Sets the administrative state of the physical interface.
         * @param administrativeState the administrative state of the physical interface.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withAdministrativeState(AdministrativeState administrativeState) {
			assertNotInvalidated(getClass(), submission);
			submission.administrativeState = administrativeState;
			return this;
		}
		
		/** 
		 * Sets the container interface name.
		 * @param ifcName the container interface name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withIfcName(InterfaceName ifcName) {
			assertNotInvalidated(getClass(), submission);
			submission.ifcName = ifcName;
			return this;
		}
		
	    /** 
         * Sets the physical interface neighbor.
         * @param neighbor the physical interface neighbor.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withNeighbor(ElementPhysicalInterfaceNeighbor.Builder neighbor) {
			return withNeighbor(neighbor.build());
		}

	    /** 
         * Sets the physical interface neighbor.
         * @param neighbor the physical interface neighbor.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withNeighbor(ElementPhysicalInterfaceNeighbor neighbor) {
			assertNotInvalidated(getClass(), submission);
			submission.neighbor = neighbor;
			return this;
		}
		
		/**
		 * Creates an immutable <code>ElementPhysicalInterfaceSubmission</code> value object and invalidates this builder.
		 * Subsequent invocations of the <code>build()</code> method raise an exception.
		 * @return the immutable <code>ElementPhysicalInterfaceSubmission</code> value object.
		 */
		public ElementPhysicalInterfaceSubmission build() {
			try {
				assertNotInvalidated(getClass(), submission);
				return submission;
			} finally {
				this.submission = null;
			}
		}
		
	}
	

	private InterfaceName ifpName;
	
	private String ifpAlias;
	
	private String category;

	private Bandwidth bandwidth;

	private MACAddress macAddress;
	
	private OperationalState operationalState;
	
	private AdministrativeState administrativeState;
	
	private InterfaceName ifcName;
	
	private ElementPhysicalInterfaceNeighbor neighbor;
	
	/**
	 * Returns the physical interface name.
	 * @return the physical interface name.
	 */
	public InterfaceName getIfpName(){
		return ifpName;
	}
	
	/**
	 * Returns the MAC address of the physical interface.
	 * @return the physical interface MAC address.
	 */
	public MACAddress getMacAddress(){
		return macAddress;
	}
	
	/**
	 * Returns the operational state of the physical interface.
	 * @return the operational state of the physical interface.
	 */
	public OperationalState getOperationalState() {
		return operationalState;
	}
	
	/**
	 * Returns the container interface name.
	 * <p>
	 * Multiple physical interfaces can be bundled to a single interface, such that 
	 * logical interfaces can leverage multiple physical interfaces. 
	 * Consequently, the container interface contains either one or multiple physical interfaces which
	 * means that different physical interfaces can refer to the same container interface.
	 * </p>
	 * @return the container interface name.
	 */
	public InterfaceName getIfcName() {
		if(ifcName == null) {
			return ifpName;
		}
		return ifcName;
	}
	
	/**
	 * Returns the physical interface bandwidth.
	 * @return the physical interface bandwidth.
	 */
	public Bandwidth getBandwidth() {
		return bandwidth;
	}
	
	/**
	 * Returns the administrative state of the physical interface.
	 * @return the administrative state of the physical interface.
	 */
	public AdministrativeState getAdministrativeState() {
		return administrativeState;
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
	 * Returns the physical interface neighbor.
	 * @return the physical interface neighbor.
	 */
	public ElementPhysicalInterfaceNeighbor getNeighbor() {
		return neighbor;
	}
	
}

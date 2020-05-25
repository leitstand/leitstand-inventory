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
 * A submission to store a new physical interface of a certain element in the resource inventory.
 */

public class ElementPhysicalInterfaceSubmission extends ValueObject {
	
	public static Builder newPhysicalInterfaceSubmission() {
		return new Builder();
	}
	
	public static class Builder {
		
		private ElementPhysicalInterfaceSubmission submission = new ElementPhysicalInterfaceSubmission();
		
		public Builder withIfpName(InterfaceName ifpName) {
			assertNotInvalidated(getClass(), submission);
			submission.ifpName = ifpName;
			return this;
		}
		
		public Builder withIfpAlias(String alias) {
			assertNotInvalidated(getClass(), submission);
			submission.ifpAlias = alias;
			return this;
		}
		
		public Builder withCategory(String category) {
			assertNotInvalidated(getClass(), submission);
			submission.category = category;
			return this;
		}
		
		public Builder withBandwidth(Bandwidth bandwidth) {
			assertNotInvalidated(getClass(), submission);
			submission.bandwidth = bandwidth;
			return this;
		}
		
		public Builder withMacAddress(MACAddress macAddress) {
			assertNotInvalidated(getClass(), submission);
			submission.macAddress = macAddress;
			return this;
		}
		
		public Builder withOperationalState(OperationalState operationalState) {
			assertNotInvalidated(getClass(), submission);
			submission.operationalState = operationalState;
			return this;
		}
		
		public Builder withAdministrativeState(AdministrativeState administrativeState) {
			assertNotInvalidated(getClass(), submission);
			submission.administrativeState = administrativeState;
			return this;
		}
		
		public Builder withIfcName(InterfaceName ifcName) {
			assertNotInvalidated(getClass(), submission);
			submission.ifcName = ifcName;
			return this;
		}
		
		public Builder withNeighbor(ElementPhysicalInterfaceNeighbor.Builder neighbor) {
			return withNeighbor(neighbor.build());
		}
		
		public Builder withNeighbor(ElementPhysicalInterfaceNeighbor neighbor) {
			assertNotInvalidated(getClass(), submission);
			submission.neighbor = neighbor;
			return this;
		}
		
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
	 * Returns the MAC address of the physical interface
	 * @return the physical interface MAC address
	 */
	public MACAddress getMacAddress(){
		return macAddress;
	}
	
	/**
	 * Returns the operational state of the physical interface.
	 * @return the operational state
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
	 * Returns the bandwidth of the physical interface.
	 * @return the bandwidth
	 */
	public Bandwidth getBandwidth() {
		return bandwidth;
	}
	
	/**
	 * Returns the administrative state of the physical interface.
	 * @return the administrative state
	 */
	public AdministrativeState getAdministrativeState() {
		return administrativeState;
	}
	
	public String getIfpAlias() {
		return ifpAlias;
	}
	
	public String getCategory() {
		return category;
	}
	
	public ElementPhysicalInterfaceNeighbor getNeighbor() {
		return neighbor;
	}
	
}

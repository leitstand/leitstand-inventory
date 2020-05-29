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
 * Contains the properties of a physical interface.
 */
public class ElementPhysicalInterfaceData extends ValueObject {

	/**
	 * Returns a builder to create an immutable <code>ElementPhysicalInterfaceData</code> instance.
	 * @return a builder to create an immutable <code>ElementPhysicalInterfaceData</code> instance.
	 */
	public static Builder newPhysicalInterfaceData(){
		return new Builder();
	}
	
	/**
	 * The builder to create an immutable <code>ElementPhysicalInterfaceData</code> instance.
	 */
	public static class Builder {
		
		private ElementPhysicalInterfaceData data = new ElementPhysicalInterfaceData();
		
		/**
		 * Sets the physical interface name.
		 * @param name - the interface name
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withIfpName(InterfaceName name){
			assertNotInvalidated(getClass(), data);
			data.ifpName = name;
			return this;
		}
		
		public Builder withIfpAlias(String ifpAlias) {
			assertNotInvalidated(getClass(), data);
			data.ifpAlias = ifpAlias;
			return this;
		}

		public Builder withCategory(String category) {
			assertNotInvalidated(getClass(), data);
			data.category = category;
			return this;
		}
		
		/**
		 * Sets the bandwidth of the physical interface name.
		 * @param bandwidth - the interface bandwidth
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withBandwidth(Bandwidth bandwidth) {
			assertNotInvalidated(getClass(), data);
			data.bandwidth = bandwidth;
			return this;
		}
		
		/**
		 * Sets the MAC address of the physical interface
		 * @param macAddress - the MAC address
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withMacAddress(MACAddress macAddress){
			assertNotInvalidated(getClass(), data);
			data.macAddress = macAddress;
			return this;
		}
		
		/**
		 * Sets the operational state of the physical interface
		 * @param opState - the operational state
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withOperationalState(OperationalState opState){
			assertNotInvalidated(getClass(), data);
			data.operationalState = opState;
			return this;
		}

		/**
		 * Sets the administrative state of the physical interface
		 * @param admState - the administrative state
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withAdministrativeState(AdministrativeState admState){
			assertNotInvalidated(getClass(), data);
			data.administrativeState = admState;
			return this;
		}

		/**
		 * Sets the neighbor interface of this physical interface.
		 * @param neighbor - the neighbor interface
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withNeighbor(ElementPhysicalInterfaceNeighbor.Builder neighbor) {
			return withNeighbor(neighbor.build());
		}
		
		/**
		 * Sets the neighbor interface of this physical interface.
		 * @param neighbor - the neighbor interface
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withNeighbor(ElementPhysicalInterfaceNeighbor neighbor) {
			assertNotInvalidated(getClass(), data);
			data.neighbor = neighbor;
			return this;
		}

		/**
		 * Returns an immutable <code>ElementPhysicalInterfaceData</code> instance and invalidates this builder.
		 * All further interactions with this builder raises an exception.
		 * @return an immutable <code>ElementPhysicalInterfaceData</code> instance.
		 */
		public ElementPhysicalInterfaceData build(){
			try{
				assertNotInvalidated(getClass(), data);
				return data;
			} finally {
				this.data = null;
			}
		}
	}
	
	
	private InterfaceName ifpName;
	
	@JsonbProperty("bandwidth")
	private Bandwidth bandwidth;
	
	@JsonbProperty("mac_address")
	private MACAddress macAddress;
	@JsonbProperty("operational_state")
	private OperationalState operationalState;
	@JsonbProperty("administrative_state")
	private AdministrativeState administrativeState;
	
	private String ifpAlias;
	private String category;
	
	private ElementPhysicalInterfaceNeighbor neighbor;
	
	/**
	 * Returns the neighbor interface of this physical interface or <code>null</code> if no neighbor information is available.
	 * @return the neighbor interface 
	 */
	public ElementPhysicalInterfaceNeighbor getNeighbor() {
		return neighbor;
	}
	
	/**
	 * Returns the physical interface name.
	 * @return the physical interface name.
	 */
	public InterfaceName getIfpName(){
		return ifpName;
	}
	
	/**
	 * Retruns the MAC address of the physical interface.
	 * @return the MAC address
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
	 * Returns the administrative state of the physical interface.
	 * @return the administrative state of the physical interface.
	 */
	public AdministrativeState getAdministrativeState() {
		return administrativeState;
	}
	
	/**
	 * Returns the bandwidth of the physical interface.
	 * @return the bandwidth
	 */
	public Bandwidth getBandwidth() {
		return bandwidth;
	}
	
	public String getIfpAlias() {
		return ifpAlias;
	}
	
	public String getCategory() {
		return category;
	}
}

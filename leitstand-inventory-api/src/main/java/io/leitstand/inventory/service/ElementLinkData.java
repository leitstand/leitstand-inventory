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
import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.leitstand.commons.model.ValueObject;
import io.leitstand.inventory.jsonb.MACAddressAdapter;

/**
 * A physical link between two elements.
 */
public class ElementLinkData extends ValueObject{

	/**
	 * Returns a new builder to create an immutable <code>ElementLinkData</code> value object.
	 * @return a new builder to create an immutable <code>ElementLinkData</code> value object.
	 */
	public static Builder newElementLinkData(){
		return new Builder();
	}

	/**
	 * A builder to create an immutable <code>ElementLinkData</code> value object.
	 */
	public static final class Builder {
		
		private ElementLinkData link = new ElementLinkData();
		
		/**
		 * Sets the local physical interface name.
		 * @param ifpName the local physical interface name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withLocalIfpName(InterfaceName ifpName){
			assertNotInvalidated(getClass(), link);
			link.localIfpName = ifpName;
			return this;
		}

		/**
		 * Sets the MAC address of the local physical interface
		 * @param mac the local MAC address
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withLocalMac(MACAddress mac){
			assertNotInvalidated(getClass(), link);
			link.localMac = mac;
			return this;
		}
		
		/**
		 * Sets the locally configured physical interface bandwidth.
		 * @param bandwidth the configured bandwidth
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withLocalBandwidth(Bandwidth bandwidth) {
			assertNotInvalidated(getClass(), link);
			link.localBandwidth = bandwidth;
			return this;
		}
		
		/**
		 * Sets the neighbor physical interface MAC address.
		 * @param mac the neighbor MAC address
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withRemoteMac(MACAddress mac){
			assertNotInvalidated(getClass(), link);
			link.neighborMac = mac;
			return this;
		}
		
		/**
		 * Sets the neighbor physical interface name.
		 * @param ifpName the neighbor interface name
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withRemoteIfpName(InterfaceName ifpName) {
			assertNotInvalidated(getClass(), link);
			link.neighborIfpName = ifpName;
			return this;
		}
		
		/**
		 * Sets the neighbor bandwidth setting.
		 * @param bandwidth the neighbor bandwidth setting
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withRemoteBandwidth(Bandwidth bandwidth) {
			assertNotInvalidated(getClass(), link);
			link.neighborBandwidth = bandwidth;
			return this;
		}
		
		/**
		 * Sets the neighbor element name.
		 * @param elementName the neighbor element name
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withRemoteElementName(ElementName elementName){
			assertNotInvalidated(getClass(), link);
			link.neighborElementName = elementName;
			return this;
		}
		
		/**
		 * Sets the neighbor element ID.
		 * @param elementId the neighbor element ID
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withRemoteElementId(ElementId elementId){
			assertNotInvalidated(getClass(), link);
			link.neighborElementId = elementId;
			return this;
		}

		/**
		 * Sets the neighbor element group ID
		 * @param groupId the neighbor element group ID
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withRemoteElementGroupId(ElementGroupId groupId) {
			assertNotInvalidated(getClass(), link);
			link.neighborElementGroupId = groupId;
			return this;
		}
		
		/**
		 * Sets the neighbor element group type.
		 * @param groupType the neighbor element group type.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withRemoteElementGroupType(ElementGroupType groupType) {
			assertNotInvalidated(getClass(), link);
			link.neighborElementGroupType = groupType;
			return this;
		}
		
	    /**
         * Sets the neighbor element group name.
         * @param groupType the neighbor element group name.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withRemoteElementGroupName(ElementGroupName groupName) {
			assertNotInvalidated(getClass(), link);
			link.neighborElementGroupName = groupName;
			return this;
		}
		
		/**
		 * Sets the local operational state.
		 * @param opState the local operational state
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withLocalOperationalState(OperationalState opState) {
			assertNotInvalidated(getClass(), link);
			link.localOperationalState = opState;
			return this;
		}

		/**
		 * Sets the local administrative state.
		 * @param admState the local administrative state
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withLocalAdministrativeState(AdministrativeState admState) {
			assertNotInvalidated(getClass(), link);
			link.localAdministrativeState = admState;
			return this;
		}

		/**
		 * Sets the neighbor operational state.
		 * @param opState the neighbor operational state
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withRemoteOperationalState(OperationalState opState) {
			assertNotInvalidated(getClass(), link);
			link.neighborOperationalState = opState;
			return this;
		}

		/**
		 * Sets the neighbor administrative state.
		 * @param admState the neighbor administrative state
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withRemoteAdministrativeState(AdministrativeState admState) {
			assertNotInvalidated(getClass(), link);
			link.neighborAdministrativeState = admState;
			return this;
		}
		

		/**
		 * Returns an immutable <code>ElementLinkData</code> value object and invalidates this builder.
		 * Subsequent invocations of the <code>build()</code> method raises an exception.
		 * @return an immutable <code>ElementLinkData</code> value object.
		 */
		public ElementLinkData build(){
			try {
				assertNotInvalidated(getClass(), link);
				return link;
			} finally {
				this.link = null;
			}
		}
		
	}
	
	@Valid
	@NotNull(message="{local_ifp_name.required}")
	@JsonbProperty("local_ifp_name")
	private InterfaceName localIfpName;
	
	@Valid
	@NotNull(message="{local_mac_address.required}")
	@JsonbProperty("local_mac_address")
	private MACAddress localMac;
	
	@Valid
	@NotNull(message="{neighbor_mac_address.required}")
	@JsonbProperty("neighbor_mac_address")
	@JsonbTypeAdapter(MACAddressAdapter.class)
	private MACAddress neighborMac;

	@Valid
	@NotNull(message="{neighbor_ifp_name.required}")
	@JsonbProperty("neighbor_ifp_name")
	private InterfaceName neighborIfpName;

	
	@Valid
	@NotNull(message="{neighbor_element_name.required}")
	@JsonbProperty("neighbor_element_name")
	private ElementName neighborElementName;

	@Valid
	@JsonbProperty("neighbor_element_id")
	private ElementId neighborElementId;
	
	@Valid
	@JsonbProperty("neighbor_group_id")
	private ElementGroupId neighborElementGroupId;
	
	@Valid
	@JsonbProperty("neighbor_group_name")
	private ElementGroupName neighborElementGroupName;

	
	@Valid
	@JsonbProperty("neighbor_group_type")
	private ElementGroupType neighborElementGroupType;


	@JsonbProperty("neighbor_operational_state")
	@NotNull(message="{local_operational_state.required}")
	private OperationalState neighborOperationalState;

	@JsonbProperty("local_operational_state")
	@NotNull(message="{local_operational_state.required}")
	private OperationalState localOperationalState;

	@JsonbProperty("neighbor_administrative_state")
	@NotNull(message="{neighbor_administrative_state.required}")
	private AdministrativeState neighborAdministrativeState;

	@JsonbProperty("local_administrative_state")
	@NotNull(message="{local_administrative_state.required}")
	private AdministrativeState localAdministrativeState;
	
	@JsonbProperty("neighbor_bandwidth")
	private Bandwidth neighborBandwidth;

	@JsonbProperty("local_bandwidth")
	private Bandwidth localBandwidth;

	/**
	 * Returns the local interface name.
	 * @return the local interface name.
	 */
	public InterfaceName getLocalIfpName() {
		return localIfpName;
	}
	
	/**
	 * Returns the neighbor interface name.
	 * @return the neighbor interface name.
	 */
	public InterfaceName getNeighborIfpName() {
		return neighborIfpName;
	}
	
	/**
	 * Returns the local MAC address.
	 * @return the local MAC address.
	 */
	public MACAddress getLocalMac() {
		return localMac;
	}
	
	/**
	 * Returns the neighbor MAC address.
	 * @return the neighbor MAC address.
	 */
	public MACAddress getNeighborMac() {
		return neighborMac;
	}
	
	/**
	 * Returns the neighbor element ID.
	 * @return the neighbor element ID.
	 */
	public ElementId getNeighborElementId() {
		return neighborElementId;
	}
	
	/**
	 * Returns the neighbor element group name.
	 * @return the neighbor element group name.
	 */
	public ElementGroupName getNeighborElementGroupName() {
		return neighborElementGroupName;
	}
	
	/**
	 * Returns the neighbor element group type.
	 * @return the neighbor element group type.
	 */
	public ElementGroupType getNeighborElementGroupType() {
		return neighborElementGroupType;
	}
	
	/**
	 * Returns the neighbor element name.
	 * @return the neighbor element name.
	 */
	public ElementName getNeighborElementName() {
		return neighborElementName;
	}
	
	/**
	 * Returns the local administrative state.
	 * @return the local administrative state.
	 */
	public AdministrativeState getLocalAdministrativeState() {
		return localAdministrativeState;
	}

	/**
	 * Returns the local operational state.
	 * @return the local operational state.
	 */
	public OperationalState getLocalOperationalState() {
		return localOperationalState;
	}

	/**
	 * Returns the neighbor administrative state.
	 * @return the neighbor administrative state.
	 */
	public AdministrativeState getNeighborAdministrativeState() {
		return neighborAdministrativeState;
	}

	/**
	 * Returns the neighbor operational state.
	 * @return the neighbor operational state.
	 */
	public OperationalState getNeighborOperationalState() {
		return neighborOperationalState;
	}
	
	/**
	 * Returns the locally configured bandwidth.
	 * @return the locally configured bandwidth.
	 */
	public Bandwidth getLocalBandwidth() {
		return localBandwidth;
	}
	
	/**
	 * Returns the neighbor element group ID.
	 * @return the neighbor element group ID.
	 */
	public ElementGroupId getNeighborElementGroupId() {
		return neighborElementGroupId;
	}
	
	/**
     * Returns the remotely configured bandwidth.
     * @return the remotely configured bandwidth.
     */
	public Bandwidth getNeighborBandwidth() {
		return neighborBandwidth;
	}
	
}

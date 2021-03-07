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
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import io.leitstand.commons.model.ValueObject;

/**
 * Logical interface settings.
 */
public class ElementLogicalInterfaceData extends ValueObject {

	/**
	 * Returns a builder for an immutable <code>ElementLogicalInterfaceData</code> value object.
	 * @return a builder for an immutable <code>ElementLogicalInterfaceData</code> value object.
	 */
	public static Builder newElementLogicalInterfaceData() {
		return new Builder();
	}

	
	/**
	 * A builder for an immutable <code>ElementLogicalInterfaceData</code> value object.
	 */
	public static class Builder{
		
		private ElementLogicalInterfaceData data = new ElementLogicalInterfaceData();
		
		/**
		 * Sets the interface name.
		 * @param iflName the interface name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withInterfaceName(InterfaceName iflName){
			assertNotInvalidated(getClass(), data);
			data.iflName = iflName;
			return this;
		}
		
		/**
		 * Sets the interface alias.
		 * @param name the interface alias.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withInterfaceAlias(String alias){
			assertNotInvalidated(getClass(), data);
			data.iflAlias = alias;
			return this;
		}
		
		/**
		 * Sets the routing instance name.
		 * @param routingInstance the routing instance name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withRoutingInstance(RoutingInstanceName routingInstance) {
			assertNotInvalidated(getClass(),data);
			data.routingInstance = routingInstance;
			return this;
		}
		
		/**
		 * Sets the IP addresses bound to this logical interface.
		 * @param ifas the address interfaces.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withAddressInterfaces(AddressInterface.Builder... ifas) {
			return withAddressInterfaces(stream(ifas)
										.map(AddressInterface.Builder::build)
										.collect(toList()));
		}

		/**
		 * Sets the IP addresses bound to this logical interface.
		 * @param ifas the address interfaces.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withAddressInterfaces(AddressInterface... ifas) {
			return withAddressInterfaces(asList(ifas));
		}
		
		/**
		 * Sets the IP addresses bound to this logical interface.
		 * @param ifas the address interfaces.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withAddressInterfaces(List<AddressInterface> ifas){
			assertNotInvalidated(getClass(), data);
			data.addresses = unmodifiableList(new LinkedList<>(ifas));
			return this;
		}

		/**
		 * Sets the configured VLANs.
		 * @param vlans the configured VLANs.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withVlans(VlanTag.Builder... vlans) {
			return withVlans(stream(vlans)
							 .map(VlanTag.Builder::build)
							 .collect(toList()));
		}
		
		/**
		 * Sets the configured VLANS.
		 * @param vlans the configured VLANs.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withVlans(List<VlanTag> vlans) {
			assertNotInvalidated(getClass(), data);
			data.vlans = new ArrayList<>(vlans);
			return this;
		}
		
		/**
		 * Sets the used physical interfaces.
		 * @param ifps the physical interfaces.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withPhysicalInterfaces(PhysicalInterface.Builder... ifps){
			return withPhysicalInterfaces(stream(ifps)
										 .map(PhysicalInterface.Builder::build)
										 .collect(toList()));
		}
		
		/**
		 * Sets the used physical interfaces.
		 * @param ifps the physical interfaces.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withPhysicalInterfaces(PhysicalInterface... ifps){
			return withPhysicalInterfaces(asList(ifps));
		}
		
		/**
		 * Sets the used physical interfaces.
		 * @param ifps the used physical interfaces.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withPhysicalInterfaces(Collection<PhysicalInterface> ifps){
			assertNotInvalidated(getClass(), data);
			data.physicalInterfaces = unmodifiableList(new LinkedList<>(ifps));
			return this;
		}

		/**
		 * Sets the operational state of the logical interface.
		 * @param opState the operational state.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withOperationalState(OperationalState opState) {
			assertNotInvalidated(getClass(), data);
			data.operationalState = opState;
			return this;
		}

		/**
		 * Sets the administrative state of the logical interface.
		 * @param admState the administrative state.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withAdministrativeState(AdministrativeState admState) {
			assertNotInvalidated(getClass(), data);
			data.administrativeState = admState;
			return this;
		}
		
		
		/**
		 * Creates an immutable <code>ElementLogicalInterfaceData</code> value object and invalidates this builder.
		 * Subsequent invocations of the <code>build()</code> raise an exception.
		 * @return the immutable <code>ElementLogicalInterfaceData</code> value object.
		 */
		public ElementLogicalInterfaceData build() {
			try{
				assertNotInvalidated(getClass(), data);
				return data;
			} finally {
				this.data = null;
			}
		}

	}
	
	private InterfaceName iflName;
	private String iflAlias;
	
	private RoutingInstanceName routingInstance;
	
	private OperationalState operationalState;

	private AdministrativeState administrativeState;
	
	private List<AddressInterface> addresses;
	private List<VlanTag> vlans;
	
	private List<PhysicalInterface> physicalInterfaces = emptyList();

	/**
	 * Returns the logical interface name.
	 * @return the logical interface name.
	 */
	public InterfaceName getIflName() {
		return iflName;
	}
	
	/**
	 * Returns the assigned IP addresses in CIDR notation.
	 * @return the assigned IP addresses.
	 */
	public List<AddressInterface> getAddresses() {
		return unmodifiableList(addresses);
	}
	
	/**
	 * Returns the assigned VLANs.
	 * @return the assigned VLANs.
	 */
	public List<VlanTag> getVlans(){
		return unmodifiableList(vlans);
	}
	
	/**
	 * Returns the physical interfaces used by this logical interface.
	 * @return the used physical interfaces.
	 */
	public List<PhysicalInterface> getPhysicalInterfaces() {
		return unmodifiableList(physicalInterfaces);
	}
	
	/**
	 * Returns the operational state of the logical interface.
	 * @return the operational state of the logical interface.
	 */
	public OperationalState getOperationalState() {
		return operationalState;
	}
	
	/**
	 * Returns the administrative state of the logical interface.
	 * @return the administrative state of the logical interface.
	 */
	public AdministrativeState getAdministrativeState() {
		return administrativeState;
	}
	
	/**
	 * Returns the routing instance name.
	 * @return the routing instance name.
	 */
	public RoutingInstanceName getRoutingInstance() {
		return routingInstance;
	}

	/**
	 * Returns the logical interface alias.
	 * @return the logical interface alias.
	 */
	public String getIflAlias() {
		return iflAlias;
	}
	

}

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
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import io.leitstand.commons.model.ValueObject;


/**
 * A submission to store a new logical interface of a certain element in the resource inventory.
 */
public class ElementLogicalInterfaceSubmission extends ValueObject {

	public static Builder newElementLogicalInterfaceSubmission() {
		return new Builder();
	}
	
	public static class Builder {
		
		private ElementLogicalInterfaceSubmission submission = new ElementLogicalInterfaceSubmission();
		
		public Builder withIflName(InterfaceName iflName) {
			assertNotInvalidated(getClass(), submission);
			submission.iflName = iflName;
			return this;
		}

		public Builder withIfcName(InterfaceName ifcName) {
			assertNotInvalidated(getClass(), submission);
			submission.ifcName = ifcName;
			return this;
		}
		
		public Builder withInterfaceAlias(String alias) {
			assertNotInvalidated(getClass(), submission);
			submission.iflAlias = alias;
			return this;
		}
		
		public Builder withAddressInterfaces(AddressInterface.Builder... ifas) {
			return withAddressInterfaces(stream(ifas)
										 .map(AddressInterface.Builder::build)
										 .collect(toList()));
		}
		
		public Builder withAddressInterfaces(List<AddressInterface> ifas) {
			assertNotInvalidated(getClass(), submission);
			submission.addresses = new ArrayList<>(ifas);
			return this;
		}
		
		public Builder withOperationalState(OperationalState state) {
			assertNotInvalidated(getClass(), submission);
			submission.operationalState = state;
			return this;
		}
		
		public Builder withAdministrativeState(AdministrativeState state) {
			assertNotInvalidated(getClass(), submission);
			submission.administrativeState = state;
			return this;
		}
		
		public Builder withRoutingInstanceName(RoutingInstanceName name) {
			assertNotInvalidated(getClass(), submission);
			submission.routingInstance = name;
			return this;
		}
		
		public Builder withVlans(VlanTag.Builder... vlans) {
			return withVlans(stream(vlans)
							 .map(VlanTag.Builder::build)
							 .collect(toList()));
		}

		public Builder withVlans(List<VlanTag> vlans) {
			assertNotInvalidated(getClass(), submission);
			submission.vlans = new ArrayList<>(vlans);
			return this;
		}
		
		public ElementLogicalInterfaceSubmission build() {
			try {
				assertNotInvalidated(getClass(), submission);
				return submission;
			} finally {
				this.submission = null;
			}
		}
		
	}
	
	private InterfaceName iflName;

	private String  iflAlias;
	
	private List<AddressInterface> addresses = emptyList();
	
	private InterfaceName ifcName;
	
	private OperationalState operationalState;
	
	private AdministrativeState administrativeState;
	
	private RoutingInstanceName routingInstance;
	
	private List<VlanTag> vlans = emptyList();

	/**
	 * Returns the logical interface name.
	 * @return the logical interface name.
	 */
	public InterfaceName getIflName() {
		return iflName;
	}
	
	/**
	 * Returns the interface alias.
	 * @return the interface alias or <code>null</code> if no alias exists.
	 */
	public String getInterfaceAlias() {
		return iflAlias;
	}
	
	/**
	 * Returns the assigned IP addresses.
	 * @return the assigned IP addresses.
	 */
	public List<AddressInterface> getAddresses() {
		return addresses;
	}
	
	/**
	 * Returns the container interface name.
	 * <p>
	 * The container interface maps physical interfaces to logical interfaces. 
	 * A single logical interface can leverage multiple physical interfaces. 
	 * The container interface contains all physical interfaces leveraged by a
	 * single logical interface. 
	 * </p>
	 * @return the container interface name.
	 */
	public InterfaceName getContainerInterfaceName() {
		return ifcName;
	}

	/**
	 * Returns the operational state of the logical interface
	 * @return the operational state
	 */
	public OperationalState getOperationalState() {
		return operationalState;
	}
	
	/**
	 * Returns the administrative state of the logical interface.
	 * @return the administrative state
	 */
	public AdministrativeState getAdministrativeState() {
		return administrativeState;
	}
	
	/**
	 * Returns the name of the routing instance this logical interface is associated with.
	 * @return the routing instance name or <code>null</code> if no routing instance assignment exist.
	 */
	public RoutingInstanceName getRoutingInstance() {
		return routingInstance;
	}
	
	/**
	 * Returns the VLANs this logical interface is assigned to or an empty list if no VLAN assignment exists.
	 * @return the list of assigned VLANS.
	 */
	public List<VlanTag> getVlans() {
		return vlans;
	}
}

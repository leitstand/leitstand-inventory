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

import javax.json.bind.annotation.JsonbProperty;

import io.leitstand.commons.model.ValueObject;

public class PhysicalInterface extends ValueObject implements Comparable<PhysicalInterface>{
	

	 public static Builder newPhysicalInterfaceInfo() {
		 return new Builder();
	 }
	
	public static class Builder {
		
		private PhysicalInterface ifp = new PhysicalInterface();
		
		public Builder withIfpName(InterfaceName name) {
			ifp.ifpName = name;
			return this;
		}

		public Builder withIfpAlias(String alias) {
			ifp.ifpAlias = alias;
			return this;
		}
		
		public Builder withCategory(String category) {
			ifp.category = category;
			return this;
		}
		
		
		public Builder withOperationalState(OperationalState opState) {
			ifp.operationalState = opState;
			return this;
		}

		public Builder withAdministrativeState(AdministrativeState admState) {
			ifp.administrativeState = admState;
			return this;
		}
		
		public Builder withMacAddress(MACAddress macAddress) {
			ifp.macAddress = macAddress;
			return this;
		}
		
		public PhysicalInterface build() {
			try {
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
	
	
	public OperationalState getOperationalState() {
		return operationalState;
	}
	
	public AdministrativeState getAdministrativeState() {
		return administrativeState;
	}
	
	public InterfaceName getIfpName() {
		return ifpName;
	}
	
	public MACAddress getMacAddress() {
		return macAddress;
	}
	
	public String getIfpAlias() {
		return ifpAlias;
	}
	
	public String getCategory() {
		return category;
	}
	
	@Override
	public int compareTo(PhysicalInterface ifc) {
		return getIfpName().compareTo(ifc.getIfpName());
	}

	
}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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
		
		public Builder withIfpClass(String ifpClass) {
			ifp.ifpClass = ifpClass;
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
		
		public Builder withMtuSize(int mtuSize) {
			ifp.mtuSize = mtuSize;
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
	
	private String ifpClass;
	private String ifpAlias;
	
	@JsonbProperty("mac_address")
	private MACAddress macAddress;
	
	@JsonbProperty("mtu_size")
	private int mtuSize;
	
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
	
	public int getMtuSize() {
		return mtuSize;
	}

	public String getIfpAlias() {
		return ifpAlias;
	}
	
	public String getIfpClass() {
		return ifpClass;
	}
	
	@Override
	public int compareTo(PhysicalInterface ifc) {
		return getIfpName().compareTo(ifc.getIfpName());
	}

	
}
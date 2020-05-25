package io.leitstand.inventory.service;

import io.leitstand.commons.model.ValueObject;

public class LogicalInterfaceReference extends ValueObject {

	public static Builder newLogicalInterfaceReference() {
		return new Builder();
	}
	
	public static class Builder {
		
		
		public LogicalInterfaceReference build() {
			return null;
		}
		
		
	}
	
	private InterfaceName iflName;
	private AddressInterface addressInterface;
	private AdministrativeState administrativeState;
	private OperationalState operationalState;
	private RoutingInstanceName routingInstance;
	private VlanTag vlan;
	
}

/*
 * (c) RtBrick, Inc All rights reserved, 2015 2018
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.json.bind.annotation.JsonbProperty;

import io.leitstand.commons.model.ValueObject;

/**
 * Logical interface settings.
 */
public class ElementLogicalInterfaceData extends ValueObject {

	/**
	 * Returns a builder to create an immutable <code>ElementLogicalInterfaceData</code> value object.
	 * @return a builder to create an immutable <code>ElementLogicalInterfaceData</code> value object.
	 */
	public static Builder newElementLogicalInterfaceData() {
		return new Builder();
	}

	
	/**
	 * The builder to create a <code>ElementLogicalInterfaceData</code> instance.
	 */
	public static class Builder{
		
		private ElementLogicalInterfaceData data = new ElementLogicalInterfaceData();
		
		/**
		 * Lists the interface name.
		 * @param name the interface name
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withInterfaceName(InterfaceName name){
			assertNotInvalidated(getClass(), data);
			data.name = name;
			return this;
		}
		
		/**
		 * Lists the routing instance name.
		 * @param routingInstance the routing instance name.
		 * @return a reference to this builder to continue with object creation.
		 */
		public Builder withRoutingInstance(RoutingInstanceName routingInstance) {
			assertNotInvalidated(getClass(),data);
			data.routingInstance = routingInstance;
			return this;
		}
		
		/**
		 * Lists the IP addresses bound to this logical interface.
		 * @param ifas the address interfaces
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withAddressInterfaces(AddressInterface.Builder... ifas) {
			return withAddressInterfaces(stream(ifas)
										.map(AddressInterface.Builder::build)
										.collect(toList()));
		}

		/**
		 * Lists the IP addresses bound to this logical interface.
		 * @param ifas the address interfaces
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withAddressInterfaces(AddressInterface... ifas) {
			return withAddressInterfaces(asList(ifas));
		}
		
		/**
		 * Lists the IP addresses bound to this logical interface.
		 * @param ifas the address interfaces
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withAddressInterfaces(List<AddressInterface> ifas){
			assertNotInvalidated(getClass(), data);
			data.addresses = unmodifiableList(new LinkedList<>(ifas));
			return this;
		}

		/**
		 * Lists the configured VLANs.
		 * @param vlans the configured VLANs
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withVlans(VlanTag.Builder... vlans) {
			return withVlans(stream(vlans)
							 .map(VlanTag.Builder::build)
							 .collect(toList()));
		}
		
		/**
		 * Lists the configured VLANS.
		 * @param vlans the configured VLANs
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withVlans(List<VlanTag> vlans) {
			assertNotInvalidated(getClass(), data);
			data.vlans = new ArrayList<>(vlans);
			return this;
		}
		
		/**
		 * Lists the used physical interfaces.
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withPhysicalInterfaces(PhysicalInterface.Builder... ifps){
			return withPhysicalInterfaces(stream(ifps)
										 .map(PhysicalInterface.Builder::build)
										 .collect(toList()));
		}
		
		/**
		 * Lists the used physical interfaces.
		 * @param ifps the used physical interfaces 
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withPhysicalInterfaces(PhysicalInterface... ifps){
			return withPhysicalInterfaces(asList(ifps));
		}
		
		/**
		 * Lists the used physical interfaces.
		 * @param ifps the used physical interfaces 
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withPhysicalInterfaces(Collection<PhysicalInterface> ifps){
			assertNotInvalidated(getClass(), data);
			data.pyhsicalInterfaces = unmodifiableList(new LinkedList<>(ifps));
			return this;
		}

		/**
		 * Lists the operational state of the logical interface.
		 * @param opState the operational state
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withOperationalState(OperationalState opState) {
			assertNotInvalidated(getClass(), data);
			data.operationalState = opState;
			return this;
		}

		/**
		 * Lists the administrative state of the logical interface.
		 * @param operationalState the operational state
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withAdministrativeState(AdministrativeState admState) {
			assertNotInvalidated(getClass(), data);
			data.administrativeState = admState;
			return this;
		}
		
		
		/**
		 * Returns the immutable <code>ElementLogicalInterfaceData</code> object and 
		 * invalidates this builder. Any further invocation with this builder raises an exception.
		 * @return the immutable <code>ElementLogicalInterfaceData</code> object.
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
	
	@JsonbProperty("ifl_name")
	private InterfaceName name;
	
	private RoutingInstanceName routingInstance;
	
	private OperationalState operationalState;

	private AdministrativeState administrativeState;
	
	private List<AddressInterface> addresses;
	private List<VlanTag> vlans;
	
	@JsonbProperty("physical_interfaces")
	private List<PhysicalInterface> pyhsicalInterfaces;

	/**
	 * Returns the logical interface name.
	 * @return the logical interface name.
	 */
	public InterfaceName getName() {
		return name;
	}
	
	/**
	 * Returns an unmodifiable list of assigned IP addresses in CIDR notation.
	 * @return the assigned IP addresses.
	 */
	public List<AddressInterface> getAddresses() {
		return unmodifiableList(addresses);
	}
	
	public List<VlanTag> getVlans(){
		return unmodifiableList(vlans);
	}
	
	/**
	 * Returns an unmodifiable set of physical interfaces leveraged by this logical interface.
	 * @return the physical interfaces leveraged by this logical interface.
	 */
	public List<PhysicalInterface> getPhysicalInterfaces() {
		return unmodifiableList(pyhsicalInterfaces);
	}
	
	/**
	 * Returns the logical interface operational state.
	 * @return the operational state.
	 */
	public OperationalState getOperationalState() {
		return operationalState;
	}
	
	/**
	 * Returns the logical interface administrative state.
	 * @return the administrative state.
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
	

}
/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

public class PhysicalInterfaceData extends BaseElementEnvelope {
	
	public static Builder newPhysicalInterfaceData() {
		return new Builder();
	}
	
	public static class Builder extends BaseElementEnvelopeBuilder<PhysicalInterfaceData,Builder> {
		
		protected Builder() {
			super(new PhysicalInterfaceData());
		}
		
		public Builder withIfpName(InterfaceName ifpName) {
			assertNotInvalidated(getClass(), object);
			object.ifpName = ifpName;
			return this;
		}
		
		public Builder withIfpAlias(String ifpAlias) {
			assertNotInvalidated(getClass(), object);
			object.ifpAlias = ifpAlias;
			return this;
		}
		
		public Builder withIfpClass(String ifpClass) {
			assertNotInvalidated(getClass(), object);
			object.ifpClass = ifpClass;
			return this;
		}
		
		public Builder withAdministrativeState(AdministrativeState admState) {
			assertNotInvalidated(getClass(), object);
			object.admState = admState;
			return this;
		}
		
		public Builder withOperationalState(OperationalState opState) {
			assertNotInvalidated(getClass(), object);
			object.opState = opState;
			return this;
		}

	}
	
	private InterfaceName ifpName;
	private String ifpAlias;
	private String ifpClass;
	private OperationalState opState;
	private AdministrativeState admState;
	
	public InterfaceName getIfpName() {
		return ifpName;
	}
	
	public String getIfpAlias() {
		return ifpAlias;
	}
	
	public String getIfpClass() {
		return ifpClass;
	}
	
	public OperationalState getOpState() {
		return opState;
	}
	
	public AdministrativeState getAdmState() {
		return admState;
	}

}

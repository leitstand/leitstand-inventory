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

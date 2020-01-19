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
import static io.leitstand.inventory.service.AddressInterface.AddressType.IPv4;
import static io.leitstand.inventory.service.AddressInterface.AddressType.IPv6;

import java.io.Serializable;

import javax.persistence.Embeddable;

import io.leitstand.commons.model.ValueObject;

/**
 * An address interface of a logical interface.
 */
@Embeddable
public class AddressInterface extends ValueObject implements Serializable {

	public static enum AddressType{
		IPv4,
		IPv6
	}
	
	private static final long serialVersionUID = 1L;

	/**
	 * Returns a builder to create an <code>AddressInterface</code> instance.
	 * @return a builder to create an <code>AddressInterface</code> instance.
	 */
	public static Builder newAddressInterface() {
		return new Builder();
	}
	
	/**
	 * The builder to create an <code>AddressInterface</code> instance.
	 */
	public static class Builder {
		
		private AddressInterface ifc = new AddressInterface();
		
		/**
		 * Sets the IP address
		 * @param address the IP address
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withAddress(IPvxPrefix address) {
			assertNotInvalidated(getClass(), ifc);
			ifc.address = address;
			if(address.isIPv4Prefix()) {
				ifc.addressType = IPv4;
			} else {
				ifc.addressType = IPv6;
			}
			return this;
		}

		public AddressInterface build() {
			try {
				assertNotInvalidated(getClass(), ifc);
				return ifc;
			} finally {
				this.ifc = null;
			}
		}
	}
	
	private AddressType addressType;
	
	private IPvxPrefix address;
	
	/**
	 * Returns the IP address type.
	 * @return the IP address type.
	 */
	public AddressType getAddressType() {
		return addressType;
	}
	
	/**
	 * Returns the IP address in CIDR notation.
	 * @return the IP address.
	 */
	public IPvxPrefix getAddress() {
		return address;
	}
	
}

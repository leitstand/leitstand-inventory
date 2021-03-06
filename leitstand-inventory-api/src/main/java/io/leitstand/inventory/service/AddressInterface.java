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
import static io.leitstand.inventory.service.AddressInterface.AddressType.IPV4;
import static io.leitstand.inventory.service.AddressInterface.AddressType.IPV6;
import static javax.persistence.EnumType.STRING;

import java.io.Serializable;

import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;

import io.leitstand.commons.model.ValueObject;
import io.leitstand.inventory.jpa.IPvxPrefixConverter;

/**
 * An address interface of a logical interface.
 * <p>
 * The interface addresses are provided in <a href="https://en.wikipedia.org/wiki/Classless_Inter-Domain_Routing" title="Open Classless Inter-Domain Routing wikipedia article">CIDR notation</a>
 */
@Embeddable
public class AddressInterface extends ValueObject implements Serializable {

    /**
     * Supported address types.
     */
	public static enum AddressType{
	    /** IPv4 prefix. */
		IPV4,
		/** IPv6 prefix. */
		IPV6
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
	 * A builder to create an <code>AddressInterface</code> instance.
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
				ifc.addressType = IPV4;
			} else {
				ifc.addressType = IPV6;
			}
			return this;
		}

		/**
		 * Returns the <code>AddressInterface</code> instance.
		 * @return the immutable <code>AddressInterface</code>.
		 */
		public AddressInterface build() {
			try {
				assertNotInvalidated(getClass(), ifc);
				return ifc;
			} finally {
				this.ifc = null;
			}
		}
	}
	
	@Enumerated(STRING)
	private AddressType addressType;
	@Convert(converter=IPvxPrefixConverter.class)
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

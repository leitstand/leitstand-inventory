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
import static io.leitstand.inventory.service.ElementId.randomElementId;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.leitstand.commons.model.CompositeValue;

/**
 * Request to clone an existing element.
 */
public class ElementCloneRequest extends CompositeValue{

    /**
     * Returns a builder for a clone request.
     * @return a builder for a clone request.
     */
    public static Builder newCloneElementRequest() {
		return new Builder();
	}
	
    /**
     * A builder for an immutable <code>ElementCloneRequest</code>.
     */
	public static class Builder {
		
		private ElementCloneRequest request = new ElementCloneRequest();
		
		/**
		 * Sets the element ID of the new element.
		 * @param elementId the element ID
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withElementId(ElementId elementId) {
			assertNotInvalidated(getClass(), request);
			request.elementId = elementId;
			return this;
		}

	     /**
         * Sets the element name of the new element.
         * @param elementName the element name
         * @return a reference to this builder to continue object creation
         */
		public Builder withElementName(ElementName elementName) {
			assertNotInvalidated(getClass(), request);
			request.elementName = elementName;
			return this;
		}
		
	    /**
         * Sets the element alias.
         * @param elementAlias the element alias
         * @return a reference to this builder to continue object creation
         */
		public Builder withElementAlias(ElementAlias elementAlias) {
			assertNotInvalidated(getClass(), request);
			request.elementAlias = elementAlias;
			return this;
		}
		
	    /**
         * Sets the MAC address of the management port of the new element.
         * @param macAddress the MAC address of the management port of the new element
         * @return a reference to this builder to continue object creation
         */
		public Builder withMgmtMacAddress(MACAddress macAddress) {
			assertNotInvalidated(getClass(), request);
			request.mgmtMacAddress = macAddress;
			return this;
		}
		
		/**
		 * Sets the serial number of the new element.
		 * @param serialNumber the serial number of the new element
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withSerialNumber(String serialNumber) {
			assertNotInvalidated(getClass(), request);
			request.serialNumber= serialNumber;
			return this;
		}
		
		/**
		 * Creates an immutable <code>ElementCloneRequest</code>.
		 * @return the immutable <code>ElementCloneRequest</code>.
		 */
		public ElementCloneRequest build() {
			try {
				assertNotInvalidated(getClass(), request);
				return request;
			} finally {
				this.request = null;
			}
		}
	}
	
	private ElementId elementId = randomElementId();
	@Valid
	@NotNull(message="{element_name.required}")
	private ElementName elementName;

	private ElementAlias elementAlias;
	private MACAddress mgmtMacAddress;
	private String serialNumber;

	/**
	 * Returns the element ID of the new element.
	 * @return the element ID of the new element.
	 */
	public ElementId getElementId() {
		return elementId;
	}
	
	/**
	 * Returns the element name of the new element.
	 * @return the element name of the new element.
	 */
	public ElementName getElementName() {
		return elementName;
	}
	
	/**
	 * Returns the element alias of the new element.
	 * @return the element alias of the new element.
	 */
	public ElementAlias getElementAlias() {
		return elementAlias;
	}
	
	/**
	 * Returns the MAC address of the management port of the new element.
	 * @return the MAC address of the management port of the new element.
	 */
	public MACAddress getMgmtMacAddress() {
		return mgmtMacAddress;
	}
	
	/**
	 * Returns the serial number of the new element.
	 * @return the serial number of the new element.
	 */
	public String getSerialNumber() {
		return serialNumber;
	}
	
}

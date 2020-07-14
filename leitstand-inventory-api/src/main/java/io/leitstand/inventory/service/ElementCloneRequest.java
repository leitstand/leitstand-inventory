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

import io.leitstand.commons.model.CompositeValue;

public class ElementCloneRequest extends CompositeValue{

	public static Builder newCloneElementRequest() {
		return new Builder();
	}
	
	public static class Builder {
		
		private ElementCloneRequest request = new ElementCloneRequest();
		
		public Builder withElementId(ElementId elementId) {
			assertNotInvalidated(getClass(), request);
			request.elementId = elementId;
			return this;
		}
		
		public Builder withElementName(ElementName elementName) {
			assertNotInvalidated(getClass(), request);
			request.elementName = elementName;
			return this;
		}
		
		public Builder withElementAlias(ElementAlias elementAlias) {
			assertNotInvalidated(getClass(), request);
			request.elementAlias = elementAlias;
			return this;
		}
		
		public Builder withMgmtMacAddress(MACAddress macAddress) {
			assertNotInvalidated(getClass(), request);
			request.mgmtMacAddress = macAddress;
			return this;
		}
		
		public Builder withSerialNumber(String serialNumber) {
			assertNotInvalidated(getClass(), request);
			request.serialNumber= serialNumber;
			return this;
		}
		
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
	private ElementName elementName;
	private ElementAlias elementAlias;
	private MACAddress mgmtMacAddress;
	private String serialNumber;

	public ElementId getElementId() {
		return elementId;
	}
	
	public ElementName getElementName() {
		return elementName;
	}
	
	public ElementAlias getElementAlias() {
		return elementAlias;
	}
	
	public MACAddress getMgmtMacAddress() {
		return mgmtMacAddress;
	}
	
	public String getSerialNumber() {
		return serialNumber;
	}
	
}

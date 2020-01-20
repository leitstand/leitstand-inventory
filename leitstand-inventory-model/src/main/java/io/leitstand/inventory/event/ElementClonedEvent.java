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
package io.leitstand.inventory.event;

import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.MACAddress;

public class ElementClonedEvent extends ElementEvent{

	public static Builder newElementClonedEvent() {
		return new Builder();
	}
	
	public static class Builder extends ElementEventBuilder<ElementClonedEvent, Builder>{
		
		public Builder() {
			super(new ElementClonedEvent());
		}
		
		public Builder withSerialNumber(String serialNumber) {
			object.serialNumber = serialNumber;
			return this;
		}

		public Builder withMacAddress(MACAddress macAddress) {
			object.macAddress = macAddress;
			return this;
		}
		
		public Builder withCloneElementId(ElementId elementId) {
			object.cloneId = elementId;
			return this;
		}
		
		public Builder withCloneElementName(ElementName elementName) {
			object.cloneName = elementName;
			return this;
		}
		
		public Builder withCloneSerialNumber(String serialNumber) {
			object.cloneSerialNumber = serialNumber;
			return this;
		}
		
		public Builder withCloneMacAddress(MACAddress macAddress) {
			object.cloneMacAddress = macAddress;
			return this;
		}
		
	}
	
	
	private String serialNumber;
	private MACAddress  macAddress;
	private ElementId cloneId;
	private ElementName cloneName;
	private String cloneSerialNumber;
	private MACAddress cloneMacAddress;
	
	
	public String getSerialNumber() {
		return serialNumber;
	}
	
	public MACAddress getMacAddress() {
		return macAddress;
	}
	
	public ElementId getCloneId() {
		return cloneId;
	}
	
	public ElementName getCloneName() {
		return cloneName;
	}
	
	public String getCloneSerialNumber() {
		return cloneSerialNumber;
	}
	
	public MACAddress getCloneMacAddress() {
		return cloneMacAddress;
	}
}

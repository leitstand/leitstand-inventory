/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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

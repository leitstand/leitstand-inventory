/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.inventory.service.ElementId.randomElementId;

import io.leitstand.commons.model.CompositeValue;

public class ElementCloneRequest extends CompositeValue{

	private ElementId elementId = randomElementId();
	private ElementName elementName;
	private MACAddress mgmtMacAddress;
	private String serialNumber;

	public ElementId getElementId() {
		return elementId;
	}
	
	public ElementName getElementName() {
		return elementName;
	}
	
	public MACAddress getMgmtMacAddress() {
		return mgmtMacAddress;
	}
	
	public String getSerialNumber() {
		return serialNumber;
	}
	
}
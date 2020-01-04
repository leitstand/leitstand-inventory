/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

public interface ElementRackService {

	ElementRack getElementRack(ElementId elementId);
	ElementRack getElementRack(ElementName elementName);
	void storeElementRackMountPoint(ElementId elementId, 
									ElementRackLocation mountPoint);
	void storeElementRackLocation(ElementName elementName, 
									ElementRackLocation mountPoint);
	void removeElementRackLocation(ElementId elementId);
	void removeElementRackLocation(ElementName elementName);
}

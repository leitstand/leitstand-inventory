/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

public interface RetireElementService {

	void retireElement(ElementId elementId);
	void retireElement(ElementName elementName);
	
}
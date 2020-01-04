/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

public interface CloneElementService {

	
	ElementId cloneElement(ElementId source, ElementCloneRequest request );
	ElementId cloneElement(ElementName source, ElementCloneRequest request);
	
	
	
}

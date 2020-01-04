/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.visitor;

import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;

public interface ElementTransformationService {

	<T> T apply(ElementId elementId,
				ElementTransformation<T> transformation);
	
	<T> T apply(ElementName elementName,
				ElementTransformation<T> transformation);
	
}

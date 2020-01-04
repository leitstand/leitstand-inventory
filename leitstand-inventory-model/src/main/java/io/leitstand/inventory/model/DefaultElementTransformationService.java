/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import javax.inject.Inject;

import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.visitor.ElementTransformation;
import io.leitstand.inventory.visitor.ElementTransformationService;

@Service
public class DefaultElementTransformationService implements ElementTransformationService {

	@Inject
	private ElementProvider elements;
	
	@Inject
	private ElementTransformationManager manager;
	
	@Override
	public <T> T apply(ElementId elementId, 
					   ElementTransformation<T> transformation) {
		Element element = elements.fetchElement(elementId);
		return manager.apply(element,
							 transformation);
	}

	@Override
	public <T> T apply(ElementName elementName, 
					   ElementTransformation<T> transformation) {
		Element element = elements.fetchElement(elementName);
		return manager.apply(element,
					 		 transformation);
	}

}
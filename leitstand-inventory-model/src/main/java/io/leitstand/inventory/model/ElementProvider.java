/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.model.Element.findElementById;
import static io.leitstand.inventory.model.Element.findElementByName;
import static io.leitstand.inventory.service.ReasonCode.IVT0300E_ELEMENT_NOT_FOUND;
import static java.lang.String.format;

import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;

@Dependent
public class ElementProvider {

	private static final Logger LOG = Logger.getLogger(ElementProvider.class.getName());
	
	private Repository repository;

	protected ElementProvider() {
		// CDI
	}
	
	@Inject
	public ElementProvider(@Inventory Repository repository) {
		this.repository = repository;
	}
	


	public Element tryFetchElement(ElementId elementId) {
		return repository.execute(findElementById(elementId));
	}
	
	public Element fetchElement(ElementId elementId) {
		Element element = tryFetchElement(elementId);
		if(element == null) {
			LOG.fine(() -> format("%s: Element %s not found.", 
								  IVT0300E_ELEMENT_NOT_FOUND.getReasonCode(),
								  elementId));
			throw new EntityNotFoundException(IVT0300E_ELEMENT_NOT_FOUND,
											  elementId);
		}
		return element;
	}
	
	public Element tryFetchElement(ElementName elementName) {
		return repository.execute(findElementByName(elementName));
	}
	
	public Element fetchElement(ElementName elementName) {
		Element element = tryFetchElement(elementName);
		if(element == null) {
			LOG.fine(() -> format("%s: Element %s not found.", 
								  IVT0300E_ELEMENT_NOT_FOUND.getReasonCode(),
								  elementName));
			throw new EntityNotFoundException(IVT0300E_ELEMENT_NOT_FOUND,
											  elementName);
		}
		return element;
	}


}
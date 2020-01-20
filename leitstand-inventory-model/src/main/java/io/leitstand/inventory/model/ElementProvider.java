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

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import javax.inject.Inject;

import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementLinkService;
import io.leitstand.inventory.service.ElementLinks;
import io.leitstand.inventory.service.ElementName;

@Service
public class DefaultElementLinksService implements ElementLinkService {

	@Inject
	private ElementProvider elements;
	@Inject
	private ElementLinksManager manager;

	@Override
	public ElementLinks getElementLinks(ElementId id) {
		Element element = elements.fetchElement(id);
		return manager.getElementLinks(element);
	}

	@Override
	public ElementLinks getElementLinks(ElementName name) {
		Element element = elements.fetchElement(name);
		return manager.getElementLinks(element);
	}

}
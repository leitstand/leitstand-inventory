/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import java.util.List;

import javax.inject.Inject;

import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementService;
import io.leitstand.inventory.service.ElementSettings;
import io.leitstand.inventory.service.OperationalState;

@Service
public class DefaultElementService implements ElementService {

	private ElementProvider elements;
	
	private ElementManager manager;
	
	
	public DefaultElementService() {
		// EJB constructor
	}
	
	@Inject
	DefaultElementService(ElementManager manager,
			  			  ElementProvider elements){
		this.elements  = elements;
		this.manager = manager;
	}

	@Override
	public List<ElementSettings> findElements(String filter, int offset, int items) {
		return manager.findElements(filter,offset,items);
	}

	@Override
	public void removeElement(ElementId elementId) {
		Element element = elements.tryFetchElement(elementId);
		if(element != null) {
			manager.remove(element);
		}
	}

	@Override
	public void removeElement(ElementName elementName) {
		Element element = elements.tryFetchElement(elementName);
		if(element != null) {
			manager.remove(element);
		}
	}

	@Override
	public void forceRemoveElement(ElementId elementId) {
		Element element = elements.tryFetchElement(elementId);
		if(element != null) {
			manager.forceRemove(element);
		}
	}

	@Override
	public void forceRemoveElement(ElementName elementName) {
		Element element = elements.tryFetchElement(elementName);
		if(element != null) {
			manager.forceRemove(element);
		}
	}
	
	
	@Override
	public void updateElementOperationalState(ElementId id, OperationalState state) {
		Element element = elements.fetchElement(id);
		manager.updateElementOperationalState(element,state);
	}

	@Override
	public void updateElementOperationalState(ElementName name, OperationalState state) {
		Element element = elements.fetchElement(name);
		manager.updateElementOperationalState(element,state);
	}

}
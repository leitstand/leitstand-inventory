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

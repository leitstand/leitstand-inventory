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
import io.leitstand.inventory.service.ElementLogicalInterface;
import io.leitstand.inventory.service.ElementLogicalInterfaceService;
import io.leitstand.inventory.service.ElementLogicalInterfaceSubmission;
import io.leitstand.inventory.service.ElementLogicalInterfaces;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.InterfaceName;

@Service
public class DefaultElementLogicalInterfaceService implements ElementLogicalInterfaceService {
	
	@Inject
	private ElementProvider elements;
	
	@Inject
	private ElementLogicalInterfaceManager manager;

	public DefaultElementLogicalInterfaceService() {
		// CDI
	}
	
	DefaultElementLogicalInterfaceService(ElementProvider elements, 
								   ElementLogicalInterfaceManager manager){
		this.elements = elements;
		this.manager = manager;
	}

	@Override
	public ElementLogicalInterface getLogicalInterface(ElementName elementName, InterfaceName name) {
		Element element = elements.fetchElement(elementName);
		return manager.getLogicalInterface(element,name);
	}

	@Override
	public ElementLogicalInterfaces getLogicalInterfaces(ElementId elementId) {
		Element element = elements.fetchElement(elementId);
		return manager.getLogicalInterfaces(element);
	}

	@Override
	public ElementLogicalInterfaces getLogicalInterfaces(ElementName elementName) {
		Element element = elements.fetchElement(elementName);
		return manager.getLogicalInterfaces(element);
	}

	@Override
	public boolean storeLogicalInterface(ElementId elementId, ElementLogicalInterfaceSubmission ifc) {
		Element element = elements.fetchElement(elementId);
		return manager.storeLogicalInterface(element,ifc);
	}

	@Override
	public boolean storeLogicalInterface(ElementName elementName, ElementLogicalInterfaceSubmission ifc) {
		Element element = elements.fetchElement(elementName);
		return manager.storeLogicalInterface(element,ifc);
	}

	@Override
	public void storeLogicalInterfaces(ElementId elementId, List<ElementLogicalInterfaceSubmission> ifc) {
		Element element = elements.fetchElement(elementId);
		manager.storeLogicalInterfaces(element,ifc);
		
	}

	@Override
	public void storeLogicalInterfaces(ElementName elementName, List<ElementLogicalInterfaceSubmission> ifc) {
		Element element = elements.fetchElement(elementName);
		manager.storeLogicalInterfaces(element,ifc);
	}

	@Override
	public void removeLogicalInterface(ElementId elementId, InterfaceName name) {
		Element element = elements.fetchElement(elementId);
		manager.removeLogicalInterface(element,name);
	}

	@Override
	public void removeLogicalInterface(ElementName elementName, InterfaceName name) {
		Element element = elements.fetchElement(elementName);
		manager.removeLogicalInterface(element,name);
	}

	@Override
	public ElementLogicalInterface getLogicalInterface(ElementId elementId, InterfaceName name) {
		Element element = elements.fetchElement(elementId);
		return manager.getLogicalInterface(element, name);
	}

}

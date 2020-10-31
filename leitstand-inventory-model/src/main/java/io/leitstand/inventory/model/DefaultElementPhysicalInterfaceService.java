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
import io.leitstand.inventory.service.AdministrativeState;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementPhysicalInterface;
import io.leitstand.inventory.service.ElementPhysicalInterfaceFilter;
import io.leitstand.inventory.service.ElementPhysicalInterfaceNeighbor;
import io.leitstand.inventory.service.ElementPhysicalInterfaceService;
import io.leitstand.inventory.service.ElementPhysicalInterfaceSubmission;
import io.leitstand.inventory.service.ElementPhysicalInterfaces;
import io.leitstand.inventory.service.InterfaceName;
import io.leitstand.inventory.service.OperationalState;

@Service
public class DefaultElementPhysicalInterfaceService implements ElementPhysicalInterfaceService {
	
	@Inject
	private ElementProvider elements;
	
	@Inject
	private ElementPhysicalInterfaceManager inventory;

	public DefaultElementPhysicalInterfaceService() {
		// CDI
	}
	
	DefaultElementPhysicalInterfaceService(ElementProvider elements, 
								   ElementPhysicalInterfaceManager manager){
		this.elements = elements;
		this.inventory = manager;
	}

	@Override
	public ElementPhysicalInterface getPhysicalInterface(ElementId elementId, InterfaceName name) {
		Element element = elements.fetchElement(elementId);
		return inventory.getPhysicalInterface(element,name);
	}

	@Override
	public ElementPhysicalInterface getPhysicalInterface(ElementName elementName, InterfaceName name) {
		Element element = elements.fetchElement(elementName);
		return inventory.getPhysicalInterface(element,name);
	}

	@Override
	public ElementPhysicalInterfaces getPhysicalInterfaces(ElementId elementId, ElementPhysicalInterfaceFilter filter) {
		Element element = elements.fetchElement(elementId);
		return inventory.getPhysicalInterfaces(element,filter);
	}

	@Override
	public ElementPhysicalInterfaces getPhysicalInterfaces(ElementName elementName, ElementPhysicalInterfaceFilter filter) {
		Element element = elements.fetchElement(elementName);
		return inventory.getPhysicalInterfaces(element,filter);
	}

	@Override
	public boolean storePhysicalInterface(ElementId elementId, ElementPhysicalInterfaceSubmission submission) {
		Element element = elements.fetchElement(elementId);
		return inventory.storePhysicalInterface(element,submission);
	}

	@Override
	public boolean storePhysicalInterface(ElementName elementName, ElementPhysicalInterfaceSubmission submission) {
		Element element = elements.fetchElement(elementName);
		return inventory.storePhysicalInterface(element,submission);
	}

	@Override
	public void storePhysicalInterfaces(ElementId elementId, List<ElementPhysicalInterfaceSubmission> submissions) {
		Element element = elements.fetchElement(elementId);
		inventory.storePhysicalInterfaces(element,submissions);
	}

	@Override
	public void storePhysicalInterfaces(ElementName elementName, List<ElementPhysicalInterfaceSubmission> submissions) {
		Element element = elements.fetchElement(elementName);
		inventory.storePhysicalInterfaces(element,submissions);
	}

	@Override
	public void removePhysicalInterface(ElementId elementId, InterfaceName name) {
		Element element = elements.fetchElement(elementId);
		inventory.removePhysicalInterface(element,name);
	}

	@Override
	public void removePhysicalInterface(ElementName elementName, InterfaceName name) {
		Element element = elements.fetchElement(elementName);
		inventory.removePhysicalInterface(element,name);
	}

	@Override
	public void storePhysicalInterfaceNeighbor(ElementId elementId, InterfaceName ifpName, ElementPhysicalInterfaceNeighbor link) {
		Element element = elements.fetchElement(elementId);
		inventory.storePhysicalNeighborInterface(element,ifpName,link);
	}

	@Override
	public void storePhysicalInterfaceNeighbor(ElementName elementName, InterfaceName ifpName, ElementPhysicalInterfaceNeighbor link) {
		Element element = elements.fetchElement(elementName);
		inventory.storePhysicalNeighborInterface(element,ifpName,link);
	}

	@Override
	public void removePhysicalInterfaceNeighbor(ElementId elementId, InterfaceName ifpName) {
		Element element = elements.fetchElement(elementId);
		inventory.removePhysicalInterfaceNeighbor(element,ifpName);
	}

	@Override
	public void removePhysicalInterfaceNeighbor(ElementName elementName, InterfaceName ifpName) {
		Element element = elements.fetchElement(elementName);
		inventory.removePhysicalInterfaceNeighbor(element,ifpName);
	}
	
	@Override
	public void updatePhysicalInterfaceOperationalState(ElementName name, 
													   InterfaceName ifpName,
													   OperationalState opState) {
		Element element = elements.fetchElement(name);
		inventory.updatePhysicalLinkOperationalState(element,ifpName, opState);
	}

	@Override
	public void updatePhysicalInterfaceOperationalState(ElementId id, 
													   InterfaceName ifpName, 
													   OperationalState opState) {
		Element element = elements.fetchElement(id);
		inventory.updatePhysicalLinkOperationalState(element,ifpName, opState);
	}

	@Override
	public void updatePhysicalInterfaceAdministrativeState(ElementName name, 
														  InterfaceName ifpName,
														  AdministrativeState admState) {
		Element element = elements.fetchElement(name);
		inventory.updatePhysicalLinkAdministrativeState(element,ifpName, admState);

	}

	@Override
	public void updatePhysicalInterfaceAdministrativeState(ElementId id, 
														  InterfaceName ifpName,
														  AdministrativeState admState) {
		Element element = elements.fetchElement(id);
		inventory.updatePhysicalLinkAdministrativeState(element,ifpName, admState);
		
	}

}

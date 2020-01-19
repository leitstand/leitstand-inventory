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
import io.leitstand.inventory.service.ElementModule;
import io.leitstand.inventory.service.ElementModuleService;
import io.leitstand.inventory.service.ElementModules;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ModuleData;
import io.leitstand.inventory.service.ModuleName;

@Service
public class DefaultElementModuleService implements ElementModuleService {

	@Inject
	private ElementProvider elements;
	
	@Inject
	private ElementModuleManager manager;

	public DefaultElementModuleService() {
		// CDI 
	}
	
	DefaultElementModuleService(ElementProvider elements,
								ElementModuleManager modules){
		this.elements = elements;
		this.manager = modules;
	}
	
	@Override
	public ElementModules getElementModules(ElementId id) {
		Element element = elements.fetchElement(id);
		return manager.getElementModules(element);
	}

	@Override
	public ElementModules getElementModules(ElementName name) {
		Element element = elements.fetchElement(name);
		return manager.getElementModules(element);
	}

	@Override
	public void storeElementModules(ElementId id, 
									List<ModuleData> moduleDatas) {
		Element element = elements.fetchElement(id);
		manager.storeElementModules(element,
									moduleDatas);
	}

	@Override
	public boolean storeElementModule(ElementId id, 
									  ModuleName moduleName,
									  ModuleData moduleData) {
		Element element = elements.fetchElement(id);
		return manager.storeElementModule(element, 
										  moduleName, 
										  moduleData);
	}

	@Override
	public boolean storeElementModule(ElementName name, 
									  ModuleName moduleName,
									  ModuleData moduleData) {
		Element element = elements.fetchElement(name);
		return manager.storeElementModule(element, 
										  moduleName, 
										  moduleData);
	}
	
	@Override
	public void removeElementModule(ElementId elementId, 
									ModuleName moduleName) {
		Element element = elements.fetchElement(elementId);
		manager.removeElementModule(element,
									moduleName);
	}

	@Override
	public void removeElementModule(ElementName elementName, 
									ModuleName moduleName) {
		Element element = elements.fetchElement(elementName);
		manager.removeElementModule(element,
									moduleName);
		
	}

	@Override
	public void storeElementModules(ElementName elementName, 
									List<ModuleData> modules) {
		Element element = elements.fetchElement(elementName);
		manager.storeElementModules(element, 
									modules);
	}

	@Override
	public ElementModule getElementModule(ElementId id, 
								   String serialNumber) {
		Element element = elements.fetchElement(id);
		return manager.getElementModule(element,
										serialNumber);
	}

	@Override
	public ElementModule getElementModule(ElementName name, 
										  String serialNumber) {
		Element element = elements.fetchElement(name);
		return manager.getElementModule(element,serialNumber);
	}

	@Override
	public ElementModule getElementModule(ElementId id, ModuleName moduleName) {
		Element element = elements.fetchElement(id);
		return manager.getElementModule(element, moduleName);
	}

	@Override
	public ElementModule getElementModule(ElementName name, ModuleName moduleName) {
		Element element = elements.fetchElement(name);
		return manager.getElementModule(element, moduleName);
	}

}

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

import static io.leitstand.commons.messages.MessageFactory.createMessage;
import static io.leitstand.commons.model.ObjectUtil.optional;
import static io.leitstand.inventory.model.Element_Module.findModule;
import static io.leitstand.inventory.model.Element_Module.findModules;
import static io.leitstand.inventory.service.AdministrativeState.ACTIVE;
import static io.leitstand.inventory.service.AdministrativeState.RETIRED;
import static io.leitstand.inventory.service.AdministrativeState.UNKNOWN;
import static io.leitstand.inventory.service.ElementModule.newElementModule;
import static io.leitstand.inventory.service.ElementModules.newElementModules;
import static io.leitstand.inventory.service.ModuleData.newModuleData;
import static io.leitstand.inventory.service.ReasonCode.IVT0310E_ELEMENT_MODULE_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0312I_ELEMENT_MODULE_REMOVED;
import static java.lang.String.format;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementModule;
import io.leitstand.inventory.service.ElementModules;
import io.leitstand.inventory.service.ModuleData;
import io.leitstand.inventory.service.ModuleName;

@Dependent
public class ElementModuleManager {

	private static final Logger LOG = Logger.getLogger(ElementModuleManager.class.getName());
	
	private Repository repository;
	private Messages messages;
	
	@Inject
	protected ElementModuleManager(@Inventory Repository repository,
								  Messages messages){
		this.repository = repository;
		this.messages   = messages;
	}
	
	public ElementModules getElementModules(Element element) {
		List<ModuleData> modules = new LinkedList<>();
		
		for(Element_Module module : repository.execute(findModules(element))){
			modules.add(moduleData(module));
		}
		
		return newElementModules()
			   .withGroupId(element.getGroupId())
			   .withGroupName(element.getGroupName())
			   .withGroupType(element.getGroupType())
			   .withElementId(element.getElementId())
			   .withElementName(element.getElementName())
			   .withElementAlias(element.getElementAlias())
			   .withAdministrativeState(element.getAdministrativeState())
			   .withOperationalState(element.getOperationalState())
			   .withDateModified(element.getDateModified())
			   .withModules(modules)
			   .build();
	}

	private ModuleData moduleData(Element_Module module) {
		return newModuleData()
			   .withModuleName(module.getModuleName())
			   .withModuleClass(module.getModuleClass())
			   .withSerialNumber(module.getSerialNumber())
			   .withAssetId(module.getAssetId())
			   .withDescription(module.getDescription())
			   .withFieldReplaceableUnit(module.isFieldReplaceableUnit())
			   .withFirmwareRevision(module.getFirmwareRevision())
			   .withHardwareRevision(module.getHardwareRevision())
			   .withSoftwareRevision(module.getSoftwareRevision())
			   .withManufacturerName(module.getManufacturerName())
			   .withManufacturingDate(module.getDateManufactured())
			   .withModelName(module.getModelName())
			   .withVendorType(module.getVendorType())
			   .withAdministrativeState(module.getAdministrativeState())
			   .withParentModule(optional(module.getParentModule(),Element_Module::getModuleName))
			   .withLocation(module.getLocation())
			   .build();
	}

	public void removeElementModule(Element element, ModuleName moduleName) {
		Element_Module module = repository.execute(findModule(element,moduleName));
		if(module != null){
			repository.remove(module);
			LOG.fine(() -> format("%s: Removed module %s (%s) from element %s",
								  IVT0312I_ELEMENT_MODULE_REMOVED.getReasonCode(),
								  module.getModuleName(),
								  module.getSerialNumber(),
								  element.getElementName()));
			messages.add(createMessage(IVT0312I_ELEMENT_MODULE_REMOVED, 
									   element.getElementName(),
									   module.getModuleName()));		
		}
	}

	public boolean storeElementModule(Element element, 
									  ModuleName moduleName, 
									  ModuleData module) {
		Element_Module elementModule = repository.execute(findModule(element, 
																	 module.getModuleName()));
		
		if(elementModule == null) {
			elementModule = repository.execute(findModule(element,moduleName));
		}
		if(elementModule == null){
			elementModule = new Element_Module(element, 
											   module.getModuleName());
			repository.add(elementModule);
			populateModule(element, 
						   module, 
						   elementModule);
			return true;
		}
		populateModule(element, 
					   module, 
					   elementModule);
		return false;
	}

	protected void populateModule(Element element, ModuleData module, Element_Module elementModule) {
		elementModule.setAdministrativeState(module.getAdministrativeState());
		if(module.getAdministrativeState() == null || UNKNOWN.equals(module.getAdministrativeState())) {
			// It must be an active module, if it was reported by an element with unknown administrative state.
			elementModule.setAdministrativeState(ACTIVE);
		}
		elementModule.setAssetId(module.getAssetId());
		elementModule.setDateManufactured(module.getDateManufactured());
		elementModule.setDescription(module.getDescription());
		elementModule.setFieldReplaceableUnit(module.isFieldReplaceableUnit());
		elementModule.setFirmwareRevision(module.getFirmwareRevision());
		elementModule.setHardwareRevision(module.getHardwareRevision());
		elementModule.setManufacturerName(module.getManufacturerName());
		elementModule.setModelName(module.getModelName());
		elementModule.setModuleClass(module.getModuleClass());
		elementModule.setLocation(module.getLocation());
		elementModule.setSerialNumber(module.getSerialNumber());
		elementModule.setSoftwareRevision(module.getSoftwareRevision());
		elementModule.setVendorType(module.getVendorType());
		if(module.getParentModule() != null) {
			Element_Module parent = repository.execute(findModule(element,
																  module.getParentModule()));
			if(parent == null) {
				// Create stub record for missing parent modules.
				parent = new Element_Module(element,module.getParentModule());
				repository.add(parent);
			}
			elementModule.setParentModule(parent);
		} else {
			elementModule.setParentModule(null);
		}
	}

	public void storeElementModules(Element element, 
								    List<ModuleData> modules) {
		Map<ModuleName,Element_Module> elementModules = new HashMap<>();
		for(Element_Module elementModule : repository.execute(findModules(element))){
			elementModules.put(elementModule.getModuleName(),elementModule);
		}
		
		for(ModuleData module : modules){
			Element_Module elementModule = elementModules.remove(module.getModuleName());
			if(elementModule == null){
				elementModule = new Element_Module(element,
												   module.getModuleName());
				repository.add(elementModule);
			}
			populateModule(element, 
						   module, 
						   elementModule);
		}
		
		for(Element_Module _unit : elementModules.values()){
			// All unit still available in _units do not exist any longer and have to be removed,
			// as they have no entry in units.
			if(_unit.isActive()){
				_unit.setAdministrativeState(RETIRED);
			}
		}
	}

	public ElementModule getElementModule(Element element, String serialNumber) {
		Element_Module module = repository.execute(findModule(element, serialNumber));
		if(module == null){
			LOG.fine(() -> format("%s: Module with serial-number %s does not exist on element %s", 
								  IVT0310E_ELEMENT_MODULE_NOT_FOUND.getReasonCode(),
								  serialNumber,
								  element.getElementName()));
			throw new EntityNotFoundException(IVT0310E_ELEMENT_MODULE_NOT_FOUND,
											  element.getElementName(),
											  serialNumber);
		}
		
		return mapModule(element, 
						 module);
	}

	private ElementModule mapModule(Element element, Element_Module module) {

		return newElementModule()
			   .withGroupId(element.getGroup().getGroupId())
			   .withGroupName(element.getGroup().getGroupName())
			   .withGroupType(element.getGroup().getGroupType())
			   .withElementId(element.getElementId())
			   .withElementName(element.getElementName())
			   .withElementAlias(element.getElementAlias())
			   .withElementRole(element.getElementRoleName())
			   .withAdministrativeState(element.getAdministrativeState())
			   .withOperationalState(element.getOperationalState())
			   .withDateModified(element.getDateModified())
			   .withModule(moduleData(module))
			   .build();
	}

	public ElementModule getElementModule(Element element, ModuleName moduleName) {
		Element_Module module = repository.execute(findModule(element, moduleName));
		if(module == null){
			LOG.fine(() -> format("%s: Module named %s does not exist on element %s", 
								  IVT0310E_ELEMENT_MODULE_NOT_FOUND.getReasonCode(),
								  moduleName,
								  element.getElementName()));
			throw new EntityNotFoundException(IVT0310E_ELEMENT_MODULE_NOT_FOUND,
											  element.getElementName(),
											  moduleName);
		}
		return mapModule(element, 
						 module);
	}
}

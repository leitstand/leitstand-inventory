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
package io.leitstand.inventory.service;

import java.util.List;

/**
 * A service for managing element hardware modules.
 */
public interface ElementModuleService {

	/**
	 * Returns the element module information.
	 * @param elementId the element ID
	 * @param serialNumber the module serial number
	 * @return the element module
	 * @throws EntityNotFoundException if the element or the module does not exist.
	 */
	ElementModule getElementModule(ElementId elementId, String serialNumber);
	
    /**
     * Returns the element module information.
     * @param elementName the element name
     * @param serialNumber the module serial number
     * @return the element module
     * @throws EntityNotFoundException if the element or the module does not exist.
     */
	ElementModule getElementModule(ElementName elementName, String serialNumber);

    /**
     * Returns the element module information.
     * @param elementId the element ID
     * @param moduleName the module name
     * @return the element module
     * @throws EntityNotFoundException if the element or the module does not exist.
     */
	ElementModule getElementModule(ElementId elementId, ModuleName moduleName);
	
    /**
     * Returns the element module information.
     * @param elementName the element name
     * @param moduleName the module name
     * @return the element module
     * @throws EntityNotFoundException if the element or the module does not exist.
     */
	ElementModule getElementModule(ElementName elementName, ModuleName moduleName);
	
	/**
	 * Returns all element modules of the specified element.
	 * @param elementId the element ID
	 * @return the element module
	 * @throws EntityNotFoundException if the requested element does not exist.
	 */
	ElementModules getElementModules(ElementId elementId);

	/**
	 * Returns all element modules of the specified element.
	 * @param elementName the element name.
	 * @return the element module.
	 * @throws EntityNotFoundException if the requested element does not exist.
	 */
	ElementModules getElementModules(ElementName elementName);

	/**
	 * Removes the specified module from the specified element.
	 * @param elementId the element ID.
	 * @param moduleName the module name.
	 * @throws EntityNotFoundException if the requested element does not exist.
	 */
	void removeElementModule(ElementId elementId, ModuleName moduleName);

	/**
	 * Removes the specified module from the specified element.
	 * @param elementName the element name.
	 * @param moduleName the module name.
	 * @throws EntityNotFoundException if the requested element does not exist.
	 */
	void removeElementModule(ElementName elementName, ModuleName moduleName);

	/**
	 * Stores an element module into the inventory.
	 * Returns <code>true</code> if a new module is added to the inventory and <code>false</code> if not.
	 * @param elementId the element ID
	 * @param moduleName the module name
	 * @param module the module data
	 * @return <code>true</code> if a new module is added, <code>false</code> otherwise.
	 */
	boolean storeElementModule(ElementId elementId, ModuleName moduleName, ModuleData module);

    /**
     * Stores an element module into the inventory.
     * Returns <code>true</code> if a new module is added to the inventory and <code>false</code> if not.
     * @param elementName the element name
     * @param moduleName the module name
     * @param module the module data
     * @return <code>true</code> if a new module is added, <code>false</code> otherwise.
     */
	boolean storeElementModule(ElementName elementName, ModuleName moduleName, ModuleData module);

	/**
	 * Stores all modules of an element in one go.
	 * @param elementId the element ID
	 * @param modules the element modules
	 */
	void storeElementModules(ElementId elementId, List<ModuleData> modules);

	/**
	 * Stores all modules of an element in one go.
	 * @param elementName the element name
	 * @param modules the element modules
	 */
	void storeElementModules(ElementName elementName, List<ModuleData> modules);

}

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

import javax.persistence.EntityNotFoundException;

/**
 * A service for managing logical interfaces.
 */
public interface ElementLogicalInterfaceService {

	/**
	 * Finds all logical interfaces.
	 * @param elementId the element ID.
	 * @param filter a filter for interface name, IP prefix or VLAN ID.
	 * @param limit the maximum number of returned matching interfaces.
	 * @return the matching logical interfaces.
	 * @throws EntityNotFoundException if the element does not exist.
	 */
	ElementLogicalInterfaces findLogicalInterfaces(ElementId elementId, String filter, int limit);
	
	
    /**
     * Finds all logical interfaces.
     * @param elementName the element name.
     * @param filter a filter for interface name, IP prefix or VLAN ID.
     * @param limit the maximum number of returned matching interfaces.
     * @return the matching logical interfaces.
     * @throws EntityNotFoundException if the element does not exist.
     */
	ElementLogicalInterfaces findLogicalInterfaces(ElementName elementName, String filter, int limit);

	
	/**
	 * Returns the logical interface.
	 * @param elementId the element ID.
	 * @param name the interface name.
	 * @return the logical interface with the specified name at the specified element.
	 * @throws EntityNotFoundException if the element or interface does not exist. 
	 */
	ElementLogicalInterface getLogicalInterface(ElementId elementId, InterfaceName name);
	
    /**
     * Returns the logical interface.
     * @param elementName the element name.
     * @param name the interface name.
     * @return the logical interface with the specified name at the specified element.
     * @throws EntityNotFoundException if the element or interface does not exist. 
     */
	ElementLogicalInterface getLogicalInterface(ElementName elementName, InterfaceName name);
	
	/**
	 * Removes a logical interface.
	 * @param elementId the element ID.
	 * @param name the interface name.
	 * @throws EntityNotFoundException if the specified element does not exist.
	 */
	void removeLogicalInterface(ElementId elementId, InterfaceName name);
	
    /**
     * Removes a logical interface.
     * @param elementName the element name.
     * @param name the interface name.
     * @throws EntityNotFoundException if the specified element does not exist.
     */
	void removeLogicalInterface(ElementName elementName, InterfaceName name);
	
	/**
	 * Stores a logical interface.
	 * Returns <code>true</code> if a new interface is added and <code>false</code> if an existing interface is updated.
	 * @param elementId the element ID.
	 * @param ifc the logical interface.
	 * @return <code>true</code> if a new interface record is added and 
	 * 		   <code>false</code> if an existing interface record is updated.
	 * @throws EntityNotFoundException if the element does not exist
	 */
	boolean storeLogicalInterface(ElementId elementId, ElementLogicalInterfaceSubmission ifc);
	
    /**
     * Stores a logical interface.
     * Returns <code>true</code> if a new interface is added and <code>false</code> if an existing interface is updated.
     * @param elementName the element name.
     * @param ifc the logical interface.
     * @return <code>true</code> if a new interface record is added and 
     *         <code>false</code> if an existing interface record is updated.
     * @throws EntityNotFoundException if the element does not exist
     */
	boolean storeLogicalInterface(ElementName elementName, ElementLogicalInterfaceSubmission ifc);

	
	/**
	 * Removes all logical interfaces from the element.
	 * @param elementId the element ID
	 * @throws EntityNotFoundException if the element does not exist.
	 */
	void removeLogicalInterfaces(ElementId elementId);
	
    /**
     * Removes all logical interfaces from the element.
     * @param elementName the element name
     * @throws EntityNotFoundException if the element does not exist.
     */
	void removeLogicalInterfaces(ElementName elementName);
	
	
}

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

import javax.persistence.EntityNotFoundException;

/**
 * A stateless and transactional service to manage the logical interfaces of an element.
 */
public interface ElementLogicalInterfaceService {

	/**
	 * Returns the logical interface with the specified name at the specified element.
	 * @param elementId the element ID
	 * @param name the interface name
	 * @return the logical interface with the specified name at the specified element
	 * @throws EntityNotFoundException if the requested element or interface does not exist. 
	 */
	ElementLogicalInterface getLogicalInterface(ElementId elementId, InterfaceName name);
	
	/**
	 * Returns the logical interface with the specified name at the specified element.
	 * @param elementName the element name
	 * @param name the interface name
	 * @return the logical interface with the specified name at the specified element
	 * @throws EntityNotFoundException if the requested element or interface does not exist. 
	 */
	ElementLogicalInterface getLogicalInterface(ElementName elementName, InterfaceName name);
	
	/**
	 * Returns all logical interfaces of the specified element.
	 * @param elementId the element ID
	 * @return the logical interface with the specified name at the specified element
	 * @throws EntityNotFoundException if the requested element does not exist. 
	 */
	ElementLogicalInterfaces getLogicalInterfaces(ElementId elementId);

	/**
	 * Returns all logical interfaces of the specified element.
	 * @param elementName- the element name
	 * @return the logical interface with the specified name at the specified element
	 * @throws EntityNotFoundException if the requested element does not exist. 
	 */
	ElementLogicalInterfaces getLogicalInterfaces(ElementName elementName);

	/**
	 * Removes the specified logical interface from the specified element.
	 * @param elementId the element ID
	 * @param name the interface name
	 * @throws EntityNotFoundException if the specified element does not exist
	 */
	void removeLogicalInterface(ElementId elementId, InterfaceName name);
	
	/**
	 * Removes the specified logical interface from the specified element.
	 * @param elementId the element name
	 * @param name the interface name
	 * @throws EntityNotFoundException if the specified element does not exist
	 */
	void removeLogicalInterface(ElementName elementName, InterfaceName name);
	
	/**
	 * Stores a logical interface of the specified element.
	 * @param elementId the element ID
	 * @param ifc the logical interface
	 * @return <code>true</code> if a new interface record was created and 
	 * 		   <code>false</code> if an existing interface record was updated.
	 * @throws EntityNotFoundException if the specified element does not exist
	 */
	boolean storeLogicalInterface(ElementId elementId, ElementLogicalInterfaceSubmission ifc);
	
	/**
	 * Stores a logical interface of the specified element.
	 * @param elementName the element name
	 * @param ifc the logical interface
	 * @return <code>true</code> if a new interface record was created and 
	 * 		   <code>false</code> if an existing interface record was updated.
	 * @throws EntityNotFoundException if the specified element does not exist
	 */
	boolean storeLogicalInterface(ElementName elementName, ElementLogicalInterfaceSubmission ifc);
	
	/**
	 * Updates all logical interfaces of the specified element in one go.
	 * @param elementId the element ID
	 * @param ifcs the list of logical interfaces
	 * @throws EntityNotFoundException if the specified element does not exist
	 */
	void storeLogicalInterfaces(ElementId elementId, List<ElementLogicalInterfaceSubmission> ifcs);
	
	/**
	 * Updates all logical interfaces of the specified element in one go.
	 * @param elementId the element ID
	 * @param ifcs the list of logical interfaces
	 * @throws EntityNotFoundException if the specified element does not exist
	 */
	void storeLogicalInterfaces(ElementName elementName, List<ElementLogicalInterfaceSubmission> ifcs);
	
	
	
	
}

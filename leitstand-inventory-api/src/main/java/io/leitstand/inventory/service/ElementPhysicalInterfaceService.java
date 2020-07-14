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
 * A stateless and transactional service to manage the physical interfaces of an element.
 */
public interface ElementPhysicalInterfaceService {

	/**
	 * Returns the physical interface with the specified name at the specified element.
	 * @param elementId - the element ID
	 * @param name - the interface name
	 * @return the physical interface with the specified name at the specified element
	 * @throws EntityNotFoundException if the requested element or interface does not exist. 
	 */
	ElementPhysicalInterface getPhysicalInterface(ElementId elementId, InterfaceName name);

	/**
	 * Returns the physical interface with the specified name at the specified element.
	 * @param elementName- the element name
	 * @param name - the interface name
	 * @return the physical interface with the specified name at the specified element
	 * @throws EntityNotFoundException if the requested element or interface does not exist. 
	 */
	ElementPhysicalInterface getPhysicalInterface(ElementName elementName, InterfaceName name);
	
	/**
	 * Returns all physical interfaces of the specified element.
	 * @param elementId - the element ID
	 * @return the physical interface with the specified name at the specified element
	 * @throws EntityNotFoundException if the requested element does not exist. 
	 */
	ElementPhysicalInterfaces getPhysicalInterfaces(ElementId elementId);

	/**
	 * Returns all physical interfaces of the specified element.
	 * @param elementNAme- the element name
	 * @return the physical interface with the specified name at the specified element
	 * @throws EntityNotFoundException if the requested element does not exist. 
	 */
	ElementPhysicalInterfaces getPhysicalInterfaces(ElementName elementName);
	
	/**
	 * Removes the specified physical interface from the specified element.
	 * @param elementId - the element ID
	 * @param name - the interface name
	 * @throws EntityNotFoundException if the specified element does not exist
	 */
	void removePhysicalInterface(ElementId elementId, InterfaceName name);
	
	/**
	 * Removes the specified physical interface from the specified element.
	 * @param elementName - the element name
	 * @param name - the interface name
	 * @throws EntityNotFoundException if the specified element does not exist
	 */
	void removePhysicalInterface(ElementName elementName, InterfaceName name);
	
	/**
	 * Removes the neighbor information of a physical interface.
	 * @param elementId - the element ID
	 * @param ifpName - the interface name
	 */
	void removePhysicalInterfaceNeighbor(ElementId elementId, InterfaceName ifpName);

	/**
	 * Removes the neighbor information of a physical interface.
	 * @param elementName - the element name
	 * @param ifpName - the interface name
	 */
	void removePhysicalInterfaceNeighbor(ElementName elementName, InterfaceName ifpName);
	
	/**
	 * Stores a physical interface of the specified element.
	 * @param elementId - the element ID
	 * @param ifc - the physical interface
	 * @return <code>true</code> if a new interface record was created and 
	 * 		   <code>false</code> if an existing interface record was updated.
	 * @throws EntityNotFoundException if the specified element does not exist
	 */
	boolean storePhysicalInterface(ElementId elementId, ElementPhysicalInterfaceSubmission ifc);
	
	/**
	 * Stores a physical interface of the specified element.
	 * @param elementName - the element name
	 * @param ifc - the physical interface
	 * @return <code>true</code> if a new interface record was created and 
	 * 		   <code>false</code> if an existing interface record was updated.
	 * @throws EntityNotFoundException if the specified element does not exist
	 */
	boolean storePhysicalInterface(ElementName elementName, ElementPhysicalInterfaceSubmission ifc);
	
	/**
	 * Stores the neighbor of a physical interface.
	 * @param id - the element ID
	 * @param ifpName - the interface name
	 * @param neighbor - the neighbor of the physical interface
	 */
	void storePhysicalInterfaceNeighbor(ElementId id, InterfaceName ifpName, ElementPhysicalInterfaceNeighbor neighbor);

	/**
	 * Stores the neighbor of a physical interface.
	 * @param name - the element name
	 * @param ifpName - the interface name
	 * @param neighbor - the neighbor of the physical interface
	 */
	void storePhysicalInterfaceNeighbor(ElementName name, InterfaceName ifpName, ElementPhysicalInterfaceNeighbor neighbor);

	/**
	 * Updates all physical interfaces of the specified element in one go.
	 * @param elementId - the element ID
	 * @param ifcs - the list of physical interfaces
	 * @throws EntityNotFoundException if the specified element does not exist
	 */
	void storePhysicalInterfaces(ElementId elementId, List<ElementPhysicalInterfaceSubmission> ifcs);
	
	/**
	 * Updates all physical interfaces of the specified element in one go.
	 * @param elementId - the element ID
	 * @param ifcs - the list of physical interfaces
	 * @throws EntityNotFoundException if the specified element does not exist
	 */
	void storePhysicalInterfaces(ElementName elementName, List<ElementPhysicalInterfaceSubmission> ifcs);
	
	/**
	 * Updates the administrative state of a physical interface.
	 * @param name - the element name
	 * @param ifpName - the interface name
	 * @param admState - the administrative state
	 */
	void updatePhysicalInterfaceAdministrativeState(ElementId id, InterfaceName ifpName, AdministrativeState admState);
	
	/**
	 * Updates the administrative state of a physical interface.
	 * @param name - the element name
	 * @param ifpName - the interface name
	 * @param admState - the administrative state
	 */
	void updatePhysicalInterfaceAdministrativeState(ElementName name, InterfaceName ifpName, AdministrativeState admState);
	
	/**
	 * Updates the operational state of a physical interface.
	 * @param id - the element ID
	 * @param ifpName - the interface name
	 * @param opState - the operational state
	 */
	void updatePhysicalInterfaceOperationalState(ElementId id, InterfaceName ifpName, OperationalState opState);
	
	/**
	 * Updates the operational state of a physical interface.
	 * @param name - the element name
	 * @param ifpName - the interface name
	 * @param opState - the operational state
	 */
	void updatePhysicalInterfaceOperationalState(ElementName name, InterfaceName ifpName, OperationalState opState);

	
}

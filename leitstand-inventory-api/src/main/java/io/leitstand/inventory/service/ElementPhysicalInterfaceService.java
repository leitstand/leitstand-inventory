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
 * A servive for managing physical interfaces of an element.
 */
public interface ElementPhysicalInterfaceService {

	/**
	 * Returns the physical interface.
	 * @param elementId the element ID.
	 * @param name the interface name.
	 * @return the physical interface.
	 * @throws EntityNotFoundException if the element or the physical interface does not exist. 
	 */
	ElementPhysicalInterface getPhysicalInterface(ElementId elementId, InterfaceName name);

    /**
     * Returns the physical interface.
     * @param elementId the element name.
     * @param name the interface name.
     * @return the physical interface.
     * @throws EntityNotFoundException if the element or the physical interface does not exist. 
     */
	ElementPhysicalInterface getPhysicalInterface(ElementName elementName, InterfaceName name);
	
	/**
	 * Returns all matching physical interfaces of the element.
	 * @param elementId the element ID.
	 * @param filter filter for physical interfaces.
	 * @return the physical interface with the specified name at the specified element.
	 * @throws EntityNotFoundException if the element does not exist. 
	 */
	ElementPhysicalInterfaces getPhysicalInterfaces(ElementId elementId, ElementPhysicalInterfaceFilter filter);

	/**
	 * Returns all physical interfaces of the specified element.
	 * @param elementName the element name.
	 * @param filter filter for physical interfaces.
	 * @return the physical interface with the specified name at the specified element.
	 * @throws EntityNotFoundException if the element does not exist. 
	 */
	ElementPhysicalInterfaces getPhysicalInterfaces(ElementName elementName,ElementPhysicalInterfaceFilter filter);
	
	
	/**
	 * Removes a physical interface.
	 * @param elementId the element ID.
	 * @param name the interface name.
	 * @throws EntityNotFoundException if the element does not exist.
	 */
	void removePhysicalInterface(ElementId elementId, InterfaceName name);
	
	/**
	 * Removes the specified physical interface from the specified element.
	 * @param elementName the element name
	 * @param name the interface name
	 * @throws EntityNotFoundException if the specified element does not exist
	 */
	void removePhysicalInterface(ElementName elementName, InterfaceName name);
	
	/**
	 * Removes the neighbor information of a physical interface.
	 * @param elementId the element ID.
	 * @param ifpName the interface name.
	 * @throws EntityNotFoundException if the element or the physical does not exist.
	 */
	void removePhysicalInterfaceNeighbor(ElementId elementId, InterfaceName ifpName);

	/**
	 * Removes the neighbor information of a physical interface.
	 * @param elementName the element name.
	 * @param ifpName the interface name.
	 * @throws EntityNotFoundException if the element or the physical does not exist.
	 */
	void removePhysicalInterfaceNeighbor(ElementName elementName, InterfaceName ifpName);
	
	/**
	 * Stores a physical interface. Returns <code>true</code> if an interface is added, <code>false</code> if an existing interface is updated.
	 * @param elementId the element ID.
	 * @param ifp the physical interface settings.
	 * @return <code>true</code> if a new interface record is added and 
	 * 		   <code>false</code> if an existing interface record is updated.
	 * @throws EntityNotFoundException if the element does not exist.
	 */
	boolean storePhysicalInterface(ElementId elementId, ElementPhysicalInterfaceSubmission ifp);
	
    /**
     * Stores a physical interface. Returns <code>true</code> if an interface is added, <code>false</code> if an existing interface is updated.
     * @param elementName the element name.
     * @param ifp the physical interface settings.
     * @return <code>true</code> if a new interface record is added and 
     *         <code>false</code> if an existing interface record is updated.
     * @throws EntityNotFoundException if the element does not exist.
     */
	boolean storePhysicalInterface(ElementName elementName, ElementPhysicalInterfaceSubmission ifc);
	
	/**
	 * Stores the neighbor of a physical interface.
	 * @param id the element ID
	 * @param ifpName the interface name
	 * @param neighbor the neighbor of the physical interface
	 */
	void storePhysicalInterfaceNeighbor(ElementId id, InterfaceName ifpName, ElementPhysicalInterfaceNeighbor neighbor);

	/**
	 * Stores the neighbor of a physical interface.
	 * @param elementName the element name.
	 * @param ifpName the interface name.
	 * @param neighbor the neighbor of the physical interface.
	 * @throws EntityNotFoundException if the element or the physical interface does not exist.
	 */
	void storePhysicalInterfaceNeighbor(ElementName elementName, InterfaceName ifpName, ElementPhysicalInterfaceNeighbor neighbor);

    /**
     * Stores the physical interfaces of an element.
     * @param elementId the element ID.
     * @param ifpName the interface name.
     * @param neighbor the neighbor of the physical interface.
     * @throws EntityNotFoundException if the element or the physical interface does not exist.
     */
	void storePhysicalInterfaces(ElementId elementId, List<ElementPhysicalInterfaceSubmission> ifcs);
	
    /**
     * Stores the physical interfaces of an element.
     * @param elementName the element name.
     * @param ifpName the interface name.
     * @param neighbor the neighbor of the physical interface.
     * @throws EntityNotFoundException if the element or the physical interface does not exist.
     */
	void storePhysicalInterfaces(ElementName elementName, List<ElementPhysicalInterfaceSubmission> ifcs);
	
	/**
	 * Updates the administrative state of a physical interface.
	 * @param elementId the element ID
	 * @param ifpName the interface name
	 * @param admState the administrative state
	 * @throws EntityNotFoundException if the element or the physical interface does not exist.
	 */
	void updatePhysicalInterfaceAdministrativeState(ElementId elementId, InterfaceName ifpName, AdministrativeState admState);
	
    /**
     * Updates the administrative state of a physical interface.
     * @param elementName the element name
     * @param ifpName the interface name
     * @param admState the administrative state
     * @throws EntityNotFoundException if the element or the physical interface does not exist.
     */
	void updatePhysicalInterfaceAdministrativeState(ElementName elementName, InterfaceName ifpName, AdministrativeState admState);
	
	/**
	 * Updates the operational state of a physical interface.
	 * @param elementId the element ID
	 * @param ifpName the interface name
	 * @param opState the operational state
     * @throws EntityNotFoundException if the element or the physical interface does not exist.
     */	 
	void updatePhysicalInterfaceOperationalState(ElementId elementId, InterfaceName ifpName, OperationalState opState);
	
    /**
     * Updates the operational state of a physical interface.
     * @param elementName the element name
     * @param ifpName the interface name
     * @param opState the operational state
     * @throws EntityNotFoundException if the element or the physical interface does not exist.
     */  
	void updatePhysicalInterfaceOperationalState(ElementName elementName, InterfaceName ifpName, OperationalState opState);

	
}

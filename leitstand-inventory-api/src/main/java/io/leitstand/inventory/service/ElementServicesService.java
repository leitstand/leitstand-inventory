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

import io.leitstand.commons.EntityNotFoundException;

/**
 * A stateless and transactional service to manage element service informations.
 */
public interface ElementServicesService {

	/**
	 * Returns all services installed on the specified element
	 * @param id the element id
	 * @return all services installed on the element.
	 * @throws EntityNotFoundException if the element does not exist
	 */
	ElementServices getElementServices(ElementId id);

	/**
	 * Returns all services installed on the specified element
	 * @param name the element name
	 * @return all services installed on the element.
	 * @throws EntityNotFoundException if the element does not exist.
	 */

	ElementServices getElementServices(ElementName name);

	/**
	 * Returns the service stack for the specified service on a certain element.
	 * The service stack is constituted by all parent services of the specified service.
	 * @param id the element ID 
	 * @param service the service name
	 * @return the service stack
	 * @throws EntityNotFoundException if the element or service instance does not exist.
	 */
	ElementServiceStack getElementServiceStack(ElementId id, ServiceName service);

	/**
	 * Returns the service stack for the specified service on a certain element.
	 * The service stack is constituted by all parent services of the specified service.
	 * @param name the element name 
	 * @param service the service name
	 * @return the service stack
	 * @throws EntityNotFoundException if the element or service instance does not exist.
	 */
	ElementServiceStack getElementServiceStack(ElementName name, ServiceName service);

	ElementServiceContext getElementService(ElementId elementId, ServiceName service);
	ElementServiceContext getElementService(ElementName elementName, ServiceName service);
	
	/**
	 * Stores an element service.
	 * Returns <code>true</code> when a new service was added to the inventory and 
	 * <code>false</code> if an existing service recors has been updated.
	 * @param id the element ID 
	 * @param service the service submission
	 * @return <code>true</code> if a new service record was created or <code>false</code> if an existing service record has been updated.
	 * @throws EntityNotFoundException if the element does not exist.
	 */
	boolean storeElementService(ElementId id, ElementServiceSubmission service);

	/**
	 * Stores an element service.
	 * Returns <code>true</code> when a new service was added to the inventory and 
	 * <code>false</code> if an existing service recors has been updated.
	 * @param name the element name 
	 * @param service the service submission
	 * @return <code>true</code> if a new service record was created or <code>false</code> if an existing service record has been updated.
	 * @throws EntityNotFoundException if the element does not exist.
	 */
	boolean storeElementService(ElementName name, ElementServiceSubmission service);

	/**
	 * Stores all service information of a certain element in one go.
	 * @param id the element ID
	 * @param services the element services
	 * @throws EntityNotFoundException if the element does not exist.
	 */
	void storeElementServices(ElementId id, List<ElementServiceSubmission> services);

	/**
	 * Stores all service information of a certain element in one go.
	 * @param name the element name
	 * @param services the element services
	 * @throws EntityNotFoundException if the element does not exist.
	 */
	void storeElementServices(ElementName name, List<ElementServiceSubmission> services);

	
	/**
	 * Updates the operational state of an element service.
	 * @param id the element ID
	 * @param serviceName the service name
	 * @param state the operational state
	 * @throws EntityNotFoundException if the element or service does not exist.
	 */
	void updateElementServiceOperationalState(	ElementId id,
												ServiceName serviceName,
												OperationalState state);

	/**
	 * Updates the operational state of an element service.
	 * @param name the element name
	 * @param serviceName the service name
	 * @param state the operational state
	 * @throws EntityNotFoundException if the element or service does not exist.
	 */
	void updateElementServiceOperationalState(	ElementName name,
												ServiceName serviceName,
												OperationalState state);

	/**
	 * Removes an element service from the inventory.
	 * @param id the element ID
	 * @param serviceName the service name
	 * @throws EntityNotFoundException if the element does not exist.
	 */
	void removeElementService(ElementId id, ServiceName serviceName);

	/**
	 * Removes an element service from the inventory.
	 * @param name the element name
	 * @param serviceName the service name
	 * @throws EntityNotFoundException if the element or service does not exist.
	 */
	void removeElementService(ElementName name, ServiceName serviceName);

}

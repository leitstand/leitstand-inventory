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
 * A service for managing services running on an element.
 */
public interface ElementServicesService {

	/**
	 * Returns all services installed on an element.
	 * @param elementId the element ID.
	 * @return all services installed on the element.
	 * @throws EntityNotFoundException if the element does not exist.
	 */
	ElementServices getElementServices(ElementId elementId);

    /**
     * Returns all services installed on an element.
     * @param elementName the element name.
     * @return all services installed on the element.
     * @throws EntityNotFoundException if the element does not exist.
     */
	ElementServices getElementServices(ElementName name);

	/**
	 * Returns the service stack.
	 * The service stack contains a service and all its parent services.
	 * @param elementId the element ID.
	 * @param serviceName the service name.
	 * @return the service stack.
	 * @throws EntityNotFoundException if the element or the service does not exist.
	 */
	ElementServiceStack getElementServiceStack(ElementId elementId, ServiceName serviceName);

    /**
     * Returns the service stack.
     * The service stack contains a service and all its parent services.
     * @param elementName the element name.
     * @param serviceName the service name.
     * @return the service stack.
     * @throws EntityNotFoundException if the element or the service does not exist.
     */
	ElementServiceStack getElementServiceStack(ElementName elementName, ServiceName serviceName);

	/**
	 * Returns the service context.
	 * @param elementId the element ID.
	 * @param serviceName the service name.
	 * @return the service context.
	 * @throws EntityNotFoundException if the element or service does not exist.
	 */
	ElementServiceContext getElementService(ElementId elementId, ServiceName serviceName);

	/**
     * Returns the service context.
     * @param elementName the element name.
     * @param serviceName the service name.
     * @return the service context.
     * @throws EntityNotFoundException if the element or service does not exist.
     */
	ElementServiceContext getElementService(ElementName elementName, ServiceName serviceName);
	
	/**
	 * Stores an element service.
	 * Returns <code>true</code> when a new service is added and 
	 * <code>false</code> if an existing service is updated.
	 * @param id the element ID 
	 * @param service the service submission
	 * @return <code>true</code> when a new service is added and 
     * <code>false</code> if an existing service is updated.
	 * @throws EntityNotFoundException if the element does not exist.
	 */
	boolean storeElementService(ElementId id, ElementServiceSubmission service);

    /**
     * Stores an element service.
     * Returns <code>true</code> when a new service is added and 
     * <code>false</code> if an existing service is updated.
     * @param id the element ID 
     * @param service the service submission
     * @return <code>true</code> when a new service is added and 
     * <code>false</code> if an existing service is updated.
     * @throws EntityNotFoundException if the element does not exist.
     */
	boolean storeElementService(ElementName name, ElementServiceSubmission service);

	/**
	 * Stores all services of an element.
	 * @param id the element ID
	 * @param services the element services
	 * @throws EntityNotFoundException if the element does not exist.
	 */
	void storeElementServices(ElementId id, List<ElementServiceSubmission> services);

    /**
     * Stores all services of an element.
     * @param id the element ID
     * @param services the element services
     * @throws EntityNotFoundException if the element does not exist.
     */
	void storeElementServices(ElementName name, List<ElementServiceSubmission> services);

	
	/**
	 * Updates the operational state of an element service.
	 * @param elementId the element ID.
	 * @param serviceName the service name.
	 * @param state the operational state.
	 * @throws EntityNotFoundException if the element or service does not exist.
	 */
	void updateElementServiceOperationalState(	ElementId elementId,
												ServiceName serviceName,
												OperationalState state);

    /**
     * Updates the operational state of an element service.
     * @param elementId the element ID.
     * @param serviceName the service name.
     * @param state the operational state.
     * @throws EntityNotFoundException if the element or service does not exist.
     */
	void updateElementServiceOperationalState(	ElementName name,
												ServiceName serviceName,
												OperationalState state);

	/**
	 * Removes an element service.
	 * @param elementId the element ID
	 * @param serviceName the service name
	 * @throws EntityNotFoundException if the element does not exist.
	 */
	void removeElementService(ElementId elementId, ServiceName serviceName);

	/**
	 * Removes an element service.
	 * @param elementName the element name.
	 * @param serviceName the service name.
	 * @throws EntityNotFoundException if the element or service does not exist.
	 */
	void removeElementService(ElementName elementName, ServiceName serviceName);

}

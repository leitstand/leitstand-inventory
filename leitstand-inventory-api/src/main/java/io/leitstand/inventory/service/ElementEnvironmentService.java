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

/**
 * A service for managing element environments.
 * <p>
 * The <code>ElementEnvironmentService</code> allows querying element environments as well as storing and removing element environments.
 * An element environment contains variables passed to the template engine to render the element configuration.
 * The environment variables are stored in a JSON object.
 * <p>
 * Typically an element environment addresses a transport network capability configuration, like BGP or IS-IS configurations for example.
 */
public interface ElementEnvironmentService {

    /**
     * Returns the environments meta data of the specified element.
     * @param elementId the element ID
     * @return the environments meta data.
     */
    ElementEnvironments getElementEnvironments(ElementId elementId);

    /**
     * Returns the environments meta data of the specified element.
     * @param elementName the element name or the element alias
     * @return the environments meta data.
     */
    ElementEnvironments getElementEnvironments(ElementName elementName);
	
    /**
     * Returns the element environment with the specified ID
     * @param id the environment ID
     * @return the element environment with the specified ID
     */
	ElementEnvironment getElementEnvironment(EnvironmentId id);
	
	/**
	 * Returns the environment with the specified name.
	 * @param elementId the element ID
	 * @param name the environment name
	 * @return the element environment with the specified name
	 */
	ElementEnvironment getElementEnvironment(ElementId elementId, EnvironmentName name);
	
	/**
	 * Returns the environment with the specified name.
	 * @param elementName the element name
	 * @param name the environment name
	 * @return the element environment with the specified name
	 */
	ElementEnvironment getElementEnvironment(ElementName elementName, EnvironmentName name);
	
	/**
	 * Stores an element environment.
	 * @param elementId the element ID
	 * @param env the element environment
	 * @return <code>true</code> if a new environment is added to the inventory and 
	 *         <code>false</code> if an existing environment got updated
	 */
	boolean storeElementEnvironment(ElementId elementId, Environment env);

	/**
     * Stores an element environment.
     * @param elementName the element name or the element alias
     * @param env the element environment
     * @return <code>true</code> if a new environment is added to the inventory and 
     *         <code>false</code> if an existing environment got updated
     */
	boolean storeElementEnvironment(ElementName elementName, Environment env);
	
	/**
	 * Removes an element environment.
	 * @param id the element environment ID
	 */
	void removeElementEnvironment(EnvironmentId id);
	
	/**
	 * Removes an element environment.
	 * @param elementId the element ID
	 * @param name the environment ID
	 */
	void removeElementEnvironment(ElementId elementId, EnvironmentName name);
	
	/**
	 * Removes an element environment.
	 * @param elementName the element name
	 * @param name the environment name
	 */
	void removeElementEnvironment(ElementName elementName, EnvironmentName name);
	
}

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
 * The <code>CloneElementService</code> creates a clone of an element in the inventory.
 * <p>
 * Cloning an element creates a new element and applies the settings of the broken element to the new element.
 */
public interface CloneElementService {

	/**
	 * Clones an element by creating a new element in the inventory and 
	 * copying the configuration and environments from the source element to its clone.
	 * @param source the source element ID
	 * @param cloneRequest the mandatory data to create a clone
	 * @return the element ID of the clone element
	 */
	ElementId cloneElement(ElementId source, ElementCloneRequest cloneRequest );
	
	/**
     * Clones an element by creating a new element in the inventory and 
     * copying the configuration and environments from the source element to its clone.
     * @param source the source element name
     * @param cloneRequest the mandatory data to create a clone
     * @return the element ID of the clone element
     */
	ElementId cloneElement(ElementName source, ElementCloneRequest request);
	
	
	
}

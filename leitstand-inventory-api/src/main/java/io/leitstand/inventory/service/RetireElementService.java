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
 * A service to retire an element.
 * <p>
 * Retiring an element applies the following changes to the inventory:
 * <ul>
 *  <li>The administrative state of all interfaces changes is set to DOWN.</li>
 *  <li>The operational state of all interfaces changes is set to DOWN.</li>
 *  <li>The administrative state of all services is set to DOWN.</li>
 *  <li>The operational state of all services is set to DOWN.</li>
 *  <li>The administrative state of the element is set to RETIRED.</li>
 *  <li>The operational state of the element changes to DOWN.</li>
 * </ul>
 */
public interface RetireElementService {

    /**
     * Marks an element as retired.
     * @param elementId the element ID
     * @throws EntityNotFoundException if the element does not exist.
     */
	void retireElement(ElementId elementId);
	
	/**
	 * Marks an element as retired.
	 * @param elementName the element name.
	 * @throws EntityNotFoundException if the element does not exist.
	 */
	void retireElement(ElementName elementName);
	
}

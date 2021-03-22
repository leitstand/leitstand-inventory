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
 * A service for querying elements.
 */
public interface ElementService {

	/**
	 * Searches for elements that match the specified filter expression and returns the element settings of all matches.
	 * The filter expression is a POSIX regular expression and applied to the element name and element alias.
	 * <p>
	 * This method supports pagination by specifying a read <code>offset</code> and the number of items to be returned. 
	 * The list is sorted by the element name in ascending order.
	 * </p>
	 * @param filter a regular expression to filter elements by the element name or element alias.
	 * @param offset the search offset.
	 * @param items the number of items to be returned at maximum.
	 * @return the list of matching elements.
	 */
	List<ElementSettings> findElementsByName(String filter, int offset, int items);
	
	/**
	 * Searches for elements that match the specified filter expression and returns the element settings of all matches.
	 * The filter expression is a POSIX regular expression and applied to the element name, element alias and all element tags.
	 * <p>
	 * This method supports pagination by specifying an <code>offset</code> and the number of items to be returned. 
	 * The list is sorted by the element name in ascending order.
	 * </p>
	 * @param filter a regular expression to filter elements by the element name, element alias or element tags.
	 * @param offset the search offset
	 * @param items the number of items to be returned at maximum
	 * @return the list of matching elements.
	 */
	List<ElementSettings> findElementsByNameOrTag(String filter, int offset, int items);

	/**
	 * Searches for elements that match the specified filter expression and returns the element settings of all matches.
	 * The filter expression is a POSIX regular expression and applied to the element name, element alias and all element tags.
	 * <p>
	 * This method supports pagination by specifying an <code>offset</code> and the number of items to be returned. 
	 * The list is sorted by the element name in ascending order.
	 * </p>
	 * @param filter a regular expression to filter elements by the assigned asset ID
	 * @param offset the search offset
	 * @param items the number of items to be returned at maximum
     * @return the list of matching elements.
	 */
	List<ElementSettings> findElementsByAssetId(String filter, int offset, int items);
	
	/**
	 * Searches for elements that match the specified filter expression and returns the element settings of all matches.
	 * The filter expression is a POSIX regular expression and applied to the serial number of an element.
	 * <p>
	 * This method supports pagination by specifying an <code>offset</code> and the number of items to be returned. 
	 * The list is sorted by the element name in ascending order.
	 * </p>
	 * @param filter a regular expression to filter elements by the assigned serial number
	 * @param offset the search offset
	 * @param items the number of items to be returned at maximum
     * @return the list of matching elements.
	 */
	List<ElementSettings> findElementsBySerialNumber(String filter, int offset, int items);
	
	/**
	 * Searches for elements that match the specified filter expression and returns the element settings of all matches.
	 * The filter expression is a POSIX regular expression and applied to the management IP address or the management interface DNS name of the element.
	 * <p>
	 * This method supports pagination by specifying an <code>offset</code> and the number of items to be returned. 
	 * The list is sorted by the element name in ascending order.
	 * </p>
	 * @param filter a regular expression to filter elements by management IP address / hostname
	 * @param offset the search offset
	 * @param items the number of items to be returned at maximum
     * @return the list of matching elements.
	 */
	List<ElementSettings> findElementsByManagementIP(String filter, int offset, int items);

	/**
	 * Removes the specified element.
	 * Only inactive elements are removable.
	 * This operation reports an error if sub-resources like services, modules, interfaces or configurations are assigned to this element.
	 * Use {@link #forceRemoveElement(ElementId)} to remove an element and assigned element resources.
	 * @param elementId the element ID.
	 * @see #forceRemoveElement(ElementId)
	 */
	void removeElement(ElementId elementId);

	/**
	 * Removes the specified element.
	 * Only inactive elements are removable.
	 * This operation reports an error if sub-resources like services, modules, interfaces or configurations are assigned to this element.
	 * Use {@link #forceRemoveElement(ElementName)} to remove an element and assigned element resources.
	 * @param elementName the element name
	 * @see #forceRemoveElement(ElementName)
	 */
	void removeElement(ElementName elementName);
	
	/**
	 * Sets the operational state of an element.
	 * @param elementId the element ID.
	 * @param state the operational state.
	 */
	void updateElementOperationalState(ElementId elementId, OperationalState state);

	/**
     * Sets the operational state of an element.
     * @param elementName the element name.
     * @param state the operational state.
     */
	void updateElementOperationalState(ElementName elementName, OperationalState state);

	/**
	 * Removes the specified element and its assigned resources.
	 * Only inactive elements are removable.
	 * @param elementId the element ID
	 */
	void forceRemoveElement(ElementId elementId);

	/**
	 * Removes the specified element and its assigned resources.
	 * Only inactive elements are removable.
	 * @param elementId the element name
	 */
	void forceRemoveElement(ElementName elementName);
	
}

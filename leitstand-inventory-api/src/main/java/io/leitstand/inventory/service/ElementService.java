/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import java.util.List;

/**
 * A stateless and transactional service to query for elements.
 */
public interface ElementService {

	/**
	 * Searches for elements that match the specified filter expression and returns the element settings of all matches.
	 * The filter expression is a POSIX regular expression and applied to the element name and all element tags.
	 * <p>
	 * This method supports pagination by specifying an <code>offset</code> and the number of items to be returned. 
	 * The list is sorted by the element name in ascending order.
	 * </p>
	 * @param filter a regular expression to filter elements by the element name or element alias
	 * @param offset the search offset
	 * @param items the number of items to be returned at maximum
	 * @return an immutable list that contains the element settings of all matching elements or 
	 * 		   an empty list if no matches were found.
	 */
	List<ElementSettings> findElements(String filter, int offset, int items);
	
	void removeElement(ElementId elementId);

	void removeElement(ElementName elementName);
	
	void updateElementOperationalState(ElementId id, OperationalState state);
	
	void updateElementOperationalState(ElementName name, OperationalState state);


	void forceRemoveElement(ElementId elementId);

	void forceRemoveElement(ElementName elementName);
	
}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

/**
 * A stateless and transactional service to fetch link information.
 */
public interface ElementLinkService {

	/** 
	 * Returns all links of the specified element.
	 * @param id - the element ID
	 * @return all links of the specified element
	 * @throws EntityNotFoundException if the specified element does not exist.
	 */
	ElementLinks getElementLinks(ElementId id);

	/** 
	 * Returns all links of the specified element.
	 * @param name - the element name
	 * @return all links of the specified element
	 * @throws EntityNotFoundException if the specified element does not exist.
	 */
	ElementLinks getElementLinks(ElementName name);

}
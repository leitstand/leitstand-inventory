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
 * A service to query element link information.
 */
public interface ElementLinkService {

	/** 
	 * Returns all links of the specified element.
	 * @param elementId the element ID
	 * @return all links of the specified element
	 * @throws EntityNotFoundException if the element does not exist.
	 */
	ElementLinks getElementLinks(ElementId elementId);

	/** 
	 * Returns all links of the specified element.
	 * @param elementName the element name
	 * @return all links of the specified element
	 * @throws EntityNotFoundException if the specified element does not exist.
	 */
	ElementLinks getElementLinks(ElementName elementName);

}
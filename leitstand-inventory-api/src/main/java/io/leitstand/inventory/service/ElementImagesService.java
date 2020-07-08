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
 * A stateless and transaction service to manage the images on an element.
 */
public interface ElementImagesService {

	/**
	 * Returns the available information for the specified image on the specified element.
	 * Returns <code>null</code> if the image is not present on the element.
	 * @param elementId - the element ID
	 * @param imageId - the image ID
	 * @return the image information for the specified image on the specified element. 
	 * @throws EntityNotFoundException if the specified element or the specified image does not exist.
	 */
	ElementInstalledImage getElementInstalledImage(ElementId elementId, ImageId imageId);

	/**
	 * Returns the available information for the specified image on the specified element.
	 * Returns <code>null</code> if the image is not present on the element.
	 * @param name - the element name
	 * @param imageId - the image ID
	 * @return the image information for the specified image on the specified element. 
	 * @throws EntityNotFoundException if the specified element or the specified image does not exist.
	 */
	ElementInstalledImage getElementInstalledImage(ElementName name, ImageId imageId);
	
	/**
	 * Returns informations of all images available on the element.
	 * Returns an empty list if no image information is available.
	 * @param id - the element ID
	 * @return the image information for the specified image on the specified element. 
	 * @throws EntityNotFoundException if the specified element does not exist.
	 */
	ElementInstalledImages getElementInstalledImages(ElementId id);

	/**
	 * Returns informations of all images available on the element.
	 * Returns an empty list if no image information is available.
	 * @param name - the element name
	 * @return the image information for the specified image on the specified element. 
	 * @throws EntityNotFoundException if the specified element does not exist.
	 */
	ElementInstalledImages getElementInstalledImages(ElementName name);

	/**
	 * Updates the information of installed images on the element. 
	 * This includes both, the currently active images as well as cached images 
	 * that could be activated instantly.
	 * @param id - the element id
	 * @param images - all images installed on the element.
	 */
	void storeInstalledImages(ElementId id, List<ElementInstalledImageReference> images);

	/**
	 * Updates the information of installed images on the element. 
	 * This includes both, the currently active images as well as cached images 
	 * that could be activated instantly.
	 * @param id - the element name
	 * @param images - all images installed on the element.
	 */
	void storeInstalledImages(ElementName name, List<ElementInstalledImageReference> images);

	void removeInstalledImage(ElementId elementId, ImageId imageId);
	void removeInstalledImage(ElementName elementName, ImageId imageId);
	
	/**
	 * Returns the image that shall be installed via ZTP.
	 * Returns the release image if not ZTP image is specified for the element.
	 * @param elementId the element ID
	 * @return the ZTP image
	 */
	ImageReference getZtpImage(ElementId elementId);
	
	/**
     * Returns the image that shall be installed via ZTP.
     * Returns the release image if not ZTP image is specified for the element.
     * @param elementName the element name
     * @return the ZTP image
     */
	ImageReference getZtpImage(ElementName elementName);
	
	/**
	 * Sets the image that shall be installed via ZTP on this element.
	 * @param elementId the element ID
	 * @param imageId the ZTP image ID.
	 */
	void setZtpImage(ElementId elementId, ImageId imageId);

	   /**
     * Sets the image that shall be installed via ZTP on this element.
     * @param elementName the element name
     * @param imageId the ZTP image ID.
     */
    void setZtpImage(ElementName elementName, ImageId imageId);

	
	/**
	 * Removes ZTP information to fallback to the default ZTP configuration (i.e. deploy release image).
	 * @param elementId the element ID
	 */
	void resetZtpImage(ElementId elementId);
	
	   /**
     * Removes ZTP information to fallback to the default ZTP configuration (i.e. deploy release image).
     * @param elementName the element name
     */
    void resetZtpImage(ElementName elementName);
	
}

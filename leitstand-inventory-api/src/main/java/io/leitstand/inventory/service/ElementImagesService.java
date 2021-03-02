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
 * A transactional service to manage the element image lifecycle.
 * <p>
 * An image can have three different element lifecycle states:
 * <ul>
 *  <li>The <i>active</i> image is currently installed on an element.</li>
 *  <li>A <i>pull</i> image shall be pulled by the element.</li>
 *  <li>A <i>cached</i> image has been pulled by the element and can be activated immediately.</li>
 * </ul>
 * The ZTP flag indicates which image shall be installed by the ZTP process. 
 * This can either be the active image when the ZTP process has been executed and no upgrade is available or an image 
 * to be pulled by the element if a software upgrade is available.
 * @see ElementImageState
 */
public interface ElementImagesService {

	/**
	 * Returns the image state for the specified image on the specified element.
	 * @param elementId the element ID
	 * @param imageId the image ID
	 * @return the element image information 
	 * @throws EntityNotFoundException if the specified element or the specified image does not exist.
	 */
	ElementImage getElementImage(ElementId elementId, ImageId imageId);

    /**
     * Returns the image state for the specified image on the specified element.
     * @param elementName the element name
     * @param imageId the image ID
     * @return the element image information 
     * @throws EntityNotFoundException if the specified element or the specified image does not exist.
     */
	ElementImage getElementImage(ElementName elementName, ImageId imageId);
	
	/**
	 * Returns informations of all images installed on the element or eligible for deployment.
	 * Returns an empty list if no image information is available.
	 * @param elementId the element ID
	 * @return the images eligible for deployment including the currently installed image. 
	 * @throws EntityNotFoundException if the specified element does not exist.
	 */
	ElementImages getElementImages(ElementId elementId);

    /**
     * Returns informations of all images installed on the element or eligible for deployment.
     * Returns an empty list if no image information is available.
     * @param elementName the element name
     * @return the images eligible for deployment including the currently installed image. 
     * @throws EntityNotFoundException if the specified element does not exist.
     */
	ElementImages getElementImages(ElementName elementName);

	/**
	 * Updates the element image status.
	 * This includes images eligible for deployment as well as the installed image.
	 * @param elementId the element ID
	 * @param images the element images
     * @throws EntityNotFoundException if the element does not exist.
	 */
	void storeElementImages(ElementId id, List<ElementImageReference> images);

    /**
     * Updates the element image status.
     * This includes images eligible for deployment as well as the installed image.
     * @param elementName the element name
     * @param images the element images
     * @throws EntityNotFoundException if the element does not exist.
     */
	void storeElementImages(ElementName name, List<ElementImageReference> images);

	/**
	 * Removes the element image settings for the specified element.
	 * @param elementId the element ID
	 * @param imageId the image ID
	 */
	void removeElementImage(ElementId elementId, ImageId imageId);
	
    /**
     * Removes the element image settings for the specified element.
     * @param elementId the element ID
     * @param imageId the image ID
     */	
	void removeElementImage(ElementName elementName, ImageId imageId);
	
	/**
	 * Returns the image that shall be installed via ZTP.
	 * Returns the release image if not ZTP image is specified for the element.
	 * @param elementId the element ID
	 * @return the ZTP image
	 * @throws EntityNotFoundException if the element does not exist.
	 */
	ImageReference getZtpImage(ElementId elementId);
	
	/**
     * Returns the image that shall be installed via ZTP.
     * Returns the release image if not ZTP image is specified for the element.
     * @param elementName the element name
     * @return the ZTP image
     * @throws EntityNotFoundException if the element does not exist.
     */
	ImageReference getZtpImage(ElementName elementName);
	
	/**
     * Sets the image to be installed over zero-touch provisioning (ZTP).
	 * @param elementId the element ID
	 * @param imageId the ZTP image ID.
	 */
	void setZtpImage(ElementId elementId, ImageId imageId);

	 /**
     * Sets the image to be installed over zero-touch provisioning (ZTP).
     * @param elementName the element name
     * @param imageId the image ID.
     */
    void setZtpImage(ElementName elementName, ImageId imageId);

	/**
	 * Removes the zero-touch provisioning (ZTP) settings for the specified element.
	 * The release image is used by the ZTP process.
	 * @param elementId the element ID
	 * @throws EntityNotFoundException if the element does not exist.
	 */
	void resetZtpImage(ElementId elementId);
	
    /**
     * Removes the zero-touch provisioning (ZTP) settings for the specified element.
     * The release image is used by the ZTP process.
     * @param elementName the element name
     * @throws EntityNotFoundException if the element does not exist.
     */
	void resetZtpImage(ElementName elementName);
	
}

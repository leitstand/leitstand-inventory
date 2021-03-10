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

import javax.validation.Valid;

import io.leitstand.commons.ConflictException;

/**
 * A service for managing software images.
 */
public interface ImageService {
    
    /**
     * Searches the inventory for matching images.
     * @param query the image query to filter for images.
     * @return a list of matching images.
     */
	List<ImageReference> findImages(ImageQuery query);
	
	/**
	 * Returns all images for the specified element role.
	 * @param role the element role
	 * @return all eligible images for this element role.
	 */
	RoleImages findRoleImages(ElementRoleName role);
	
	/**
	 * Returns the image metadata.
	 * @param imageId the image ID
	 * @return the image metadata.
	 * @throws EntityNotFoundException if the image does not exist.
	 */
	ImageInfo getImage(ImageId imageId);
	
	/**
	 * Returns the image metadata.
	 * @param imageName the image name
	 * @return the image metadata
	 * @throws EntityNotFoundException if the image does not exist.
	 */
	ImageInfo getImage(ImageName imageName);
	
	/**
	 * Returns the image utilization statistics.
	 * @param imageId the image ID
	 * @return the image utilization statistics.
	 * @throws EntityNotFoundException if the image does not exist.
	 */
	ImageStatistics getImageStatistics(ImageId imageId);
	
	/**
	 * Returns the image utilization details for an element group.
	 * @param imageId the image ID.
	 * @param groupId the group ID.
	 * @return the image utilization details.
	 * @throws EntityNotFoundException if the element group or image does not exist.
	 */
	ImageStatisticsElementGroupElementImages getElementGroupImageStatistics(ImageId imageId,
	                                                                        ElementGroupId groupId);
	
	/**
     * Returns the image utilization details for an element group.
     * @param imageId the image ID.
     * @param groupType the group type.
     * @param groupName the group name.
     * @return the image utilization details.
     * @throws EntityNotFoundException if the element group or image does not exist.
     */
	ImageStatisticsElementGroupElementImages getElementGroupImageStatistics(ImageId imageId,
                                                                            ElementGroupType groupType, 
                                                                            ElementGroupName groupName);
	
	/**
	 * Returns a list of existing image types.
	 * @return the list of existing image types.
	 */
	List<ImageType> getImageTypes();
	
	/**
	 * Returns the list of image version for the specified image type. 
	 * @param imageType the image type.
	 * @return the list of image versions for the specified image type.
	 */
	List<Version> getImageVersions(ImageType imageType);
	
	/**
	 * Returns the release image for the specified element role and chipset.
	 * @param elementRole the element role.
	 * @param platformChipset the platform chipset.
	 * @param imageType the image type.
	 * @return the release image for the specified element role and chipset.
	 * @throws EntityNotFoundException if the release image does not exist.
	 */
	ImageReference getReleaseImage(ElementRoleName elementRole,
	                               PlatformChipsetName platformChipset,
	                               ImageType imageType);
	
	/**
	 * Removes an image from the inventory.
	 * @param imageId the image ID.
	 * @throws ConflictException if the image cannot be removed.
	 */
	void removeImage(ImageId imageId);
	
    /**
     * Removes an image from the inventory.
     * @param imageName the image name.
     * @throws ConflictException if the image cannot be removed.
     */
    void removeImage(@Valid ImageName imageName);
	
	/**
	 * Stores an image. 
	 * Returns <code>true</code> if a new image is added and <code>false</code> if an existing image is updated.
	 * @param image the image metadata.
	 * @return <code>true</code> if a new image is added and <code>false</code> if an existing image is updated.
	 */
	boolean storeImage(ImageInfo image);
	
	/**
	 * Updates the image lifecycle state. 
	 * This method updates the lifecycle state of the specified image and the 
	 * lifecycle state of all dependend images as defined by the image lifecycle.
	 * @param imageId the image ID
	 * @param state the lifecycle state.
	 */
	void updateImageState(ImageId imageId, 
						  ImageState state);



}

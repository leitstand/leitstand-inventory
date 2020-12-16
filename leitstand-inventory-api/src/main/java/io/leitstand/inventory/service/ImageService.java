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

public interface ImageService {
	List<ImageReference> findImages(ImageQuery query);
	RoleImages findRoleImages(ElementRoleName role);
	ImageInfo getImage(ImageId id);
	
	ImageStatistics getImageStatistics(ImageId id);
	ImageStatisticsElementGroupElementImages getElementGroupImageStatistics(ImageId imageId,
	                                                                        ElementGroupId groupId);
    ImageStatisticsElementGroupElementImages getElementGroupImageStatistics(ImageId imageId,
                                                                            ElementGroupType groupType, 
                                                                            ElementGroupName groupName);
	
	List<ImageType> getImageTypes();
	
	List<Version> getImageVersions(ImageType imageType);
	
	ImageReference getReleaseImage(ElementRoleName roleName,
	                               PlatformChipsetName chipset,
	                               ImageType imageType);
	ImageInfo removeImage(ImageId id);
	
	boolean storeImage(ImageInfo image);
	void updateImageState(ImageId id, 
						  ImageState state);

}

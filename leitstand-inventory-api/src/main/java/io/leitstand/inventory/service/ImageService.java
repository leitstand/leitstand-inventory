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
	boolean storeImage(ImageInfo image);
	ImageInfo getImage(ImageId id);
	ImageInfo removeImage(ImageId id);
	ImageInfo getImage(ImageType imageType, 
					   ImageName imageName, 
					   Version version, 
					   ElementId elementId);
	ImageInfo getImage(ImageType imageType, 
					   ImageName imageName, 
					   Version version, 
					   ElementName elementName);
	List<ImageReference> findImages(String filter,
									ElementRoleName role,
									ImageType type, 
									ImageState state, 
									Version version, 
									int limit);
	
	RoleImages findRoleImages(ElementRoleName role);
	
	void updateImageState(ImageId id, 
						  ImageState state);
	ImageStatistics getImageStatistics(ImageId id);
	
	List<ImageType> getImageTypes();
	List<Version> getImageVersions(ImageType imageType);

}

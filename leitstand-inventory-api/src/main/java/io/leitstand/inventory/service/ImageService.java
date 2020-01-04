/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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
	
	ImageMetaData getImageMetaData();

}

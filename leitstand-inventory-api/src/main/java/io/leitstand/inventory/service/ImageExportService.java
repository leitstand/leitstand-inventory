/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

/**
 * A service to export image settings.
 */
public interface ImageExportService {

	
	/**
	 * Exports all images matching the specified criteria.
	 * @param filter - an optional filter expression for element name, element type, vendor name and model name 
	 * @param role - an optional filter for images of a certain element role.
	 * @param version - an optional image version
	 * @param type - an optional image type
	 * @param state - an optional image state
	 * @return an export of all matching images or an empty report if no matching image exists
	 */
	ImagesExport exportImages(String filter, 
							  ElementRoleName role,
							  ImageType type, 
							  ImageState state,
							  Version version);
	
	/**
	 * Imports an image export.
	 * @param export - the image export to be imported
	 */
	void importImages(ImagesExport export);
	
}

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
	ImageExport exportImages(String filter, 
							  ElementRoleName role,
							  ImageType type, 
							  ImageState state,
							  Version version);
	
	/**
	 * Imports an image export.
	 * @param export - the image export to be imported
	 */
	void importImages(ImageExport export);
	
}

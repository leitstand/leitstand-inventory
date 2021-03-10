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
package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.ImageQuery.newQuery;
import static io.leitstand.inventory.service.ImageExport.newImageExport;
import static java.lang.Integer.MAX_VALUE;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ImageExportService;
import io.leitstand.inventory.service.ImageInfo;
import io.leitstand.inventory.service.ImageReference;
import io.leitstand.inventory.service.ImageService;
import io.leitstand.inventory.service.ImageState;
import io.leitstand.inventory.service.ImageType;
import io.leitstand.inventory.service.ImageExport;
import io.leitstand.inventory.service.Version;

@ApplicationScoped
public class DefaultImageExportService implements ImageExportService {

	@Inject
	private ImageService imageService;
	
	@Override
	public ImageExport exportImages(String filter, 
									 ElementRoleName elementRole,
									 ImageType type, 
									 ImageState state,
									 Version version) {
		List<ImageInfo> images = new LinkedList<>();
		for(ImageReference ref : imageService.findImages(newQuery()
		                                                 .filter(filter)
		                                                 .imageState(state)
		                                                 .imageType(type)
		                                                 .imageVersion(version)
		                                                 .roleName(elementRole)
		                                                 .limit(MAX_VALUE))) {
			images.add(imageService.getImage(ref.getImageId()));
		}
		return newImageExport()
			   .withDateCreated(new Date())
			   .withImages(images)
			   .build();
	}

	@Override
	public void importImages(ImageExport export) {
		for(ImageInfo image : export.getImages()) {
			imageService.storeImage(image);
		}
	}
	
}

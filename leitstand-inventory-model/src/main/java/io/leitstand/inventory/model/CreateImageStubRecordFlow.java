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

import static io.leitstand.inventory.model.Image.findByElementAndImageTypeAndVersion;
import static io.leitstand.inventory.model.Image.findImageById;
import static io.leitstand.inventory.service.ImageId.randomImageId;
import static io.leitstand.inventory.service.ImageName.imageName;

import io.leitstand.commons.model.Repository;
import io.leitstand.commons.tx.Flow;
import io.leitstand.inventory.service.ElementInstalledImageReference;
import io.leitstand.inventory.service.ImageName;

/**
 * Attempts to create a stub record for a reported but unknown {@link Image}.
 */
class CreateImageStubRecordFlow implements Flow<Image>{

	private Element element;
	private ElementInstalledImageReference installed;
	
	
	/**
	 * Create a <code>CreateImageStubRecordFlow</code>.
	 * @param element - the element that has reported the unknown image
	 * @param data - the data of the unknown image.
	 */
	CreateImageStubRecordFlow(Element element, ElementInstalledImageReference image) {
		this.element = element;
		this.installed = image;
	}
	
	/**
	 * Checks whether an image exist and attempts to create an image stub record if the image does not exist.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public void transaction(Repository repository) {
		// Merge the existing element entity to this transaction.
		Element attachedElement = repository.merge(element);
		Image image = repository.execute(findImageById(installed.getImageId()));
		
		ImageName imageName = installed.getImageName();
		if(imageName == null) {
			imageName = imageName(element.getElementRoleName()+"_"+element.getPlatformName()+"_"+installed.getImageType()+"_"+installed.getImageVersion());
		}
		
		if(image == null) {
			image = new Image(installed.getImageId(), 
							  installed.getImageType(),
							  imageName,
							  attachedElement.getElementRole(),
							  attachedElement.getPlatform(),
							  installed.getImageVersion());
			repository.add(image);
		}
		
	}


	/**
	 * Fetches the requested image. 
	 * Returns <code>null</code> if the image does not exist.
	 * The image is attached to the resumed transaction.
	 * This method is called after execution of {@link #transaction(Repository)} to return an attached image to the resumed transaction.
	 * @return the attached image entity.
	 */
	@Override
	public Image resume(Repository repository) {
		return repository.execute(findByElementAndImageTypeAndVersion(element, 
																	   installed.getImageType(), 
																	   installed.getImageName(),
																	   installed.getImageVersion()));
		
		
	}

	
}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.model.Image.findByElementAndImageTypeAndVersion;
import static io.leitstand.inventory.service.ImageId.randomImageId;

import io.leitstand.commons.model.Repository;
import io.leitstand.commons.tx.Flow;
import io.leitstand.inventory.service.ElementInstalledImageReference;

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
	CreateImageStubRecordFlow(Element element, ElementInstalledImageReference data) {
		this.element = element;
		this.installed = data;
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
		Image image = repository.execute(findByElementAndImageTypeAndVersion(attachedElement, 
																			  installed.getImageType(), 
																			  installed.getImageName(),
																			  installed.getImageVersion()));
		
		if(image == null) {
			image = new Image(randomImageId(), 
							  "net.rtbrick",
							  installed.getImageType(),
							  installed.getImageName(),
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

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import java.util.List;

import javax.inject.Inject;

import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementImagesService;
import io.leitstand.inventory.service.ElementInstalledImage;
import io.leitstand.inventory.service.ElementInstalledImageReference;
import io.leitstand.inventory.service.ElementInstalledImages;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ImageId;

@Service
public class DefaultElementImagesService implements ElementImagesService {

	@Inject
	private ElementProvider elements;
	
	@Inject
	private ElementImageManager manager;
	
	public DefaultElementImagesService() {
		
	}
	
	DefaultElementImagesService(ElementImageManager manager, ElementProvider elements){
		this.manager = manager;
		this.elements = elements;
	}
	
	@Override
	public ElementInstalledImage getElementInstalledImage(ElementId id, ImageId imageId) {
		Element element = elements.fetchElement(id);
		return manager.getElementInstalledImage(element, imageId);
	}

	@Override
	public ElementInstalledImage getElementInstalledImage(ElementName name, ImageId imageId) {
		Element element = elements.fetchElement(name);
		return manager.getElementInstalledImage(element, imageId);

	}

	@Override
	public ElementInstalledImages getElementInstalledImages(ElementId id) {
		Element element = elements.fetchElement(id);
		return manager.getElementInstalledImages(element);
	}

	@Override
	public ElementInstalledImages getElementInstalledImages(ElementName name) {
		Element element = elements.fetchElement(name);
		return manager.getElementInstalledImages(element);
	}

	@Override
	public void storeInstalledImages(ElementId id, List<ElementInstalledImageReference> images) {
		Element element = elements.fetchElement(id);
		manager.storeInstalledImages(element,images);
	}

	@Override
	public void storeInstalledImages(ElementName name, List<ElementInstalledImageReference> images) {
		Element element = elements.fetchElement(name);
		manager.storeInstalledImages(element,images);
	}

	@Override
	public void addCachedImages(ElementId id, List<ElementInstalledImageReference> images) {
		Element element = elements.fetchElement(id);
		manager.storeCachedImages(element,images);
	}

	@Override
	public void addCachedImages(ElementName name, List<ElementInstalledImageReference> images) {
		Element element = elements.fetchElement(name);
		manager.storeCachedImages(element, images);
	}

	@Override
	public void removeCachedImages(ElementId id, List<ElementInstalledImageReference> images) {
		Element element = elements.fetchElement(id);
		manager.removeCachedImages(element,images);
	}

	@Override
	public void removeCachedImages(ElementName name, List<ElementInstalledImageReference> images) {
		Element element = elements.fetchElement(name);
		manager.removeCachedImages(element,images);
	}

}

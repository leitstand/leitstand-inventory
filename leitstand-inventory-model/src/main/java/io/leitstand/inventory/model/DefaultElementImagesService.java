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
	public void removeInstalledImage(ElementId id,ImageId imageId) {
		Element element = elements.fetchElement(id);
		manager.removeInstalledImage(element,imageId);
	}

	@Override
	public void removeInstalledImage(ElementName name, ImageId imageId) {
		Element element = elements.fetchElement(name);
		manager.removeInstalledImage(element,imageId);
	}

}

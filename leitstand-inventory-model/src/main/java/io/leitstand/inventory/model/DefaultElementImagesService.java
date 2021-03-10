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
import io.leitstand.inventory.service.ElementImage;
import io.leitstand.inventory.service.ElementImageReference;
import io.leitstand.inventory.service.ElementImages;
import io.leitstand.inventory.service.ElementImageService;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ImageId;
import io.leitstand.inventory.service.ImageReference;

@Service
public class DefaultElementImagesService implements ElementImageService {

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
	public ElementImage getElementImage(ElementId id, ImageId imageId) {
		Element element = elements.fetchElement(id);
		return manager.getElementImage(element, imageId);
	}

	@Override
	public ElementImage getElementImage(ElementName name, ImageId imageId) {
		Element element = elements.fetchElement(name);
		return manager.getElementImage(element, imageId);

	}

	@Override
	public ElementImages getElementImages(ElementId id) {
		Element element = elements.fetchElement(id);
		return manager.getElementImages(element);
	}

	@Override
	public ElementImages getElementImages(ElementName name) {
		Element element = elements.fetchElement(name);
		return manager.getElementImages(element);
	}

	@Override
	public void storeElementImages(ElementId id, List<ElementImageReference> images) {
		Element element = elements.fetchElement(id);
		manager.storeElementImages(element,images);
	}

	@Override
	public void storeElementImages(ElementName name, List<ElementImageReference> images) {
		Element element = elements.fetchElement(name);
		manager.storeElementImages(element,images);
	}

	@Override
	public void removeElementImage(ElementId id,ImageId imageId) {
		Element element = elements.fetchElement(id);
		manager.removeElementImage(element,imageId);
	}

	@Override
	public void removeElementImage(ElementName name, ImageId imageId) {
		Element element = elements.fetchElement(name);
		manager.removeElementImage(element,imageId);
	}

    @Override
    public void setZtpImage(ElementId elementId, ImageId imageId) {
        Element element = elements.fetchElement(elementId);
        manager.setZtpImage(element,imageId);
    }

    @Override
    public void setZtpImage(ElementName elementName, ImageId imageId) {
        Element element = elements.fetchElement(elementName);
        manager.setZtpImage(element,imageId);
    }

    @Override
    public void resetZtpImage(ElementId elementId) {
        Element element = elements.fetchElement(elementId);
        manager.resetZtpImage(element);
    }

    @Override
    public void resetZtpImage(ElementName elementName) {
        Element element = elements.fetchElement(elementName);
        manager.resetZtpImage(element);
    }

    @Override
    public ImageReference getZtpImage(ElementId elementId) {
        Element element = elements.fetchElement(elementId);
        return manager.getZtpImage(element);
    }

    @Override
    public ImageReference getZtpImage(ElementName elementName) {
        Element element = elements.fetchElement(elementName);
        return manager.getZtpImage(element);
    }

}

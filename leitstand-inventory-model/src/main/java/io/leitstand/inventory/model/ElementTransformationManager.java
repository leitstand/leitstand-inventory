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

import static io.leitstand.inventory.model.DefaultImageService.imageInfo;
import static io.leitstand.inventory.model.Image.findDefaultImages;
import static io.leitstand.inventory.service.ElementInstalledImage.newElementInstalledImage;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementConfig;
import io.leitstand.inventory.service.ElementConfigReference;
import io.leitstand.inventory.service.ElementConfigs;
import io.leitstand.inventory.service.ElementInstalledImageData;
import io.leitstand.inventory.service.ElementInstalledImages;
import io.leitstand.inventory.service.ElementManagementInterface;
import io.leitstand.inventory.service.ElementSettings;
import io.leitstand.inventory.visitor.ElementConfigVisitor;
import io.leitstand.inventory.visitor.ElementImageVisitor;
import io.leitstand.inventory.visitor.ElementSettingsVisitor;
import io.leitstand.inventory.visitor.ElementTransformation;
import io.leitstand.inventory.visitor.ImageVisitor;
//TODO BLR Add unit test
@Dependent
public class ElementTransformationManager {

	@Inject
	private ElementSettingsManager elementSettings;
	
	@Inject
	private ElementConfigManager elementConfigs;

	@Inject 
	private ElementImageManager elementImages;
	
	@Inject
	@Inventory
	private Repository repository;
	
	public <T> T apply(Element element, 
						ElementTransformation<T> transformation) {
		transformSettings(element, transformation);
		transformConfigs(element, transformation);
		transformImages(element, transformation);
		return transformation.getResult();
	}

	private <T> void transformImages(Element element, ElementTransformation<T> transformation) {
		ElementImageVisitor elementImageVisitor = transformation.visitElementImages();
		if(elementImageVisitor != null) {
			ElementInstalledImages images = elementImages.getElementInstalledImages(element);
			for(ElementInstalledImageData imageData : images.getImages()) {
				elementImageVisitor.visitElementImage(newElementInstalledImage()
													  .withElementId(images.getElementId())
													  .withElementName(images.getElementName())
													  .withElementAlias(images.getElementAlias())
													  .withElementRole(images.getElementRole())
													  .withGroupId(images.getGroupId())
													  .withGroupName(images.getGroupName())
													  .withGroupType(images.getGroupType())
													  .withImage(imageData)
													  .build());
			}
		}
		
		ImageVisitor imageVisitor = transformation.visitDefaultImages();
		if(imageVisitor != null) {
			for(Image image : repository.execute(findDefaultImages(element.getElementRole(), 
																   element.getPlatform()))) {
				imageVisitor.visitImage(imageInfo(image));
			}
		}
	}

	private <T> void transformConfigs(Element element, ElementTransformation<T> transformation) {
		ElementConfigVisitor configsVisitor = transformation.visitElementConfigs();
		if(configsVisitor != null) {
			ElementConfigs configs = elementConfigs.filterElementConfig(element,
																	    configsVisitor.getConfigSelector());
			for(ElementConfigReference ref : configs.getConfigs()) {
				boolean visitData = configsVisitor.visitElementConfig(ref);
				if(visitData) {
					ElementConfig config = elementConfigs.getElementConfig(element,
																		  ref.getConfigId());
					configsVisitor.visitElementConfig(config);
				}
			}
		}
	}

	private <T> void transformSettings(Element element, ElementTransformation<T> transformation) {
		ElementSettingsVisitor settingsVisitor = transformation.visitElementSettings();
		if(settingsVisitor != null) {
			ElementSettings settings = elementSettings.getElementSettings(element);
			settingsVisitor.visitElementSettings(settings);
			for(ElementManagementInterface mgmtInterface : settings.getManagementInterfaces().values()) {
				settingsVisitor.visitElementManagementInterface(mgmtInterface);
			}
		}
	}

}

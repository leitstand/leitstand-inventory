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

import static io.leitstand.inventory.service.ElementConfigId.randomConfigId;
import static io.leitstand.inventory.service.ElementInstalledImageData.newElementInstalledImageData;
import static io.leitstand.inventory.service.ImageName.imageName;
import static io.leitstand.inventory.service.PlatformChipsetName.platformChipsetName;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementConfig;
import io.leitstand.inventory.service.ElementConfigName;
import io.leitstand.inventory.service.ElementConfigReference;
import io.leitstand.inventory.service.ElementConfigs;
import io.leitstand.inventory.service.ElementInstalledImage;
import io.leitstand.inventory.service.ElementInstalledImageData;
import io.leitstand.inventory.service.ElementInstalledImages;
import io.leitstand.inventory.service.ElementManagementInterface;
import io.leitstand.inventory.service.ElementSettings;
import io.leitstand.inventory.service.ImageInfo;
import io.leitstand.inventory.service.ImageService;
import io.leitstand.inventory.visitor.ElementConfigVisitor;
import io.leitstand.inventory.visitor.ElementImageVisitor;
import io.leitstand.inventory.visitor.ElementSettingsVisitor;
import io.leitstand.inventory.visitor.ElementTransformation;
import io.leitstand.inventory.visitor.ImageVisitor;

@RunWith(MockitoJUnitRunner.class)
public class ElementTransformationManagerTest {

	@Mock
	private ElementSettingsManager elementSettings;
	
	@Mock
	private ElementConfigManager elementConfigs;

	@Mock 
	private ElementImageManager elementImages;
	
	@Mock
	private ImageService images;
	
	@Mock
	private Repository repository;
	
	@InjectMocks
	private ElementTransformationManager manager = new ElementTransformationManager();
	
	private ElementTransformation<?> transformation;

	private Element element;
	
	@Before
	public void initTestEnvironment() {
		transformation = mock(ElementTransformation.class);
		element = mock(Element.class);
	}
	
	@Test
	public void visit_all_element_configs() {
		ElementConfigs		   configs  = mock(ElementConfigs.class);
		ElementConfigReference config	= mock(ElementConfigReference.class);
		when(config.getConfigName()).thenReturn(ElementConfigName.valueOf("UNITTEST"));
		when(configs.getConfigs()).thenReturn(asList(config,config));
		when(elementConfigs.filterElementConfig(element, null)).thenReturn(configs);
		
		ElementConfigVisitor visitor = mock(ElementConfigVisitor.class);
		when(transformation.visitElementConfigs()).thenReturn(visitor);

		manager.apply(element, transformation);
		
		verify(visitor,times(2)).visitElementConfig(config);
	}
	
	@Test
	public void visit_filtered_element_configs() {
		ElementConfigs		   configs  = mock(ElementConfigs.class);
		ElementConfigReference config	= mock(ElementConfigReference.class);
		when(config.getConfigName()).thenReturn(ElementConfigName.valueOf("UNITTEST"));
		when(configs.getConfigs()).thenReturn(asList(config,config));
		when(elementConfigs.filterElementConfig(element, "UNITTEST")).thenReturn(configs);
		
		ElementConfigVisitor visitor = mock(ElementConfigVisitor.class);
		when(transformation.visitElementConfigs()).thenReturn(visitor);
		when(visitor.getConfigSelector()).thenReturn("UNITTEST");

		manager.apply(element, transformation);
		
		verify(visitor,times(2)).visitElementConfig(config);
	}
	
	@Test
	public void visit_element_config_data() {
		ElementConfigs		   configs  = mock(ElementConfigs.class);
		ElementConfigReference config	= mock(ElementConfigReference.class);
		ElementConfig		   content  = mock(ElementConfig.class);
		when(config.getConfigId()).thenReturn(randomConfigId());
		when(configs.getConfigs()).thenReturn(asList(config,config));
	
		when(elementConfigs.filterElementConfig(element, null)).thenReturn(configs);
		when(elementConfigs.getElementConfig(element, config.getConfigId())).thenReturn(content);
		ElementConfigVisitor visitor = mock(ElementConfigVisitor.class);
		when(transformation.visitElementConfigs()).thenReturn(visitor);
		when(visitor.visitElementConfig(config)).thenReturn(FALSE).thenReturn(TRUE);

		manager.apply(element, transformation);
		
		verify(visitor,times(2)).visitElementConfig(config);
		verify(visitor,times(1)).visitElementConfig(content);
	}
	
	
	@Test
	public void visit_installed_images() {
		ElementInstalledImages images  = mock(ElementInstalledImages.class);
		List<ElementInstalledImageData> installed = asList(newElementInstalledImageData()
				  										   .withImageName(imageName("UNITTEST"))
				  										   .build());
		when(images.getImages()).thenReturn(installed);
		
		ArgumentCaptor<ElementInstalledImage> captor = forClass(ElementInstalledImage.class);
		when(elementImages.getElementInstalledImages(element)).thenReturn(images);
		ElementImageVisitor visitor = mock(ElementImageVisitor.class);

		when(transformation.visitElementImages()).thenReturn(visitor);
		doNothing().when(visitor).visitElementImage(captor.capture());

		manager.apply(element, transformation);
		
		assertEquals(imageName("UNITTEST"), captor.getValue().getImage().getImageName());

	}
	

	@Test
	public void visit_available_images() {
		Image image = mock(Image.class);
		when(image.getPlatformChipset()).thenReturn(platformChipsetName("unittest"));		
		
		when(image.getImageName()).thenReturn(imageName("UNITTEST"));

		when(images.getImage(image.getImageId())).thenReturn(new ImageInfo());
		
		ImageVisitor visitor = mock(ImageVisitor.class);
		when(transformation.visitDefaultImages()).thenReturn(visitor);

		ArgumentCaptor<ImageInfo> captor = forClass(ImageInfo.class);
		when(transformation.visitDefaultImages()).thenReturn(visitor);
		doNothing().when(visitor).visitImage(captor.capture());

		manager.apply(element, transformation);
		
		assertEquals(imageName("UNITTEST"),captor.getValue().getImageName());
	}
	
	@Test
	public void visit_general_settings() {
		ElementSettings settings 		   = mock(ElementSettings.class);
		ElementManagementInterface mgmtIfc = mock(ElementManagementInterface.class);
		Map<String,ElementManagementInterface> mgmtInterfaces = new HashMap<>();
		mgmtInterfaces.put("unittest",mgmtIfc);
		when(settings.getManagementInterfaces()).thenReturn(mgmtInterfaces);
		when(elementSettings.getElementSettings(element)).thenReturn(settings);
		
		ElementSettingsVisitor visitor = mock(ElementSettingsVisitor.class);
		when(transformation.visitElementSettings()).thenReturn(visitor);

		manager.apply(element, transformation);
		
		verify(visitor).visitElementSettings(settings);
		verify(visitor).visitElementManagementInterface(mgmtIfc);
	}
}

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

import static io.leitstand.commons.db.DatabaseService.prepare;
import static io.leitstand.inventory.model.ElementGroup.findElementGroupById;
import static io.leitstand.inventory.model.ElementRole.findRoleByName;
import static io.leitstand.inventory.model.ElementSettingsMother.element;
import static io.leitstand.inventory.model.Image.findElementRoleImage;
import static io.leitstand.inventory.model.Platform.findByModel;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementImageState.ACTIVE;
import static io.leitstand.inventory.service.ElementImageState.CACHED;
import static io.leitstand.inventory.service.ElementInstalledImageReference.newElementInstalledImageReference;
import static io.leitstand.inventory.service.ImageId.randomImageId;
import static io.leitstand.inventory.service.ImageName.imageName;
import static io.leitstand.inventory.service.ImageState.CANDIDATE;
import static io.leitstand.inventory.service.ImageType.LXC;
import static io.leitstand.inventory.service.Plane.DATA;
import static io.leitstand.inventory.service.PlatformId.randomPlatformId;
import static io.leitstand.inventory.service.Version.version;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.event.Event;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.commons.tx.SubtransactionService;
import io.leitstand.inventory.service.ElementAvailableUpdate;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementImagesService;
import io.leitstand.inventory.service.ElementInstalledImage;
import io.leitstand.inventory.service.ElementInstalledImageData;
import io.leitstand.inventory.service.ElementInstalledImageReference;
import io.leitstand.inventory.service.ElementInstalledImages;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementSettings;
import io.leitstand.inventory.service.ElementSettingsService;
import io.leitstand.inventory.service.ImageId;
import io.leitstand.inventory.service.ImageName;
import io.leitstand.inventory.service.ImageType;
import io.leitstand.inventory.service.Version;

public class ElementImageServiceIT extends InventoryIT{

	private static final ElementId ELEMENT_ID = randomElementId();
	private static final ElementName ELEMENT_NAME = ElementName.valueOf(ElementImageServiceIT.class.getSimpleName());
	
	
	private static final ElementInstalledImageReference ACTIVE_MAJOR_UPGRADE_REF = newElementInstalledImageReference()
																				   .withImageType(LXC)
																				   .withImageName(imageName("JUNIT"))
																				   .withImageVersion(version("2.0.0"))
																				   .withElementImageState(ACTIVE)
																				   .build();

	private static final ElementInstalledImageReference CACHED_MAJOR_UPGRADE_REF = newElementInstalledImageReference()
																				   .withImageType(LXC)
																				   .withImageName(imageName("JUNIT"))
																				   .withImageVersion(version("2.0.0"))
																				   .withElementImageState(CACHED)
																				   .build();
	
	private static final ElementInstalledImageReference ACTIVE_MINOR_UPGRADE_REF = newElementInstalledImageReference()
																				   .withImageType(LXC)
																				   .withImageName(imageName("JUNIT"))
																				   .withImageVersion(version("1.1.0"))
																				   .withElementImageState(ACTIVE)
																				   .build();

	private static final ElementInstalledImageReference CACHED_MINOR_UPGRADE_REF = newElementInstalledImageReference()
																				   .withImageType(LXC)
																				   .withImageName(imageName("JUNIT"))
																				   .withImageVersion(version("1.1.0"))
																				   .withElementImageState(CACHED)
																				   .build();

	private static final ElementInstalledImageReference ACTIVE_PATCH_UPGRADE_REF = newElementInstalledImageReference()
																				   .withImageType(LXC)
																				   .withImageName(imageName("JUNIT"))
																				   .withImageVersion(version("1.0.1"))
																				   .withElementImageState(ACTIVE)
																				   .build();

	private static final ElementInstalledImageReference CACHED_PATCH_UPGRADE_REF = newElementInstalledImageReference()
																				   .withImageType(LXC)
																				   .withImageName(imageName("JUNIT"))
																				   .withImageVersion(version("1.0.1"))
																				   .withElementImageState(CACHED)
																				   .build();
	
	private static final ElementInstalledImageReference ACTIVE_BASE_REF = newElementInstalledImageReference()
																		  .withImageType(LXC)
																		  .withImageName(imageName("JUNIT"))
																		  .withImageVersion(version("1.0.0"))
																		  .withElementImageState(ACTIVE)
																		  .build();

	private static final ElementInstalledImageReference CACHED_BASE_REF = newElementInstalledImageReference()
																		  .withImageType(LXC)
																		  .withImageName(imageName("JUNIT"))
																		  .withImageVersion(version("1.0.0"))
																		  .withElementImageState(CACHED)
																		  .build();
	
	
	private static Image newImage(ImageId imageId, 
								  String org, 
								  ImageType type, 
								  ImageName name,
								  ElementRole role, 
								  Platform platform,
								  String ext, 
								  Version revision, 
								  Date buildDate, 
								  List<Application> applications, 
								  List<Package_Version> packages) {

		Image image = new Image(imageId);
		image.setOrganization(org);
		image.setImageType(type);
		image.setImageName(name);
		image.setElementRole(role);
		image.setExtension(ext);
		image.setImageVersion(revision);
		image.setBuildDate(buildDate);
		image.setPackages(packages);
		image.setApplications(applications);
		image.setPlatform(platform);
		image.setImageState(CANDIDATE);
		return image;
	}
	
	private ElementImagesService service;
	
	@Before
	public void initTestEnvironment() {
		Repository repository = new Repository(getEntityManager());

		ElementProvider elements = new ElementProvider(repository);
		ElementGroupProvider groups = new ElementGroupProvider(repository);
		ElementRoleProvider roles = new ElementRoleProvider(repository);
		PlatformProvider platforms = new PlatformProvider(repository);
		
		ElementSettingsManager manager = new ElementSettingsManager(repository,
																	groups,
																	roles,
																	platforms,
																	elements,
																    mock(Messages.class), 
																    mock(Event.class));
		
		ElementSettingsService elementService = new DefaultElementSettingsService(manager,
																				  elements,
																				  mock(Event.class));
		ElementSettings settings = element(ELEMENT_ID, ELEMENT_NAME);
		service = new DefaultElementImagesService(new ElementImageManager(repository,
												  mock(SubtransactionService.class),
												  mock(Messages.class)), 
												  elements);

		
		transaction(()->{
			ElementRole role = repository.addIfAbsent(findRoleByName(settings.getElementRole()),
													  () -> new ElementRole(settings.getElementRole(),
															  				DATA));

			
			repository.addIfAbsent(findElementGroupById(settings.getGroupId()),
								   () -> {ElementGroup group = new ElementGroup(settings.getGroupId(),
										   										settings.getGroupType(),
										   										settings.getGroupName());
								   		  return group;});
			
			elementService.storeElementSettings(settings);		
			
			Platform platform = repository.addIfAbsent(findByModel("net.rtbrick", "JUNIT"), 
													   () -> new Platform(randomPlatformId(), "net.rtbrick", "JUNIT"));
			
			
			// Create base image
			repository.addIfAbsent(findElementRoleImage(platform, 
														role, 
														LXC,
														imageName("JUNIT"),
														new Version(1,0,0)),
														() ->  newImage(randomImageId(), 
																		"net.rtbrick", 
																		LXC,
																		imageName("JUNIT"),
																		role, 
																		platform, 
																		"test", 
																		new Version(1,0,0), 
																		new Date(), 
																		new LinkedList<>(),
																		new LinkedList<>()));
			// Create patch upgrade
			repository.addIfAbsent(findElementRoleImage(platform, 
														role, 
														LXC,
														imageName("JUNIT"),
														new Version(1,0,1)),
														() ->  newImage(randomImageId(), 
																		"net.rtbrick", 
																		LXC, 
																		imageName("JUNIT"),
																		role, 
																		platform, 
																		"test", 
																		new Version(1,0,1), 
																		new Date(), 
																		new LinkedList<>(),
																		new LinkedList<>()));
			
			// Create minor upgrade
			repository.addIfAbsent(findElementRoleImage(platform, 
														role, 
														LXC,
														imageName("JUNIT"),
														new Version(1,1,0)),
														() ->  newImage(randomImageId(), 
																		"net.rtbrick", 
																		LXC,
																		imageName("JUNIT"),
																		role, 
																		platform, 
																		"test", 
																		new Version(1,1,0), 
																		new Date(), 
																		new LinkedList<>(),
																		new LinkedList<>()));
			
			// Create major upgrade
			repository.addIfAbsent(findElementRoleImage(platform, 
														role, 
														LXC,
														imageName("JUNIT"),
														new Version(2,0,0)),
														() ->  newImage(randomImageId(), 
																		"net.rtbrick", 
																		LXC,
																		imageName("JUNIT"),
																		role, 
																		platform, 
																		"test", 
																		new Version(2,0,0), 
																		new Date(), 
																		new LinkedList<>(),
																		new LinkedList<>()));
			
		});
	}
	
	@After
	public void clearTestEnvironment() {
		transaction(() -> {
			getDatabase().executeUpdate(prepare("DELETE FROM inventory.element_image"));
			
		});
		
	}

	@Test
	public void set_initial_installed_image_revision_for_element_identified_by_id() {
		transaction(()->{
			service.storeInstalledImages(ELEMENT_ID, asList(ACTIVE_BASE_REF));
		});
		transaction(()->{
			ElementInstalledImages installedImages = service.getElementInstalledImages(ELEMENT_ID);
			assertNotNull(installedImages);
			assertEquals(1, installedImages.getImages().size());
			ElementInstalledImageData installedImage = installedImages.getImages().get(0);
			assertEquals(new Version(1,0,0),installedImage.getImageVersion());
			assertEquals(LXC,installedImage.getImageType());
			
			List<ElementAvailableUpdate> updates = installedImage.getAvailableUpdates();
			assertEquals(3,updates.size());
			assertTrue(updates.get(0).isMajorUpdate());
			assertTrue(updates.get(1).isMinorUpdate());
			assertTrue(updates.get(2).isPatch());
		});
	}
	
	@Test
	public void register_patch_update_pulled_for_element_identified_by_id() {
		transaction(()->{
			service.storeInstalledImages(ELEMENT_ID, asList(ACTIVE_BASE_REF, 
															CACHED_PATCH_UPGRADE_REF));
		});
		transaction(()->{
			ElementInstalledImages installedImages = service.getElementInstalledImages(ELEMENT_ID);
			assertNotNull(installedImages);
			assertEquals(2, installedImages.getImages().size());
			ElementInstalledImageData installedImage = installedImages.getImages().get(0);
			assertEquals(new Version(1,0,0),installedImage.getImageVersion());
			assertEquals(LXC,installedImage.getImageType());
			
			List<ElementAvailableUpdate> updates = installedImage.getAvailableUpdates();
			assertEquals(3,updates.size());
			assertTrue(updates.get(0).isMajorUpdate());
			assertTrue(updates.get(1).isMinorUpdate());
			assertTrue(updates.get(2).isPatch());
		});
	}
	
	@Test
	public void register_minor_update_pulled_for_element_identified_by_id() {
		transaction(()->{
			service.storeInstalledImages(ELEMENT_ID, asList(ACTIVE_BASE_REF,
														  	CACHED_MINOR_UPGRADE_REF));
		});
		transaction(()->{
			ElementInstalledImages installedImages = service.getElementInstalledImages(ELEMENT_ID);
			assertNotNull(installedImages);
			assertEquals(2, installedImages.getImages().size());
			ElementInstalledImageData installedImage = installedImages.getImages().get(0);
			assertEquals(new Version(1,0,0),installedImage.getImageVersion());
			assertEquals(LXC,installedImage.getImageType());
			
			List<ElementAvailableUpdate> updates = installedImage.getAvailableUpdates();
			assertEquals(3,updates.size());
			assertTrue(updates.get(0).isMajorUpdate());
			assertTrue(updates.get(1).isMinorUpdate());
			assertTrue(updates.get(2).isPatch());
		});
	}
	
	@Test
	public void register_major_update_pulled_for_element_identified_by_id() {
		transaction(()->{
			service.storeInstalledImages(ELEMENT_ID, asList(ACTIVE_BASE_REF,
														    CACHED_MAJOR_UPGRADE_REF));
		});
		transaction(()->{
			ElementInstalledImages installedImages = service.getElementInstalledImages(ELEMENT_ID);
			assertNotNull(installedImages);
			assertEquals(2, installedImages.getImages().size());
			ElementInstalledImageData installedImage = installedImages.getImages().get(0);
			assertEquals(new Version(1,0,0),installedImage.getImageVersion());
			assertEquals(LXC,installedImage.getImageType());
			
			List<ElementAvailableUpdate> updates = installedImage.getAvailableUpdates();
			assertEquals(3,updates.size());
			assertTrue(updates.get(0).isMajorUpdate());
			assertTrue(updates.get(1).isMinorUpdate());
			assertTrue(updates.get(2).isPatch());
		});
	}
	
	@Test
	public void register_patch_update_activated_for_element_identified_by_id() {
		transaction(()->{
			service.storeInstalledImages(ELEMENT_ID, asList(ACTIVE_PATCH_UPGRADE_REF));
		});
		transaction(()->{
			ElementInstalledImages installedImages = service.getElementInstalledImages(ELEMENT_ID);
			assertNotNull(installedImages);
			assertEquals(1, installedImages.getImages().size());
			ElementInstalledImageData installedImage = installedImages.getImages().get(0);
			assertEquals(new Version(1,0,1),installedImage.getImageVersion());
			assertEquals(LXC,installedImage.getImageType());
			
			List<ElementAvailableUpdate> updates = installedImage.getAvailableUpdates();
			assertEquals(2,updates.size());
			assertTrue(updates.get(0).isMajorUpdate());
			assertTrue(updates.get(1).isMinorUpdate());
		});
	}
	
	@Test
	public void register_minor_update_activated_for_element_identified_by_id() {
		transaction(()->{
			service.storeInstalledImages(ELEMENT_ID, asList(ACTIVE_MINOR_UPGRADE_REF));
		});
		transaction(()->{
			ElementInstalledImages installedImages = service.getElementInstalledImages(ELEMENT_ID);
			assertNotNull(installedImages);
			assertEquals(1, installedImages.getImages().size());
			ElementInstalledImageData installedImage = installedImages.getImages().get(0);
			assertEquals(new Version(1,1,0),installedImage.getImageVersion());
			assertEquals(LXC,installedImage.getImageType());
			
			List<ElementAvailableUpdate> updates = installedImage.getAvailableUpdates();
			assertEquals(1,updates.size());
			assertTrue(updates.get(0).isMajorUpdate());
		});
	}
	
	@Test
	public void register_major_update_activated_for_element_identified_by_id() {
		transaction(()->{
			service.storeInstalledImages(ELEMENT_ID, asList(ACTIVE_MAJOR_UPGRADE_REF));
		});
		transaction(()->{
			ElementInstalledImages installedImages = service.getElementInstalledImages(ELEMENT_ID);
			assertNotNull(installedImages);
			assertEquals(1, installedImages.getImages().size());
			ElementInstalledImageData installedImage = installedImages.getImages().get(0);
			assertEquals(new Version(2,0,0),installedImage.getImageVersion());
			assertEquals(LXC,installedImage.getImageType());
			
			List<ElementAvailableUpdate> updates = installedImage.getAvailableUpdates();
			assertTrue(updates.isEmpty());
		});
	}

	@Test
	public void set_initial_installed_image_revision_for_element_identified_by_name() {
		transaction(()->{
			service.storeInstalledImages(ELEMENT_NAME, asList(ACTIVE_BASE_REF));
		});
		transaction(()->{
			ElementInstalledImages installedImages = service.getElementInstalledImages(ELEMENT_NAME);
			assertNotNull(installedImages);
			assertEquals(1, installedImages.getImages().size());
			ElementInstalledImageData installedImage = installedImages.getImages().get(0);
			assertEquals(new Version(1,0,0),installedImage.getImageVersion());
			assertEquals(LXC,installedImage.getImageType());
			
			List<ElementAvailableUpdate> updates = installedImage.getAvailableUpdates();
			assertEquals(3,updates.size());
			assertTrue(updates.get(0).isMajorUpdate());
			assertTrue(updates.get(1).isMinorUpdate());
			assertTrue(updates.get(2).isPatch());
		});
	}
	
	@Test
	public void register_patch_update_pulled_for_element_identified_by_name() {
		transaction(()->{
	
			service.storeInstalledImages(ELEMENT_NAME, asList(ACTIVE_BASE_REF, CACHED_PATCH_UPGRADE_REF));
		});
		transaction(()->{
			ElementInstalledImages installedImages = service.getElementInstalledImages(ELEMENT_NAME);
			assertNotNull(installedImages);
			assertEquals(2, installedImages.getImages().size());
			ElementInstalledImageData installedImage = installedImages.getImages().get(0);
			assertEquals(new Version(1,0,0),installedImage.getImageVersion());
			assertEquals(LXC,installedImage.getImageType());
			
			List<ElementAvailableUpdate> updates = installedImage.getAvailableUpdates();
			assertEquals(3,updates.size());
			assertTrue(updates.get(0).isMajorUpdate());
			assertTrue(updates.get(1).isMinorUpdate());
			assertTrue(updates.get(2).isPatch());
		});
	}
	
	@Test
	public void register_minor_update_pulled_for_element_identified_by_name() {
		transaction(()->{
			service.storeInstalledImages(ELEMENT_NAME, asList(ACTIVE_BASE_REF,CACHED_MINOR_UPGRADE_REF));
		});
		transaction(()->{
			ElementInstalledImages installedImages = service.getElementInstalledImages(ELEMENT_NAME);
			assertNotNull(installedImages);
			assertEquals(2, installedImages.getImages().size());
			ElementInstalledImageData installedImage = installedImages.getImages().get(0);
			assertEquals(new Version(1,0,0),installedImage.getImageVersion());
			assertEquals(LXC,installedImage.getImageType());
			
			List<ElementAvailableUpdate> updates = installedImage.getAvailableUpdates();
			assertEquals(3,updates.size());
			assertTrue(updates.get(0).isMajorUpdate());
			assertTrue(updates.get(1).isMinorUpdate());
			assertTrue(updates.get(2).isPatch());
		});
	}
	
	@Test
	public void register_major_update_pulled_for_element_identified_by_name() {
		transaction(()->{
			service.storeInstalledImages(ELEMENT_NAME, asList(ACTIVE_BASE_REF, CACHED_MAJOR_UPGRADE_REF));
		});
		transaction(()->{
			ElementInstalledImages installedImages = service.getElementInstalledImages(ELEMENT_NAME);
			assertNotNull(installedImages);
			assertEquals(2, installedImages.getImages().size());
			ElementInstalledImageData installedImage = installedImages.getImages().get(0);
			assertEquals(new Version(1,0,0),installedImage.getImageVersion());
			assertEquals(LXC,installedImage.getImageType());
			
			List<ElementAvailableUpdate> updates = installedImage.getAvailableUpdates();
			assertEquals(3,updates.size());
			assertTrue(updates.get(0).isMajorUpdate());
			assertTrue(updates.get(1).isMinorUpdate());
			assertTrue(updates.get(2).isPatch());
		});
	}
	
	@Test
	public void register_patch_update_activated_for_element_identified_by_name() {
		transaction(()->{
			service.storeInstalledImages(ELEMENT_NAME, asList(ACTIVE_PATCH_UPGRADE_REF));
		});
		transaction(()->{
			ElementInstalledImages installedImages = service.getElementInstalledImages(ELEMENT_NAME);
			assertNotNull(installedImages);
			assertEquals(1, installedImages.getImages().size());
			ElementInstalledImageData installedImage = installedImages.getImages().get(0);
			assertEquals(new Version(1,0,1),installedImage.getImageVersion());
			assertEquals(LXC,installedImage.getImageType());
			
			List<ElementAvailableUpdate> updates = installedImage.getAvailableUpdates();
			assertEquals(2,updates.size());
			assertTrue(updates.get(0).isMajorUpdate());
			assertTrue(updates.get(1).isMinorUpdate());
		});
	}
	
	@Test
	public void register_minor_update_activated_for_element_identified_by_name() {
		transaction(()->{
			service.storeInstalledImages(ELEMENT_NAME, asList(ACTIVE_MINOR_UPGRADE_REF));
		});
		transaction(()->{
			ElementInstalledImages installedImages = service.getElementInstalledImages(ELEMENT_NAME);
			assertNotNull(installedImages);
			assertEquals(1, installedImages.getImages().size());
			ElementInstalledImageData installedImage = installedImages.getImages().get(0);
			assertEquals(new Version(1,1,0),installedImage.getImageVersion());
			assertEquals(LXC,installedImage.getImageType());
			
			List<ElementAvailableUpdate> updates = installedImage.getAvailableUpdates();
			assertEquals(1,updates.size());
			assertTrue(updates.get(0).isMajorUpdate());
		});
	}
	
	@Test
	public void register_major_update_activated_for_element_identified_by_name() {
		transaction(()->{
			service.storeInstalledImages(ELEMENT_NAME, asList(ACTIVE_MAJOR_UPGRADE_REF));
		});
		transaction(()->{
			ElementInstalledImages installedImages = service.getElementInstalledImages(ELEMENT_NAME);
			assertNotNull(installedImages);
			assertEquals(1, installedImages.getImages().size());
			ElementInstalledImageData installedImage = installedImages.getImages().get(0);
			assertEquals(new Version(2,0,0),installedImage.getImageVersion());
			assertEquals(LXC,installedImage.getImageType());
			
			List<ElementAvailableUpdate> updates = installedImage.getAvailableUpdates();
			assertTrue(updates.isEmpty());
		});
	}
	
	@Test
	public void load_installed_image_by_element_id() {
		transaction(()->{
			service.storeInstalledImages(ELEMENT_ID, asList(ACTIVE_MAJOR_UPGRADE_REF));
		});
		transaction(()->{
			ElementInstalledImages installedImages = service.getElementInstalledImages(ELEMENT_ID);
			assertNotNull(installedImages);
			ElementInstalledImage installedImage = service.getElementInstalledImage(ELEMENT_ID, installedImages.getImages().get(0).getImageId());
			assertEquals(installedImages.getImages().get(0),installedImage.getImage());
		});
		
	}
	
	@Test
	public void load_installed_image_by_element_name() {
		transaction(()->{
			service.storeInstalledImages(ELEMENT_ID, asList(ACTIVE_MAJOR_UPGRADE_REF));
		});
		transaction(()->{
			ElementInstalledImages installedImages = service.getElementInstalledImages(ELEMENT_NAME);
			assertNotNull(installedImages);
			ElementInstalledImage installedImage = service.getElementInstalledImage(ELEMENT_NAME, installedImages.getImages().get(0).getImageId());
			assertEquals(installedImages.getImages().get(0),installedImage.getImage());
		});
		
	}

	
}

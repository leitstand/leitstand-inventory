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

import static io.leitstand.inventory.model.Element.findElementByName;
import static io.leitstand.inventory.model.ElementGroup.findElementGroupByName;
import static io.leitstand.inventory.model.ElementRole.findRoleByName;
import static io.leitstand.inventory.model.Platform.findPlatformById;
import static io.leitstand.inventory.service.ApplicationName.applicationName;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementImageState.ACTIVE;
import static io.leitstand.inventory.service.ElementImageState.CACHED;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.ImageId.randomImageId;
import static io.leitstand.inventory.service.ImageInfo.newImageInfo;
import static io.leitstand.inventory.service.ImageName.imageName;
import static io.leitstand.inventory.service.ImageState.CANDIDATE;
import static io.leitstand.inventory.service.ImageState.NEW;
import static io.leitstand.inventory.service.ImageType.imageType;
import static io.leitstand.inventory.service.PackageVersionInfo.newPackageVersionInfo;
import static io.leitstand.inventory.service.Plane.DATA;
import static io.leitstand.inventory.service.PlatformChipsetName.platformChipsetName;
import static io.leitstand.inventory.service.PlatformId.randomPlatformId;
import static io.leitstand.inventory.service.PlatformName.platformName;
import static io.leitstand.inventory.service.ReasonCode.IVT0200E_IMAGE_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0201I_IMAGE_STATE_UPDATED;
import static io.leitstand.inventory.service.ReasonCode.IVT0202I_IMAGE_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0203I_IMAGE_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0204E_IMAGE_NOT_REMOVABLE;
import static io.leitstand.inventory.service.ReasonCode.IVT0400E_ELEMENT_ROLE_NOT_FOUND;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.hasSizeOf;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.enterprise.event.Event;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Message;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.event.ImageAddedEvent;
import io.leitstand.inventory.event.ImageEvent;
import io.leitstand.inventory.event.ImageRemovedEvent;
import io.leitstand.inventory.event.ImageStateChangedEvent;
import io.leitstand.inventory.event.ImageStoredEvent;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ImageId;
import io.leitstand.inventory.service.ImageInfo;
import io.leitstand.inventory.service.ImageName;
import io.leitstand.inventory.service.ImageService;
import io.leitstand.inventory.service.ImageStatistics;
import io.leitstand.inventory.service.ImageType;
import io.leitstand.inventory.service.PlatformChipsetName;
import io.leitstand.inventory.service.PlatformId;
import io.leitstand.inventory.service.PlatformName;
import io.leitstand.inventory.service.Version;
import io.leitstand.testing.ut.LeitstandCoreMatchers;

public class ImageServiceIT extends InventoryIT{
    
    @Rule
    public ExpectedException exception = ExpectedException.none();

	private static final PlatformId          PLATFORM_ID      = randomPlatformId();
	private static final PlatformName        PLATFORM_NAME    = platformName(ImageServiceIT.class.getSimpleName());
	private static final PlatformChipsetName PLATFORM_CHIPSET = platformChipsetName("unittest");
	private static final ElementName         ELEMENT_NAME     = elementName("element");
	private static final ElementGroupName    GROUP_NAME       = groupName("group");
	private static final ElementGroupType    GROUP_TYPE       = groupType("unittest");
	private static final ElementRoleName     ELEMENT_ROLE     = elementRoleName("role");
	private static final ImageId             IMAGE_ID         = randomImageId();
	private static final ImageName           IMAGE_NAME       = imageName("image");
	private static final ImageType           IMAGE_TYPE       = imageType("lxc");
	private static final String              IMAGE_CATEGORY   = "dummy";
	private static final String              IMAGE_EXTENSION  = "tar.gz";
	private static final String              IMAGE_ORG        = "io.leitstand";
	private static final Version             IMAGE_VERSION    = new Version(1,0,0);
	private static final Version             PATCH            = new Version(1,0,1);
	private static final Version             MINOR_UPGRADE    = new Version(1,1,0);
	private static final Version             MAJOR_UPGRADE    = new Version(2,0,0);
	
	private ImageService service;
	private ArgumentCaptor<Message> messageCaptor;
	private ArgumentCaptor<ImageEvent> eventCaptor;
	private Repository repository;
	
	@Before
	@SuppressWarnings("unchecked")
	public void initTestEnvironment() {
		repository = new Repository(getEntityManager());
		messageCaptor = ArgumentCaptor.forClass(Message.class);
		Messages messages = mock(Messages.class);
		doNothing().when(messages).add(messageCaptor.capture());
		eventCaptor = ArgumentCaptor.forClass(ImageEvent.class);
		Event<ImageEvent> events = mock(Event.class);
		doNothing().when(events).fire(eventCaptor.capture());
		PackageVersionService pkgVersions = new PackageVersionService(repository);
		
		service = new DefaultImageService(pkgVersions,
										  repository,
										  getDatabase(),
										  messages,
										  events);
		
		transaction(()->{
			repository.addIfAbsent(findRoleByName(ELEMENT_ROLE), 
								   () -> new ElementRole(ELEMENT_ROLE,DATA));
		});
	}
	
	@Test
	public void raise_entity_not_found_exception_when_image_does_not_exist() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0200E_IMAGE_NOT_FOUND));
	    
		transaction(()->{
			service.getImage(randomImageId());
			fail("EntityNotFoundException expected");
		});
	}
	
	@Test
	public void raise_entity_not_found_exception_when_role_does_not_exist() {
	    exception.expect(EntityNotFoundException.class);
	    exception.expect(reason(IVT0400E_ELEMENT_ROLE_NOT_FOUND));
	    
		ImageInfo imageInfo = newImageInfo()
				  			  .withImageId(IMAGE_ID)
				  			  .withImageType(IMAGE_TYPE)
				  			  .withImageName(IMAGE_NAME)
				  			  .withImageState(NEW)
				  			  .withImageVersion(IMAGE_VERSION)
				  			  .withExtension(IMAGE_EXTENSION)
				  			  .withElementRoles(asList(elementRoleName("unknown")))
				  			  .withOrganization(IMAGE_ORG)
				  			  .withCategory(IMAGE_CATEGORY)
				  			  .withPlatformChipset(PLATFORM_CHIPSET)
				  			  .build();
		
		
		transaction(()->{
			service.storeImage(imageInfo);
			fail("EntityNotFoundException expected");
		});
	}
	
	@Test
	public void store_image_metadata() {
		ImageInfo imageInfo = newImageInfo()
							  .withImageId(IMAGE_ID)
							  .withImageType(IMAGE_TYPE)
							  .withImageName(IMAGE_NAME)
							  .withImageState(NEW)
							  .withImageVersion(IMAGE_VERSION)
							  .withExtension(IMAGE_EXTENSION)
							  .withElementRoles(asList(ELEMENT_ROLE))
							  .withOrganization(IMAGE_ORG)
							  .withCategory(IMAGE_CATEGORY)
							  .withPlatformChipset(PLATFORM_CHIPSET)
							  .build();
		transaction(()->{
			boolean created = service.storeImage(imageInfo);
			assertTrue(created);
			assertThat(eventCaptor.getValue(),is(ImageAddedEvent.class));
			assertEquals(IVT0202I_IMAGE_STORED.getReasonCode(), 
						 messageCaptor.getValue().getReason());
		});
		
		transaction(()->{
			ImageInfo storedImage = service.getImage(imageInfo.getImageId());
			assertEquals(IMAGE_ID,storedImage.getImageId());
			assertEquals(IMAGE_NAME,storedImage.getImageName());
			assertEquals(NEW,storedImage.getImageState());
			assertEquals(IMAGE_TYPE,storedImage.getImageType());
			assertEquals(new Version(1,0,0),storedImage.getImageVersion());
			assertThat(storedImage.getElementRoles(),hasSizeOf(1));
			assertEquals(ELEMENT_ROLE,storedImage.getElementRoles().get(0));
			assertEquals(IMAGE_EXTENSION,storedImage.getExtension());
			assertEquals(IMAGE_ORG,storedImage.getOrganization());
			assertEquals(IMAGE_CATEGORY,storedImage.getCategory());
			assertTrue(storedImage.getApplications().isEmpty());
			assertTrue(storedImage.getPackages().isEmpty());
			assertTrue(storedImage.getChecksums().isEmpty());
			assertEquals(PLATFORM_CHIPSET,storedImage.getPlatformChipset());
			assertNull(storedImage.getBuildId());
			assertNull(storedImage.getBuildDate());
			
		});
	}
	
	@Test
	public void store_image_metadata_with_build_info() {
		ImageInfo imageInfo = newImageInfo()
							  .withImageId(IMAGE_ID)
							  .withImageType(IMAGE_TYPE)
							  .withImageName(IMAGE_NAME)
							  .withImageState(NEW)
							  .withImageVersion(IMAGE_VERSION)
							  .withExtension(IMAGE_EXTENSION)
							  .withElementRoles(asList(ELEMENT_ROLE))
							  .withOrganization(IMAGE_ORG)
							  .withCategory(IMAGE_CATEGORY)
							  .withPlatformChipset(PLATFORM_CHIPSET)
							  .withBuildId(UUID.randomUUID().toString())
							  .withBuildDate(new Date())

							  .build();
		transaction(()->{
			boolean created = service.storeImage(imageInfo);
			assertTrue(created);
			assertThat(eventCaptor.getValue(),is(ImageAddedEvent.class));
			assertEquals(IVT0202I_IMAGE_STORED.getReasonCode(), 
					 	 messageCaptor.getValue().getReason());
		});
		
		transaction(()->{
			ImageInfo storedImage = service.getImage(imageInfo.getImageId());
			assertEquals(IMAGE_ID,storedImage.getImageId());
			assertEquals(IMAGE_NAME,storedImage.getImageName());
			assertEquals(NEW,storedImage.getImageState());
			assertEquals(IMAGE_TYPE,storedImage.getImageType());
			assertEquals(IMAGE_VERSION,storedImage.getImageVersion());
			assertEquals(ELEMENT_ROLE,storedImage.getElementRoles().get(0));
			assertThat(storedImage.getElementRoles(),hasSizeOf(1));
			assertEquals(IMAGE_EXTENSION,storedImage.getExtension());
			assertEquals(IMAGE_ORG,storedImage.getOrganization());
			assertEquals(IMAGE_CATEGORY,storedImage.getCategory());
			assertTrue(storedImage.getApplications().isEmpty());
			assertTrue(storedImage.getPackages().isEmpty());
			assertTrue(storedImage.getChecksums().isEmpty());
			assertEquals(imageInfo.getBuildDate(),storedImage.getBuildDate());
			assertEquals(imageInfo.getBuildId(),storedImage.getBuildId());
			
		});
	}
	
	@Test
	public void store_image_metadata_with_checksums() {
		Map<String,String> checksums = new HashMap<>();
		checksums.put("MD5", "md5-checksum");
		checksums.put("SHA256", "sha256-checksum");
		
		ImageInfo imageInfo = newImageInfo()
							  .withImageId(IMAGE_ID)
							  .withImageType(IMAGE_TYPE)
							  .withImageName(IMAGE_NAME)
							  .withImageState(NEW)
							  .withImageVersion(IMAGE_VERSION)
							  .withExtension(IMAGE_EXTENSION)
							  .withElementRoles(asList(ELEMENT_ROLE))
							  .withOrganization(IMAGE_ORG)
							  .withCategory(IMAGE_CATEGORY)
							  .withPlatformChipset(PLATFORM_CHIPSET)
							  .withBuildId(UUID.randomUUID().toString())
							  .withBuildDate(new Date())
							  .withChecksums(checksums)	
							  .build();
		transaction(()->{
			boolean created = service.storeImage(imageInfo);
			assertTrue(created);
			assertThat(eventCaptor.getValue(),is(ImageAddedEvent.class));
		});
		
		transaction(()->{
			ImageInfo storedImage = service.getImage(imageInfo.getImageId());
			assertEquals(IMAGE_ID,storedImage.getImageId());
			assertEquals(IMAGE_NAME,storedImage.getImageName());
			assertEquals(NEW,storedImage.getImageState());
			assertEquals(IMAGE_TYPE,storedImage.getImageType());
			assertEquals(IMAGE_VERSION,storedImage.getImageVersion());
			assertEquals(ELEMENT_ROLE,storedImage.getElementRoles().get(0));
			assertThat(storedImage.getElementRoles(),hasSizeOf(1));
			assertEquals(IMAGE_EXTENSION,storedImage.getExtension());
			assertEquals(IMAGE_ORG,storedImage.getOrganization());
			assertEquals(IMAGE_CATEGORY,storedImage.getCategory());
			assertTrue(storedImage.getApplications().isEmpty());
			assertTrue(storedImage.getPackages().isEmpty());
			assertEquals(imageInfo.getBuildDate(),storedImage.getBuildDate());
			assertEquals(imageInfo.getBuildId(),storedImage.getBuildId());
			assertEquals(checksums,imageInfo.getChecksums());
		});
	}
	
	@Test
	public void store_image_with_applications() {
		ImageInfo imageInfo = newImageInfo()
							  .withImageId(IMAGE_ID)
							  .withImageType(IMAGE_TYPE)
							  .withImageName(IMAGE_NAME)
							  .withImageState(NEW)
							  .withImageVersion(IMAGE_VERSION)
							  .withExtension(IMAGE_EXTENSION)
							  .withElementRoles(asList(ELEMENT_ROLE))
							  .withOrganization(IMAGE_ORG)
							  .withCategory(IMAGE_CATEGORY)
							  .withPlatformChipset(PLATFORM_CHIPSET)
							  .withBuildId(UUID.randomUUID().toString())
							  .withBuildDate(new Date())
							  .withApplications(applicationName("app1"),
									  			applicationName("app2"))	
							  .build();
		transaction(()->{
			boolean created = service.storeImage(imageInfo);
			assertTrue(created);
			assertThat(eventCaptor.getValue(),is(ImageAddedEvent.class));
			assertEquals(IVT0202I_IMAGE_STORED.getReasonCode(), 
					 	 messageCaptor.getValue().getReason());
		});
		
		transaction(()->{
			ImageInfo storedImage = service.getImage(imageInfo.getImageId());
			assertEquals(IMAGE_ID,storedImage.getImageId());
			assertEquals(IMAGE_NAME,storedImage.getImageName());
			assertEquals(NEW,storedImage.getImageState());
			assertEquals(IMAGE_TYPE,storedImage.getImageType());
			assertEquals(IMAGE_VERSION,storedImage.getImageVersion());
			assertEquals(ELEMENT_ROLE,storedImage.getElementRoles().get(0));
			assertThat(storedImage.getElementRoles(),hasSizeOf(1));
			assertEquals(IMAGE_EXTENSION,storedImage.getExtension());
			assertEquals(IMAGE_ORG,storedImage.getOrganization());
			assertEquals(IMAGE_CATEGORY,storedImage.getCategory());
			assertTrue(storedImage.getPackages().isEmpty());
			assertTrue(storedImage.getChecksums().isEmpty());
			assertEquals(imageInfo.getBuildDate(),storedImage.getBuildDate());
			assertEquals(imageInfo.getBuildId(),storedImage.getBuildId());
			assertEquals(asList(applicationName("app1"),
								applicationName("app2")),
						 storedImage.getApplications());
		});
	}
	
	@Test
	public void remove_image_with_applications_and_package_revisions() {
		ImageInfo imageInfo = newImageInfo()
							  .withImageId(IMAGE_ID)
							  .withImageType(IMAGE_TYPE)
							  .withImageName(IMAGE_NAME)
							  .withImageState(NEW)
							  .withImageVersion(IMAGE_VERSION)
							  .withExtension(IMAGE_EXTENSION)
							  .withElementRoles(asList(ELEMENT_ROLE))
							  .withOrganization(IMAGE_ORG)
							  .withCategory(IMAGE_CATEGORY)
							  .withPlatformChipset(PLATFORM_CHIPSET)
							  .withApplications(applicationName("app1"))
							  .withPackages(newPackageVersionInfo()
									  		.withOrganization("io.leitstand")
									  		.withPackageName("sample")
									  		.withPackageExtension("so")
									  		.withPackageVersion(new Version(1,0,0)))
							  .build();
		transaction(()->{
			boolean created = service.storeImage(imageInfo);
			assertTrue(created);
			assertThat(eventCaptor.getValue(),is(ImageAddedEvent.class));
			assertEquals(IVT0202I_IMAGE_STORED.getReasonCode(), 
						 messageCaptor.getValue().getReason());
		});
		
		transaction(()->{
			service.removeImage(IMAGE_ID);
			assertThat(eventCaptor.getValue(),is(ImageRemovedEvent.class));
			assertEquals(IVT0203I_IMAGE_REMOVED.getReasonCode(), 
					 	 messageCaptor.getValue().getReason());

		});
		
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0200E_IMAGE_NOT_FOUND));
		
		transaction(()->{
			service.getImage(imageInfo.getImageId());
		});
	}
	
	@Test
	public void update_image_state() {
		ImageInfo imageInfo = newImageInfo()
							  .withImageId(IMAGE_ID)
							  .withImageType(IMAGE_TYPE)
							  .withImageName(IMAGE_NAME)
							  .withImageState(NEW)
							  .withImageVersion(IMAGE_VERSION)
							  .withExtension(IMAGE_EXTENSION)
							  .withElementRoles(asList(ELEMENT_ROLE))
							  .withOrganization(IMAGE_ORG)
							  .withCategory(IMAGE_CATEGORY)
							  .withPlatformChipset(PLATFORM_CHIPSET)
							  .withApplications(applicationName("app1"))
							  .withPackages(newPackageVersionInfo()
									  		.withOrganization("io.leitstand")
									  		.withPackageName("sample")
									  		.withPackageExtension("so")
									  		.withPackageVersion(new Version(1,0,0)))
							  .build();
		transaction(()->{
			service.storeImage(imageInfo);
		});
		
		transaction(()->{
			service.updateImageState(IMAGE_ID,CANDIDATE);
			assertThat(eventCaptor.getValue(),is(ImageStateChangedEvent.class));
			assertEquals(IVT0201I_IMAGE_STATE_UPDATED.getReasonCode(), 
					 	 messageCaptor.getValue().getReason());

		});
		
		transaction(()->{
			assertEquals(CANDIDATE,service.getImage(imageInfo.getImageId()).getImageState());
		});
		
	}
	
	
	@Test
	public void rename_image() {
		ImageInfo imageInfo = newImageInfo()
							  .withImageId(IMAGE_ID)
							  .withImageType(IMAGE_TYPE)
							  .withImageName(IMAGE_NAME)
							  .withImageState(NEW)
							  .withImageVersion(IMAGE_VERSION)
							  .withExtension(IMAGE_EXTENSION)
							  .withElementRoles(asList(ELEMENT_ROLE))
							  .withOrganization(IMAGE_ORG)
							  .withCategory(IMAGE_CATEGORY)
							  .withPlatformChipset(PLATFORM_CHIPSET)
							  .withApplications(applicationName("app1"))
							  .withPackages(newPackageVersionInfo()
									  		.withOrganization("io.leitstand")
									  		.withPackageName("sample")
									  		.withPackageExtension("so")
									  		.withPackageVersion(new Version(1,0,0)))
							  .build();
		transaction(()->{
			service.storeImage(imageInfo);
		});
		
		ImageInfo renamedImage = newImageInfo()
								 .withImageId(IMAGE_ID)
								 .withImageType(IMAGE_TYPE)
								 .withImageName(imageName("renamed_image"))
								 .withImageState(NEW)
								 .withImageVersion(IMAGE_VERSION)
								 .withExtension(IMAGE_EXTENSION)
								 .withElementRoles(asList(ELEMENT_ROLE))
								 .withOrganization(IMAGE_ORG)
								 .withCategory(IMAGE_CATEGORY)
								 .withPlatformChipset(PLATFORM_CHIPSET)
								 .withApplications(applicationName("app1"))
								 .withPackages(newPackageVersionInfo()
										 	   .withOrganization("io.leitstand")
										 	   .withPackageName("sample")
										 	   .withPackageExtension("so")
										 	   .withPackageVersion(new Version(1,0,0)))
								 .build();
		
		transaction(()->{
			service.storeImage(renamedImage);
			assertThat(eventCaptor.getValue(),is(ImageStoredEvent.class));
			assertEquals(IVT0202I_IMAGE_STORED.getReasonCode(), 
					 	 messageCaptor.getValue().getReason());

		});
		
		transaction(()->{
			assertEquals(imageName("renamed_image"),service.getImage(imageInfo.getImageId()).getImageName());

		});
		
	}
	
	@Test
	public void cannot_remove_bound_image() {
		
		transaction(() -> {
			ElementRole  role  = repository.execute(findRoleByName(ELEMENT_ROLE));
			ElementGroup group = repository.addIfAbsent(findElementGroupByName(GROUP_TYPE,GROUP_NAME), 
														() -> new ElementGroup(randomGroupId(), 
															  				   GROUP_TYPE, 
																		  	   GROUP_NAME));
			
			Platform platform = repository.addIfAbsent(findPlatformById(PLATFORM_ID),
													   ()->new Platform(PLATFORM_ID,
															   			PLATFORM_NAME,
															   			PLATFORM_CHIPSET));
			
			
			Element element = repository.addIfAbsent(findElementByName(ELEMENT_NAME), 
													 () -> new Element(group, 
															 		   role, 
															 		   platform,
															 		   randomElementId(), 
															 		   ELEMENT_NAME));

			Image image = new Image(IMAGE_ID,
									IMAGE_TYPE,
									IMAGE_NAME,
									asList(role),
									PLATFORM_CHIPSET,
									IMAGE_VERSION);
			image.setOrganization(IMAGE_ORG);
			repository.add(image);
			repository.add(new Element_Image(element, image));
		});
		
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT0204E_IMAGE_NOT_REMOVABLE));
		transaction(()->{
		    service.removeImage(IMAGE_ID);
		});
		
	}
	
	@Test
	public void read_active_image_statistics() {
		
		transaction(() -> {
			ElementRole  role  = repository.execute(findRoleByName(ELEMENT_ROLE));
			ElementGroup group = repository.addIfAbsent(findElementGroupByName(GROUP_TYPE, 
																  						 GROUP_NAME), 
														() -> new ElementGroup(randomGroupId(), 
														 	  				   GROUP_TYPE, 
																		  	   GROUP_NAME));
			
			Platform platform = repository.addIfAbsent(findPlatformById(PLATFORM_ID),
													   ()->new Platform(PLATFORM_ID,
															   			PLATFORM_NAME,
															   			PLATFORM_CHIPSET));
			
			Element element = repository.addIfAbsent(findElementByName(ELEMENT_NAME), 
													 () -> new Element(group, 
															 		   role, 
															 		   platform,
															 		   randomElementId(), 
															 		   ELEMENT_NAME));

			Image image = new Image(IMAGE_ID,
									IMAGE_TYPE,
									IMAGE_NAME,
									asList(role),
									PLATFORM_CHIPSET,
									IMAGE_VERSION);
			image.setOrganization(IMAGE_ORG);
			repository.add(image);
			Element_Image ei = new Element_Image(element, image);
			ei.setImageInstallationState(ACTIVE);
			repository.add(ei);
		});
		
		transaction(()->{
			ImageStatistics stats = service.getImageStatistics(IMAGE_ID);
			assertEquals(stats.getImage(),service.getImage(IMAGE_ID));
			assertEquals(Integer.valueOf(1),stats.getActiveCount().get(GROUP_NAME));
			assertNull(stats.getCachedCount().get(GROUP_NAME));
		});
		
	}
	
	@Test
	public void read_cached_image_statistics() {
		
		transaction(() -> {
			ElementRole  role  = repository.execute(findRoleByName(ELEMENT_ROLE));
			ElementGroup group = repository.addIfAbsent(findElementGroupByName(GROUP_TYPE, GROUP_NAME), 
													    () -> new ElementGroup(randomGroupId(), 
													                           GROUP_TYPE, 
													                           GROUP_NAME));
			
			Platform platform = repository.addIfAbsent(findPlatformById(PLATFORM_ID),
													   ()->new Platform(PLATFORM_ID, 
															   			PLATFORM_NAME,
															   			PLATFORM_CHIPSET));
			
			Element element = repository.addIfAbsent(findElementByName(ELEMENT_NAME), 
													 () -> new Element(group, 
															 		   role,
															 		   platform,
															 		   randomElementId(), 
															 		   ELEMENT_NAME));

			Image image = new Image(IMAGE_ID,
									IMAGE_TYPE,
									IMAGE_NAME,
									asList(role),
									PLATFORM_CHIPSET,
									new Version(1,0,0));
			image.setOrganization("leitstand.io");
			repository.add(image);
			Element_Image ei = new Element_Image(element, image);
			ei.setImageInstallationState(CACHED);
			repository.add(ei);
		});
		
		transaction(()->{
			ImageStatistics stats = service.getImageStatistics(IMAGE_ID);
			assertEquals(stats.getImage(),service.getImage(IMAGE_ID));
			assertNull(stats.getActiveCount().get(GROUP_NAME));
			assertEquals(Integer.valueOf(1),stats.getCachedCount().get(GROUP_NAME));
		});
		
	}

}

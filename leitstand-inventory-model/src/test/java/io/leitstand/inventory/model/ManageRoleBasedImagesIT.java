/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.model.ElementRole.findRoleByName;
import static io.leitstand.inventory.service.ElementPlatformInfo.newPlatformInfo;
import static io.leitstand.inventory.service.ImageId.randomImageId;
import static io.leitstand.inventory.service.ImageInfo.newImageInfo;
import static io.leitstand.inventory.service.ImageState.CANDIDATE;
import static io.leitstand.inventory.service.ImageState.RELEASE;
import static io.leitstand.inventory.service.ImageState.REVOKED;
import static io.leitstand.inventory.service.ImageState.SUPERSEDED;
import static io.leitstand.inventory.service.ImageType.LXC;
import static io.leitstand.inventory.service.PlatformId.randomPlatformId;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;

import javax.enterprise.event.Event;

import org.junit.Before;
import org.junit.Test;

import io.leitstand.commons.db.DatabaseService;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.commons.tx.SubtransactionService;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ImageInfo;
import io.leitstand.inventory.service.ImageName;
import io.leitstand.inventory.service.ImageState;
import io.leitstand.inventory.service.Plane;
import io.leitstand.inventory.service.Version;

public class ManageRoleBasedImagesIT extends InventoryIT{
	
	
	
	private DefaultImageService service;
	private Repository repository;
	private ImageInfo ref;
	
	@Before
	public void initTestEnvironment() {
		repository = new Repository(getEntityManager());
		SubtransactionService tx = mock(SubtransactionService.class);
		Platform platform = new Platform(randomPlatformId(),
										 "unit-vendor_name", "unit-model_name");
		when(tx.run(any(), any())).thenReturn(platform);
		when(tx.run(any())).thenReturn(platform);

		service = new DefaultImageService(tx,
										  mock(PackageVersionService.class), 
										  repository,
										  mock(DatabaseService.class),
										  mock(Messages.class),
										  mock(Event.class));
		
		repository.addIfAbsent(findRoleByName(new ElementRoleName("unit-element_type")), 
							  ()-> new ElementRole(new ElementRoleName("unit-element_type"), Plane.DATA));
		
		ImageInfo image = newImageInfo()
						  .withImageId(randomImageId())
						  .withBuildDate(new Date())
						  .withElementRole(new ElementRoleName("unit-element_type"))
						  .withPlatform(newPlatformInfo()
								        .withVendorName("unit-vendor_name")
								  		.withModelName("unit-model_name"))
						  .withOrganization("net.rtbrick")
						  .withImageType(LXC)
						  .withImageName(ImageName.valueOf("JUNIT"))
						  .withImageVersion(new Version(1,0,0))
						  .build();
									  
		
		service.storeImage(image);
		transaction(()->{
			service.updateImageState(image.getImageId(), RELEASE);
			ref = image;
		});
		
	}
	
	@Test
	public void newly_added_image_is_candidate_image() {
		ImageInfo image = newImageInfo()
				  		 .withImageId(randomImageId())
				  		 .withImageState(CANDIDATE)
				  		 .withImageName(ImageName.valueOf("JUNIT"))
				  		 .withBuildDate(new Date())
				  		 .withElementRole(new ElementRoleName("unit-element_type"))
				  		 .withPlatform(newPlatformInfo()
				  				 	   .withVendorName("unit-vendor_name")
				  				 	   .withModelName("unit-model_name"))
				  		 .withOrganization("net.rtbrick")
				  		 .withImageType(LXC)
				  		 .withImageVersion(new Version(1,0,1))
				  		 .build();

		transaction(()->{
			service.storeImage(image);
		});
		transaction(()->{
			ImageInfo stored = service.getImage(image.getImageId());
			assertEquals(CANDIDATE,stored.getImageState());
		});
		
	}
	
	
	@Test
	public void make_candidate_to_default_and_supersed_existing_default() {
		ImageInfo image = newImageInfo()
		  		 		  .withImageId(randomImageId())
		  		 		  .withImageType(LXC)
		  		 		  .withImageName(ImageName.valueOf("JUNIT"))
		  		 		  .withImageState(CANDIDATE)
		  		 		  .withImageVersion(new Version(2,0,0))
		  		 		  .withBuildDate(new Date())
		  		 		  .withElementRole(new ElementRoleName("unit-element_type"))
		  		 		  .withPlatform(newPlatformInfo()
		  		 				  		.withVendorName("unit-vendor_name")
		  		 				  		.withModelName("unit-model_name"))
		  		 		  .withOrganization("net.rtbrick")
		  		 		  .build();
		transaction(()->{
			service.storeImage(image);
		});
		transaction(()->{
			service.getImage(image.getImageId());
		});
		
		transaction(()->{
			ImageInfo stored = service.getImage(image.getImageId());
			assertEquals(CANDIDATE,stored.getImageState());
			service.updateImageState(stored.getImageId(), RELEASE);
		});
		
		transaction(()->{
			ImageInfo stored = service.getImage(image.getImageId());
			assertEquals(RELEASE,stored.getImageState());
			ref = service.getImage(ref.getImageId());
			assertEquals(SUPERSEDED,ref.getImageState());
		});
		
	}
	
	@Test
	public void revoke_candidate_image_has_no_effect_on_default_image() {
		ImageInfo image = newImageInfo()
		  		 		  .withImageId(randomImageId())
		  		 		  .withBuildDate(new Date())
		  		 		  .withImageType(LXC)
		  		 		  .withImageName(ImageName.valueOf("JUNIT"))
		  		 		  .withImageVersion(new Version(1,0,1))
		  		 		  .withElementRole(new ElementRoleName("unit-element_type"))
		  		 		  .withPlatform(newPlatformInfo()
		  		 		 		 	    .withVendorName("unit-vendor_name")
		  		 				 	    .withModelName("unit-model_name"))
		  		 		  .withOrganization("net.rtbrick")
		  		 		  .build();
		transaction(()->{
			service.storeImage(image);
		});
		transaction(()->{
			service.updateImageState(image.getImageId(),REVOKED);
		});
		transaction(()->{
			ImageInfo stored = service.getImage(image.getImageId());
			assertEquals(ImageState.REVOKED,stored.getImageState());
			ref = service.getImage(ref.getImageId());
			assertEquals(ImageState.RELEASE,ref.getImageState());
		});
	}
	
	
	
	
}
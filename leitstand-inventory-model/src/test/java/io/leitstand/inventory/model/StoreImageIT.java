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

import static io.leitstand.inventory.model.ElementRole.findRoleByName;
import static io.leitstand.inventory.model.ImageInfoMother.BAR_200;
import static io.leitstand.inventory.model.ImageInfoMother.FOO_100;
import static io.leitstand.inventory.model.ImageInfoMother.FOO_101;
import static io.leitstand.inventory.model.ImageInfoMother.newLeafImage;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.Plane.DATA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.List;

import javax.enterprise.event.Event;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.leitstand.commons.db.DatabaseService;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ImageInfo;
import io.leitstand.inventory.service.ImageService;
import io.leitstand.inventory.service.PackageVersionInfo;
import io.leitstand.inventory.service.Version;

public class StoreImageIT extends InventoryIT{

	private static final ElementRoleName LEAF = elementRoleName("LEAF");
	private static final ElementRoleName SPINE = elementRoleName("SPINE");
	
	private ImageService service;
	private PackageVersionService packages;
	private ImageInfo image;
	
	@Before
	public void initTestEnvironment(){
		Repository repository = new Repository(getEntityManager());
		this.packages = new PackageVersionService(repository);
		
		service = new DefaultImageService(packages, 
		                                  mock(ElementGroupProvider.class),
										  repository,
										  mock(DatabaseService.class),
										  mock(Messages.class),
										  mock(Event.class));
		transaction(() -> {
			repository.addIfAbsent(findRoleByName(LEAF), () -> {
				return new ElementRole(LEAF,DATA);
			});

			repository.addIfAbsent(findRoleByName(SPINE), () -> {
				return new ElementRole(SPINE,DATA);
			});
		});
		
	}
	
	@Test
	public void create_new_container_image_and_new_package(){
		image = newLeafImage(new Version(1,0,0),FOO_101,BAR_200);
		transaction( () -> {
			boolean created = service.storeImage(image);
			assertTrue(created);
		});
	}
	
	@Test
	public void create_new_container_image_referring_to_existing_packages(){
		transaction(()->{
			packages.storePackageVersion(FOO_101);
			packages.storePackageVersion(BAR_200);
		});
		transaction(()->{
			image = newLeafImage(new Version(1,0,0),FOO_101,BAR_200);
			boolean created = service.storeImage(image);
			assertTrue(created);
		});
	}
	
	@Test
	public void create_new_container_image_adding_new_package_revision_to_existing_package(){
		transaction(() -> {
			packages.storePackageVersion(FOO_100);
		});
		transaction(()->{
			image = newLeafImage(new Version(1,0,0),FOO_101,BAR_200);
			boolean created = service.storeImage(image);
			assertTrue(created);
		});
	}
	
	@After
	public void verify_created_container_image(){
		transaction(()->{
			ImageInfo restored = service.getImage(image.getImageId());
			assertNotNull(restored);
			assertEquals(image.getImageId(),restored.getImageId());

			List<PackageVersionInfo> packages = image.getPackages();
			assertEquals("net.rtbrick",packages.get(0).getOrganization());
			assertEquals("foo",packages.get(0).getPackageName());
			assertEquals(new Version(1,0,1),packages.get(0).getPackageVersion());

			assertEquals("net.rtbrick",packages.get(1).getOrganization());
			assertEquals("bar",packages.get(1).getPackageName());
			assertEquals(new Version(2,0,0),packages.get(1).getPackageVersion());
		});
	
	}
	
}

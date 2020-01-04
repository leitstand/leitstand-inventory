/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.model.PackageVersionDataMother.initialVersion;
import static io.leitstand.inventory.model.PackageVersionDataMother.initialVersionPatch;
import static io.leitstand.inventory.model.PackageVersionDataMother.newMajorVersion;
import static io.leitstand.inventory.model.PackageVersionDataMother.newMinorVersion;
import static io.leitstand.inventory.service.ReasonCode.IVT0500E_PACKAGE_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0510E_PACKAGE_VERSION_NOT_FOUND;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.PackageInfo;
import io.leitstand.inventory.service.PackageVersionInfo;
import io.leitstand.inventory.service.Version;

public class PackageServiceIT extends InventoryIT{

	private static final String IT_PKG_ORG = "net.rtbrick";
	private static final String IT_PKG_EXT = "test";
	private Repository repository;
	private DefaultPackageService service;
	
	@Before
	public void initTestEnvironment(){
		repository = new Repository(getEntityManager());
		service = new DefaultPackageService(repository,getDatabase(),new PackageVersionService(repository));
	}
	
	@Test
	public void create_package_with_revision(){
		service.storePackageVersion(initialVersion(IT_PKG_ORG, "create-package",IT_PKG_EXT));
		transaction(()->{
			PackageInfo pkg = service.getPackage(IT_PKG_ORG,"create-package",IT_PKG_EXT);
			assertEquals(IT_PKG_ORG,pkg.getOrganization());
			assertEquals("create-package",pkg.getName());
			assertEquals(1,pkg.getVersions().size());
			assertEquals(new Version(1,0,0),pkg.getVersions().get(0).getRevision());
		});
	}
	
	@Test
	public void removal_of_last_revision_removes_entire_package(){
		PackageVersionInfo pkg = initialVersion(IT_PKG_ORG, "remove-pkg-with-last-revision",IT_PKG_EXT);
		transaction(()->{
			service.storePackageVersion(pkg);
		});
		transaction(()->{
			assertEquals(pkg, service.getPackageVersion(pkg.getOrganization(), 
														 pkg.getPackageName(), 
														 pkg.getPackageVersion()));
			service.removeRevision(pkg.getOrganization(), 
								   pkg.getPackageName(), 
								   pkg.getPackageVersion());
		});
		transaction(()->{
			try{
				service.getPackageVersion(pkg.getOrganization(), 
										   pkg.getPackageName(),
										   pkg.getPackageVersion());
				fail("EntityNotFoundException expected");
			} catch (EntityNotFoundException e){
				assertEquals(IVT0510E_PACKAGE_VERSION_NOT_FOUND,e.getReason());
			}
		});
		
		transaction(()->{
			try {
				service.getPackage(pkg.getOrganization(), 
								   pkg.getPackageName(),
								   pkg.getPackageExtension());
				fail("EntityNotFoundException expected");
			} catch (EntityNotFoundException e){
				assertEquals(IVT0500E_PACKAGE_NOT_FOUND,e.getReason());
			}
		});

	}
	
	@Test
	public void add_package_major_revision(){
		service.storePackageVersion(initialVersion(IT_PKG_ORG, "add-major",IT_PKG_EXT));
		transaction(()->{
			service.storePackageVersion(newMajorVersion(IT_PKG_ORG, "add-major",IT_PKG_EXT));
		});
		transaction(()->{
			PackageInfo pkg = service.getPackage(IT_PKG_ORG,"add-major",IT_PKG_EXT);
			assertEquals(IT_PKG_ORG,pkg.getOrganization());
			assertEquals("add-major",pkg.getName());
			assertEquals(2,pkg.getVersions().size());
			assertEquals(new Version(1,0,0),pkg.getVersions().get(0).getRevision());
			assertEquals(new Version(2,0,0),pkg.getVersions().get(1).getRevision());
		});
	}
	
	@Test
	public void add_package_minor_revision(){
		transaction(()->{
			service.storePackageVersion(initialVersion(IT_PKG_ORG, "add-minor",IT_PKG_EXT));
		});
		transaction(()->{
			service.storePackageVersion(newMinorVersion(IT_PKG_ORG, "add-minor",IT_PKG_EXT));
		});
		transaction(()->{
			PackageInfo pkg = service.getPackage(IT_PKG_ORG,"add-minor",IT_PKG_EXT);
			assertEquals(IT_PKG_ORG,pkg.getOrganization());
			assertEquals("add-minor",pkg.getName());
			assertEquals(2,pkg.getVersions().size());
			assertEquals(new Version(1,0,0),pkg.getVersions().get(0).getRevision());
			assertEquals(new Version(1,1,0),pkg.getVersions().get(1).getRevision());
		});
	}
	
	@Test
	public void add_package_patch_revision(){
		transaction(()->{
			service.storePackageVersion(initialVersion(IT_PKG_ORG, "add-patch",IT_PKG_EXT));
		});
		transaction(()->{
			service.storePackageVersion(initialVersionPatch(IT_PKG_ORG, "add-patch", IT_PKG_EXT));
		});
		transaction(()->{
			PackageInfo pkg = service.getPackage(IT_PKG_ORG,"add-patch",IT_PKG_EXT);
			assertEquals(IT_PKG_ORG,pkg.getOrganization());
			assertEquals("add-patch",pkg.getName());
			assertEquals(2,pkg.getVersions().size());
			assertEquals(new Version(1,0,0),pkg.getVersions().get(0).getRevision());
			assertEquals(new Version(1,0,1),pkg.getVersions().get(1).getRevision());
		});
	}
	
	@Test
	public void can_repeatedly_store_same_major_revision(){
		PackageVersionInfo info =initialVersion(IT_PKG_ORG, "update-major" ,IT_PKG_EXT);
		transaction(()->{
			service.storePackageVersion(info);
		});
		transaction(()->{
			service.storePackageVersion(info);
		});
		transaction(()->{
			assertEquals(info,service.getPackageVersion(info.getOrganization(), 
														 info.getPackageName(), 
														 info.getPackageVersion()));
		});
	}
	
	@Test
	public void can_repeatedly_store_same_minor_revision(){
		transaction(()->{
			service.storePackageVersion(initialVersion(IT_PKG_ORG, "update-minor",IT_PKG_EXT));
		});
		PackageVersionInfo minor = newMinorVersion(IT_PKG_ORG, "update-minor",IT_PKG_EXT);
		transaction(()->{
			service.storePackageVersion(minor);
		});
		transaction(()->{
			service.storePackageVersion(newMinorVersion(IT_PKG_ORG, "update-minor",IT_PKG_EXT));
			assertEquals(minor,service.getPackageVersion(minor.getOrganization(), 
														  minor.getPackageName(), 
														  minor.getPackageVersion()));
		});
	}
	
	@Test
	public void can_repeatedly_store_same_patch_revision(){
		transaction(()->{
			service.storePackageVersion(initialVersion(IT_PKG_ORG, "update-patch",IT_PKG_EXT));
		});
		PackageVersionInfo patch = initialVersionPatch(IT_PKG_ORG, "update-patch",IT_PKG_EXT);
		transaction(()->{
			service.storePackageVersion(patch);
		});
		transaction(()->{
			service.storePackageVersion(initialVersionPatch(IT_PKG_ORG, "update-patch",IT_PKG_EXT));
			assertEquals(patch,service.getPackageVersion(patch.getOrganization(), 
														  patch.getPackageName(), 
														  patch.getPackageVersion()));
		});
	}
	
	@Test
	public void remove_revision(){
		transaction(()->{
			service.storePackageVersion(initialVersion(IT_PKG_ORG, "remove-revision" ,IT_PKG_EXT));
		});
		transaction(()->{
			service.storePackageVersion(newMinorVersion(IT_PKG_ORG, "remove-revision",IT_PKG_EXT));
		});
		transaction(()->{
			PackageInfo pkg = service.getPackage(IT_PKG_ORG,"remove-revision",IT_PKG_EXT);
			assertEquals(IT_PKG_ORG,pkg.getOrganization());
			assertEquals("remove-revision",pkg.getName());
			assertEquals(2,pkg.getVersions().size());
			assertEquals(new Version(1,0,0),pkg.getVersions().get(0).getRevision());
			assertEquals(new Version(1,1,0),pkg.getVersions().get(1).getRevision());
			service.removeRevision(IT_PKG_ORG, "remove-revision",new Version(1,1,0));
			commitTransaction();
			beginTransaction();
			pkg = service.getPackage(IT_PKG_ORG,"remove-revision",IT_PKG_EXT);
			assertEquals(IT_PKG_ORG,pkg.getOrganization());
			assertEquals("remove-revision",pkg.getName());
			assertEquals(1,pkg.getVersions().size());
			assertEquals(new Version(1,0,0),pkg.getVersions().get(0).getRevision());
		});
	}
	

	
	
}

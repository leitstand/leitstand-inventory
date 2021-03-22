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
import static io.leitstand.inventory.model.Package.findByName;
import static io.leitstand.inventory.model.Package_Version.findPackageVersion;
import static io.leitstand.inventory.service.PackageVersions.newPackageVersions;
import static io.leitstand.inventory.service.PackageVersionRef.newPackageVersionRef;
import static io.leitstand.inventory.service.PackageVersionInfo.newPackageVersionInfo;
import static io.leitstand.inventory.service.QualifiedPackageName.newQualifiedPackageName;
import static io.leitstand.inventory.service.ReasonCode.IVT0500E_PACKAGE_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0510E_PACKAGE_VERSION_NOT_FOUND;
import static java.util.stream.Collectors.toMap;
import static javax.persistence.LockModeType.OPTIMISTIC;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.db.DatabaseService;
import io.leitstand.commons.model.Repository;
import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.PackageVersions;
import io.leitstand.inventory.service.PackageService;
import io.leitstand.inventory.service.PackageVersionRef;
import io.leitstand.inventory.service.PackageVersionInfo;
import io.leitstand.inventory.service.QualifiedPackageName;
import io.leitstand.inventory.service.Version;
@Service 
public class DefaultPackageService implements PackageService {

	static PackageVersionInfo packageVersionInfo(Package_Version revision) {
		return newPackageVersionInfo()
			   .withOrganization(revision.getOrganization())
			   .withPackageName(revision.getPackageName())
			   .withPackageExtension(revision.getPackageExtension())
			   .withPackageVersion(revision.getPackageVersion())
			   .withBuildId(revision.getBuildId())
			   .withBuildDate(revision.getBuildDate())
			   .withChecksums(revision.getChecksums()
					   				  .stream()
					   				  .collect(toMap(c -> c.getAlgorithm().name(),
					   						 	     Checksum::getValue)))
			   .build();
	}
	
	@Inject
	@Inventory
	private Repository repository;
	
	@Inject
	@Inventory
	private DatabaseService database;
	
	@Inject
	private PackageVersionService service;
	
	public DefaultPackageService(){
		// Tool constructor
	}
	
	public DefaultPackageService(	Repository repository,
									DatabaseService database,
									PackageVersionService service) {
		this.repository = repository;
		this.database = database;
		this.service  = service;
	}

	@Override
	public List<QualifiedPackageName> getPackages() {
		return database.executeQuery(prepare("SELECT org, name FROM INVENTORY.PACKAGE"), 
						 			 rs -> newQualifiedPackageName()
						 			 	   .withOrganization(rs.getString(1))
						 			 	   .withName(rs.getString(2))
						 			 	   .build());
	}

	@Override
	public PackageVersions getPackage(String org, String name, String ext) {
		Package pkg = repository.execute(findByName(org, 
										            name,
										            ext,
										            OPTIMISTIC));
		if(pkg == null){
			throw new EntityNotFoundException(IVT0500E_PACKAGE_NOT_FOUND,org+"-"+name+"."+ext); 
		}
		
		List<PackageVersionRef> versions = new LinkedList<>();
		for(Package_Version version : pkg.getVersions()){
			versions.add(newPackageVersionRef()
						 .withOrganization(version.getOrganization())
						 .withPackageName(version.getPackageName())
						 .withPackageVersion(version.getPackageVersion())
						 .build());
		}
		
		return newPackageVersions()
			   .withOrganization(pkg.getOrganization())
			   .withPackageName(pkg.getPackageName())
			   .withVersions(versions)
			   .build();
		
	}

	@Override
	public PackageVersionInfo getPackageVersion(String org, 
												String name, 
												Version version) {
		Package_Version pkg = repository.execute(findPackageVersion(org, 
																   name, 
																   version));
		if(pkg == null){
			throw new EntityNotFoundException(IVT0510E_PACKAGE_VERSION_NOT_FOUND, org+"."+name+"-"+version);
		}
		return packageVersionInfo(pkg);
	}

	@Override
	public void storePackageVersion(PackageVersionInfo info) {
		service.storePackageVersion(info);
	}

	void removeRevision(String org, String name, Version rev) {
		service.removePackageVersion(org, name, rev);
	}

}

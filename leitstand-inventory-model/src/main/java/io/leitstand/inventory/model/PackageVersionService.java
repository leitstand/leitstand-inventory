/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.model.Checksum.newChecksum;
import static io.leitstand.inventory.model.Package.findByName;
import static io.leitstand.inventory.model.PackageVersion.findPackageVersion;
import static java.util.stream.Collectors.toList;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.LockModeType;

import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.PackageVersionInfo;
import io.leitstand.inventory.service.Version;

@Dependent
class PackageVersionService{

	private Repository repository;

	@Inject
	protected PackageVersionService(@Inventory Repository repository){
		this.repository = repository;
	}
	
	private Package loadPackageForUpdate(String org, String name, String ext){
		Package pkg = repository.execute(findByName(org,name,ext, LockModeType.OPTIMISTIC_FORCE_INCREMENT));
		if(pkg != null){
			return pkg;
		}
		pkg = new Package(org,name,ext);
		repository.add(pkg);
		return pkg;
	}

	public PackageVersion getPackageVersion(String org, String name, Version rev){
		PackageVersion revision = repository.execute(findPackageVersion(org, name, rev));
		if(revision != null){
			return revision;
		}
		return null;
	}

	public PackageVersion storePackageVersion(PackageVersionInfo info){
		String org = info.getOrganization();
		String name = info.getPackageName();
		String ext = info.getPackageExtension();
		Version rev = info.getPackageVersion();
		
		PackageVersion revision = getPackageVersion(org, name, rev);
		if(revision == null){
			Package pkg = loadPackageForUpdate(org,name,ext);
			revision = pkg.newVersion(rev);
		}
		revision.setBuildDate(info.getBuildDate());
		revision.setChecksums(info.getChecksums()
								  .entrySet()
								  .stream()
								  .map(c -> newChecksum()
										  	.withAlgorithm(Checksum.Algorithm.valueOf(c.getKey()))
										  	.withValue(c.getValue()).build())
								  			.collect(toList()));
		return revision;
	}
	
	public void removePackageVersion(String org, String name, Version rev){
		PackageVersion revision = repository.execute(findPackageVersion(org, name, rev));
		if(revision == null){
			return;
		}
		Package pkg = revision.getPackage();
		pkg.removeVersion(rev);
		if(pkg.getVersions().isEmpty()){
			repository.remove(pkg);
		}
		
	}
	
}

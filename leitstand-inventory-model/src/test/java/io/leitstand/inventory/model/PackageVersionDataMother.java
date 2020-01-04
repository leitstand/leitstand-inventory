/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.PackageVersionInfo.newPackageVersionInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.leitstand.inventory.service.PackageVersionInfo;
import io.leitstand.inventory.service.Version;

public class PackageVersionDataMother {

	private static Map<String,String> checksum(String s){
		Map<String,String> checksums = new HashMap<>();
		checksums.put("SHA256", s);
		return checksums;
	}
	
	private static final Date BUILD_DATE = new Date();
	
	public static PackageVersionInfo initialVersion(String org, String name, String ext){
		return newPackageVersionInfo()
			   .withOrganization(org)
			   .withPackageName(name)
			   .withPackageExtension(ext)
			   .withBuildDate(BUILD_DATE)
			   .withPackageVersion(new Version(1,0,0))
			   .withChecksums(checksum("junit-sum-1.0.0"))
			   .build();
	}
	
	
	public static PackageVersionInfo newMajorVersion(String org, String name, String ext){
		return newPackageVersionInfo()
			   .withOrganization(org)
			   .withPackageName(name)
			   .withPackageExtension(ext)
			   .withBuildDate(BUILD_DATE)
			   .withPackageVersion(new Version(2,0,0))
			   .withChecksums(checksum("junit-sum-2.0.0"))
			   .build();
	}
	
	public static PackageVersionInfo newMinorVersion(String org, String name, String ext){
		return newPackageVersionInfo()
			   .withOrganization(org)
			   .withPackageName(name)
			   .withPackageExtension(ext)
			   .withBuildDate(BUILD_DATE)
			   .withPackageVersion(new Version(1,1,0))
			   .withChecksums(checksum("junit-sum-1.1.0"))
			   .build();
	}

	public static PackageVersionInfo initialVersionPatch(String org, String name, String ext){
		return newPackageVersionInfo()
			   .withOrganization(org)
			   .withPackageName(name)
			   .withPackageExtension(ext)
			   .withBuildDate(BUILD_DATE)
			   .withPackageVersion(new Version(1,0,1))
			   .withChecksums(checksum("junit-sum-1.0.1"))
			   .build();
	}

	
}

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

import static io.leitstand.commons.model.ByteArrayUtil.encodeBase36String;
import static io.leitstand.inventory.service.ImageInfo.newImageInfo;
import static io.leitstand.inventory.service.PackageVersionInfo.newPackageVersionInfo;
import static io.leitstand.inventory.service.PlatformChipsetName.platformChipsetName;
import static io.leitstand.security.crypto.SecureHashes.sha256;
import static java.util.Arrays.asList;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ImageId;
import io.leitstand.inventory.service.ImageInfo;
import io.leitstand.inventory.service.PackageVersionInfo;
import io.leitstand.inventory.service.PlatformChipsetName;
import io.leitstand.inventory.service.Version;

public final class ImageInfoMother {
	
	private static final PlatformChipsetName PLATFORM_CHIPSET = platformChipsetName("unittest-chipset");
	
	private static final String DUMMY_MD5 = "DUMMY";
	
	private static Map<String,String> checksum(String s){
		Map<String,String> checksums = new HashMap<>();
		checksums.put("SHA256", s);
		return checksums;
	}
	
	
	private static Map<String,String> md5(String s){
		Map<String,String> checksums = new HashMap<>();
		checksums.put("MD5", s);
		return checksums;
	}
	
	public static final PackageVersionInfo FOO_100 = newPackageVersionInfo()
													 .withOrganization("net.rtbrick")
													 .withPackageName("foo")
													 .withPackageVersion(new Version(1,0,0))
													 .withBuildDate(new Date())
													 .withChecksums(checksum(encodeBase36String(sha256().hash("netrtrbrickfoo100"))))
													 .withPackageExtension("so")
													 .build();
	
	public static final PackageVersionInfo FOO_101 = newPackageVersionInfo()
													 .withOrganization("net.rtbrick")
													 .withPackageName("foo")
													 .withPackageVersion(new Version(1,0,1))
													 .withBuildDate(new Date())
													 .withChecksums(checksum(encodeBase36String(sha256().hash("netrtrbrickfoo101"))))
													 .withPackageExtension("so")
													 .build();
	
	public static final PackageVersionInfo BAR_100 = newPackageVersionInfo()
													 .withOrganization("net.rtbrick")
			  										 .withPackageName("bar")
			  										 .withPackageVersion(new Version(1,0,0))
			  										 .withBuildDate(new Date())
			  										 .withChecksums(checksum(encodeBase36String(sha256().hash("netrtrbrickbar100"))))
			  										 .withPackageExtension("so")
			  										 .build();

	public static final PackageVersionInfo BAR_200 = newPackageVersionInfo()
													 .withOrganization("net.rtbrick")
													 .withPackageName("bar")
													 .withPackageVersion(new Version(2,0,0))
													 .withBuildDate(new Date())
													 .withChecksums(checksum(encodeBase36String(sha256().hash("netrtrbrickfoo100"))))
													 .withPackageExtension("so")
													 .build();
	

	public static ImageInfo newSpineImage(Version revision, PackageVersionInfo... packages){
		ImageId id = new ImageId(UUID.randomUUID().toString());
		return newImageInfo()
			   .withElementRoles(new ElementRoleName("SPINE"))
			   .withBuildDate(new Date())
			   .withImageVersion(revision)
			   .withImageId(id)
			   .withChecksums(md5(DUMMY_MD5))
			   .withPackages(asList(packages))
			   .withPlatformChipset(PLATFORM_CHIPSET)
			   .withExtension("tar.gz")
			   .build();
		
	}
	

	public static ImageInfo newLeafImage(Version revision, PackageVersionInfo... packages){
		ImageId id = new ImageId(UUID.randomUUID().toString());
		return newImageInfo()
			   .withElementRoles(new ElementRoleName("LEAF"))
			   .withBuildDate(new Date())
			   .withImageVersion(revision)
			   .withImageId(id)
			   .withChecksums(md5(DUMMY_MD5))
			   .withPackages(asList(packages))
			   .withPlatformChipset(PLATFORM_CHIPSET)
			   .withExtension("tar.gz")
			   .build();
		
	}
	
	private ImageInfoMother(){
		// No instances allowed
	}
}

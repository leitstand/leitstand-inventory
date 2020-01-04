/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import java.util.List;

public interface PackageService {
	List<QualifiedPackageName> getPackages();
	PackageInfo getPackage(String org, String name, String ext);
	PackageVersionInfo getPackageVersion(String org, String name, Version rev);
	void storePackageVersion(PackageVersionInfo info);
}

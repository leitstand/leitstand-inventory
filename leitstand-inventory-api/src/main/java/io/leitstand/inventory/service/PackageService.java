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
package io.leitstand.inventory.service;

import java.util.List;

/**
 * A service for managing packages shipped with an image.
 */
public interface PackageService {

    /**
     * Returns a list of known packages.
     * @return a list of known packages.
     */
    List<QualifiedPackageName> getPackages();
    
    /**
     * Returns the version list of a package.
     * @param organization the organization that published the package.
     * @param packageName the package name.
     * @param ext the package file extension.
     * @return the versions of the package.
     * @throws EntityNotFoundException if the package does not exist.
     */
	PackageVersions getPackage(String organization, String packageName, String ext);
	
	/**
	 * Returns the package metadata.
	 * @param organization the organization that published the package.
	 * @param packageName the package name.
	 * @param packageVersion the package version.
	 * @return the package metadata.
	 * @throws EntityNotFoundException if the package does not exist.
	 */
	PackageVersionInfo getPackageVersion(String organization, String packageName, Version packageVersion);
	
	/**
	 * Stores package version metadata. 
	 * @param info the package version metadata.
	 */
	void storePackageVersion(PackageVersionInfo info);
}

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

import javax.validation.Valid;

/**
 * A platform management service.
 * <p>
 * The <code>PlatformService</code> allows querying, storing and removing platforms.
 */
public interface PlatformService {

    /**
     * Lists all platforms stored in the inventory ordered by the platform name.
     * Returns an empty list if no platforms are stored.
     * @return all platforms stored in the inventory.
     */
    List<PlatformSettings> getPlatforms();
    
    /**
     * Queries platforms by their name.
     * Returns all matching platforms ordered by name.
     * Returns an empty list if no matching platforms exist.
     * @param filter a regular expression to filter platforms by name
     * @return all matchings platforms.
     */
	List<PlatformSettings> getPlatforms(String filter);
	
	/**
	 * Returns the platform settings.
	 * @param platformId the platform ID.
	 * @return the platform settings.
	 * @throws EntityNotFoundException if the platform does not exist.
	 */
	PlatformSettings getPlatform(PlatformId platformId);
	
	/**
	 * Returns the platform settings.
	 * @param platformName the platform name.
	 * @return the platform settings.
	 * @throws EntityNotFoundException if the platform does not exist.
	 */
	PlatformSettings getPlatform(PlatformName platformName);
	
	/**
	 * Stores the platform settings. 
	 * Returns <code>true</code> if a new platform is added, <code>false</code> otherwise.
	 * @param settings the platform settings.
	 * @return <code>true</code> if a new platform is added, <code>false</code> otherwise.
	 */
	boolean storePlatform(PlatformSettings settings);
	
	/**
	 * Removes the platform.
	 * Does nothing if the platform does not exist.
	 * @param platformId the platform ID.
	 */
	void removePlatform(@Valid PlatformId platformId);

	/**
     * Removes the platform.
     * Does nothing if the platform does not exist.
     * @param platformId the platform name.
     */
	void removePlatform(@Valid PlatformName platformName);
	
}

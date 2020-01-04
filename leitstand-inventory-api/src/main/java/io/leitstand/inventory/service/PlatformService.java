/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import java.util.List;

import javax.validation.Valid;

public interface PlatformService {

	List<PlatformSettings> getPlatforms();
	List<PlatformSettings> getPlatforms(String vendor);
	PlatformSettings getPlatform(String vendor, String model);
	boolean storePlatform(PlatformSettings settings);
	PlatformSettings getPlatform(PlatformId platformId);
	void removePlatform(@Valid PlatformId platformId);
	
}

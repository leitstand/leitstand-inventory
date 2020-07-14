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

import static io.leitstand.inventory.model.Platform.findPlatformById;
import static io.leitstand.inventory.model.Platform.findPlatformByName;
import static io.leitstand.inventory.service.ReasonCode.IVT0900E_PLATFORM_NOT_FOUND;
import static java.lang.String.format;

import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.PlatformChipsetName;
import io.leitstand.inventory.service.PlatformId;
import io.leitstand.inventory.service.PlatformName;

@Dependent
public class PlatformProvider {
	
	private static final Logger LOG = Logger.getLogger(PlatformProvider.class.getName());

	private Repository repository;
	
	protected PlatformProvider() {
		// CDI
	}
	
	@Inject
	protected PlatformProvider(@Inventory Repository repository) {
		this.repository = repository;
	}
	
	public Platform tryFetchPlatform(PlatformId platformId) {
		return repository.execute(findPlatformById(platformId));
	}
	
	public Platform tryFetchPlatform(PlatformName platformName) {
		return repository.execute(findPlatformByName(platformName));
	}
	
	
	public Platform fetchPlatform(PlatformId id) {
		Platform platform = tryFetchPlatform(id);
		if(platform == null) {
			LOG.fine(() -> format("%s: Platform %s does not exist!", 
							IVT0900E_PLATFORM_NOT_FOUND.getReasonCode(),
							id));
			throw new EntityNotFoundException(IVT0900E_PLATFORM_NOT_FOUND,
											  id);
		}
		return platform;
	}
	
	public Platform fetchPlatform(PlatformName name) {
		Platform platform = tryFetchPlatform(name);
		if(platform == null) {
			LOG.fine(() -> format("%s: Platform %s does not exist!", 
							IVT0900E_PLATFORM_NOT_FOUND.getReasonCode(),
							name));
			throw new EntityNotFoundException(IVT0900E_PLATFORM_NOT_FOUND,
											  name);
		}
		return platform;
	}
	
	public Platform findOrCreatePlatform(PlatformId platformId, 
										 PlatformName platformName,
										 PlatformChipsetName platformChipset) {
		if(platformId != null) {
			Platform platform = tryFetchPlatform(platformId);
			if(platform != null) {
				return platform;
			}
		}
		if(platformName != null) {
			if(platformId != null) {
				// Platform does not exist. Create new platform.
				Platform platform = new Platform(platformId,
												 platformName,
												 platformChipset);
				repository.add(platform);
				return platform;
			}
			return tryFetchPlatform(platformName);
		}
		return null;
	}
	
}

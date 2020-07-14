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

import static io.leitstand.commons.messages.MessageFactory.createMessage;
import static io.leitstand.inventory.model.Platform.countElements;
import static io.leitstand.inventory.model.Platform.findAll;
import static io.leitstand.inventory.model.Platform.findPlatformById;
import static io.leitstand.inventory.model.Platform.findPlatformByName;
import static io.leitstand.inventory.service.PlatformSettings.newPlatformSettings;
import static io.leitstand.inventory.service.ReasonCode.IVT0900E_PLATFORM_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0901I_PLATFORM_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0902I_PLATFORM_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0903E_PLATFORM_NOT_REMOVABLE;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.PlatformId;
import io.leitstand.inventory.service.PlatformName;
import io.leitstand.inventory.service.PlatformService;
import io.leitstand.inventory.service.PlatformSettings;

@Service
public class DefaultPlatformService implements PlatformService{
	
	private static final Logger LOG = Logger.getLogger(DefaultPlatformService.class.getName());

	private Repository repository;
	
	private Messages messages;
	
	protected DefaultPlatformService() {
		// CDI
	}
	
	@Inject
	protected DefaultPlatformService(@Inventory Repository repository, Messages messages) {
		this.repository = repository;
		this.messages = messages;
	}
	
	@Override
	public List<PlatformSettings> getPlatforms(){
		return getPlatforms(null);
	}
	
	@Override
	public List<PlatformSettings> getPlatforms(String filter) {
		return repository.execute(findAll(filter))
				 		 .stream()
				 		 .map(p -> platformSettings(p))
				 		 .collect(toList());
		
	}
	
	private PlatformSettings platformSettings(Platform platform) {
		return newPlatformSettings()
			   .withPlatformId(platform.getPlatformId())
			   .withPlatformName(platform.getPlatformName())
			   .withPlatformChipset(platform.getChipset())
			   .withVendorName(platform.getVendor())
			   .withModelName(platform.getModel())
			   .withDescription(platform.getDescription())
			   .withRackUnits(platform.getHeight())
			   .build();
	}

	@Override
	public PlatformSettings getPlatform(PlatformName name) {
		Platform platform = repository.execute(findPlatformByName(name));
		if(platform == null) {
			LOG.fine(() -> format("%s: Platform %s not found!",
								  IVT0900E_PLATFORM_NOT_FOUND.getReasonCode(),
								  name));
			throw new EntityNotFoundException(IVT0900E_PLATFORM_NOT_FOUND, name);
		}
		return platformSettings(platform);
	}
	
	@Override
	public PlatformSettings getPlatform(PlatformId platformId) {
		Platform platform = repository.execute(findPlatformById(platformId));
		if(platform == null) {
			LOG.fine(() -> format("%s: Platform %s not found!",
								  IVT0900E_PLATFORM_NOT_FOUND.getReasonCode(),
								  platformId));
			throw new EntityNotFoundException(IVT0900E_PLATFORM_NOT_FOUND, platformId);
		}
		return platformSettings(platform);
	}

	@Override
	public boolean storePlatform(PlatformSettings settings) {
		try {
			boolean created = false;
			Platform platform = repository.execute(findPlatformById(settings.getPlatformId()));
			if(platform == null) {
				platform = new Platform(settings.getPlatformId(),
										settings.getPlatformName(),
										settings.getPlatformChipset());
				repository.add(platform);
				created = true;
			}
			platform.setPlatformName(settings.getPlatformName());
			platform.setChipset(settings.getPlatformChipset());
			platform.setVendor(settings.getVendorName());
			platform.setModel(settings.getModelName());
			platform.setDescription(settings.getDescription());
			platform.setRackUnits(settings.getRackUnits());
			return created;
		} finally {
			messages.add(createMessage(IVT0901I_PLATFORM_STORED, 
									   settings.getPlatformName()));
			
			LOG.fine(() -> format("%s: Platform %s stored (%s)",
								  IVT0901I_PLATFORM_STORED.getReasonCode(),
								  settings.getPlatformName(),
								  settings.getPlatformId()));
			
		}
	}
	

	@Override
	public void removePlatform(PlatformId platformId) {
		Platform platform = repository.execute(findPlatformById(platformId));
		removePlatform(platform);
		
	}

	@Override
	public void removePlatform(PlatformName platformName) {
		Platform platform = repository.execute(findPlatformByName(platformName));
		removePlatform(platform);
	}
	
	private void removePlatform(Platform platform) {
		if(platform != null) {
			long count = repository.execute(countElements(platform));
			if(count == 0) {
				repository.remove(platform);
				messages.add(createMessage(IVT0902I_PLATFORM_REMOVED, 
										   platform.getPlatformName()));
				LOG.fine(()->format("%s: Platform %s removed (%s)", 
									IVT0902I_PLATFORM_REMOVED.getReasonCode(),
									platform.getPlatformName(),	
									platform.getPlatformId()));
				return;
			}
			
			LOG.fine(()->format("%s: Platform %s cannot be removed (%s) because of %d existing elements.", 
							    IVT0903E_PLATFORM_NOT_REMOVABLE.getReasonCode(),
							    platform.getPlatformName(),
							    platform.getPlatformId(),
							    count));
			
			throw new ConflictException(IVT0903E_PLATFORM_NOT_REMOVABLE,
										platform.getPlatformName(), 
										count);
		}
	}

}

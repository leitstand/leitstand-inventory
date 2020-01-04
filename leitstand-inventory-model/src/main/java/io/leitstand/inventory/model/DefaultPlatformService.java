/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.commons.messages.MessageFactory.createMessage;
import static io.leitstand.inventory.model.Platform.countElements;
import static io.leitstand.inventory.model.Platform.findAll;
import static io.leitstand.inventory.model.Platform.findByModel;
import static io.leitstand.inventory.model.Platform.findByPlatformId;
import static io.leitstand.inventory.model.Platform.findByVendor;
import static io.leitstand.inventory.service.PlatformSettings.newPlatformSettings;
import static io.leitstand.inventory.service.ReasonCode.IVT0900E_PLATFORM_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0901I_PLATFORM_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0902I_PLATFORM_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0903E_PLATFORM_NOT_REMOVABLE;
import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.PlatformId;
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
	public List<PlatformSettings> getPlatforms() {
		List<PlatformSettings> platforms = new LinkedList<>();
		for(Platform platform : repository.execute(findAll())){
			platforms.add(platformSettings(platform));	
		}
		return unmodifiableList(platforms);
	}

	@Override
	public List<PlatformSettings> getPlatforms(String vendor) {
		List<PlatformSettings> platforms = new LinkedList<>();
		for(Platform platform : repository.execute(findByVendor(vendor))){
			platforms.add(platformSettings(platform));	
		}
		return unmodifiableList(platforms);
	}

	private PlatformSettings platformSettings(Platform platform) {
		return newPlatformSettings()
			   .withPlatformId(platform.getPlatformId())
			   .withVendorName(platform.getVendor())
			   .withModelName(platform.getModel())
			   .withDescription(platform.getDescription())
			   .withHalfRackSize(platform.isHalfRackSize())
			   .withRackUnits(platform.getHeight())
			   .build();
	}

	@Override
	public PlatformSettings getPlatform(String vendor, String model) {
		Platform platform = repository.execute(findByModel(vendor, model));
		if(platform == null) {
			LOG.fine(() -> format("%s: Platform %s %s not found!",
								  IVT0900E_PLATFORM_NOT_FOUND.getReasonCode(),
								  vendor,
								  model));
			throw new EntityNotFoundException(IVT0900E_PLATFORM_NOT_FOUND, vendor,model);
		}
		return platformSettings(platform);
	}
	
	@Override
	public PlatformSettings getPlatform(PlatformId platformId) {
		Platform platform = repository.execute(findByPlatformId(platformId));
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
			Platform platform = repository.execute(findByPlatformId(settings.getPlatformId()));
			if(platform != null) {
				platform.setVendor(settings.getVendorName());
				platform.setModel(settings.getModelName());
				platform.setHalfRack(settings.isHalfRackSize());
				platform.setRackUnits(settings.getRackUnits());
				platform.setDescription(settings.getDescription());
				return false;
			}
			
			platform = new Platform(settings.getPlatformId(),
									settings.getVendorName(), 
									settings.getModelName());
			platform.setDescription(settings.getDescription());
			platform.setHalfRack(settings.isHalfRackSize());
			platform.setRackUnits(settings.getRackUnits());
			repository.add(platform);
			return true;
		} finally {
			messages.add(createMessage(IVT0901I_PLATFORM_STORED, 
									   settings.getVendorName(),
									   settings.getModelName()));
			
			LOG.fine(() -> format("%s: Platform %s %s stored (%s)",
								  IVT0901I_PLATFORM_STORED.getReasonCode(),
								  settings.getVendorName(),
								  settings.getModelName(),
								  settings.getPlatformId()));
			
		}
	}
	

	@Override
	public void removePlatform(PlatformId platformId) {
		Platform platform = repository.execute(findByPlatformId(platformId));
		if(platform != null) {
			long count = repository.execute(countElements(platform));
			if(count == 0) {
				repository.remove(platform);
				messages.add(createMessage(IVT0902I_PLATFORM_REMOVED, 
										   platform.getVendor(),
										   platform.getModel()));
				LOG.fine(()->format("%s: Platform %s %s removed (%s)", 
									IVT0902I_PLATFORM_REMOVED.getReasonCode(),
									platform.getVendor(),
									platform.getModel(),
									platform.getPlatformId()));
				return;
			}
			
			LOG.fine(()->format("%s: Platform %s %s cannot be removed (%s) because of %d existing elements.", 
							    IVT0903E_PLATFORM_NOT_REMOVABLE.getReasonCode(),
							    platform.getVendor(),
							    platform.getModel(),
							    platform.getPlatformId(),
							    count));
			
			throw new ConflictException(IVT0903E_PLATFORM_NOT_REMOVABLE,
										platform.getVendor(), 
										platform.getModel(), 
										count);
		}
		
	}

}

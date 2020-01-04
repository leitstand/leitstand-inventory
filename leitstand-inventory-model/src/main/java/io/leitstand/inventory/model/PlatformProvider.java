/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.model.Platform.findByVendor;
import static io.leitstand.inventory.service.ReasonCode.IVT0900E_PLATFORM_NOT_FOUND;
import static java.lang.String.format;

import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementPlatformInfo;
import io.leitstand.inventory.service.PlatformId;

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
	
	public Platform tryFetchPlatform(ElementPlatformInfo info) {
		return repository.execute(findByVendor(info));
	}
	
	public Platform fetchPlatform(ElementPlatformInfo info) {
		Platform platform = tryFetchPlatform(info);
		if(platform == null) {
			LOG.fine(() -> format("%s: Platform %s %s does not exist!", 
							IVT0900E_PLATFORM_NOT_FOUND.getReasonCode(),
							info.getVendorName(),
							info.getModelName()));
			throw new EntityNotFoundException(IVT0900E_PLATFORM_NOT_FOUND,
											  format("%s %s",
													 info.getVendorName(),
													 info.getModelName()));
		}
		return platform;
	}
	
	public Platform fetchPlatform(PlatformId platformId) {
		Platform platform = repository.execute(Platform.findByPlatformId(platformId));
		if(platform == null) {
			LOG.fine(() -> format("%s: Platform %s does not exist!", 
							IVT0900E_PLATFORM_NOT_FOUND.getReasonCode(),
							platformId));
			throw new EntityNotFoundException(IVT0900E_PLATFORM_NOT_FOUND,
											  platformId);
		}
		return platform;
	}
	
}

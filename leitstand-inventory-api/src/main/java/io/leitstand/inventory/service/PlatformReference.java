package io.leitstand.inventory.service;

import io.leitstand.commons.model.ValueObject;

public class PlatformReference extends ValueObject {

	
	private PlatformId platformId;
	private PlatformName platformName;
	
	public PlatformReference(PlatformId platformId, 
							 PlatformName platformName) {
		this.platformId = platformId;
		this.platformName = platformName;
	}
	
	
	public PlatformId getPlatformId() {
		return platformId;
	}
	
	public PlatformName getPlatformName() {
		return platformName;
	}
	
}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

public class StoreElementConfigResult {

	public static final StoreElementConfigResult configCreated(ElementConfigId configId) {
		return new StoreElementConfigResult(configId,true);
	}

	public static final StoreElementConfigResult configUpdated(ElementConfigId configId) {
		return new StoreElementConfigResult(configId,false);
	}

	
	private StoreElementConfigResult(ElementConfigId configId, boolean created) {
		this.configId = configId;
		this.created = created;
	}
	
	private ElementConfigId configId;
	private boolean created;
	
	
	public ElementConfigId getConfigId() {
		return configId;
	}	
	public boolean isCreated() {
		return created;
	}
	
}

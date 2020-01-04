/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import javax.json.JsonObject;

public class VisualizationConfig extends VisualizationConfigInfo {

	public static Builder newVisualizationConfig() {
		return new Builder();
	}
	
	public static class Builder extends BaseBuilder<VisualizationConfig, Builder>{
		
		
		protected Builder() {
			super(new VisualizationConfig());
		}
		
		public Builder withVisualizationConfig(JsonObject visualizationConfig) {
			assertNotInvalidated(getClass(), metric);
			metric.visualizationConfig = visualizationConfig;
			return this;
		}
		
	}
	
	private JsonObject visualizationConfig;
	
	public JsonObject getVisualizationConfig() {
		return visualizationConfig;
	}
	
}

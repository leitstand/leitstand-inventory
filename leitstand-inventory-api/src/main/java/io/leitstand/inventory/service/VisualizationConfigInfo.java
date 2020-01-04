/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import io.leitstand.commons.model.ValueObject;

public class VisualizationConfigInfo extends ValueObject {

	public static Builder newMetricVisualizationInfo() {
		return new Builder();
	}

	protected static class BaseBuilder<T extends VisualizationConfigInfo, B extends BaseBuilder<T,B>> {
		
		protected T metric;
		
		protected BaseBuilder(T metric) {
			this.metric = metric;
		}

		public B withVisualizationId(VisualizationConfigId id) {
			assertNotInvalidated(getClass(), metric);
			((VisualizationConfigInfo)metric).visualizationId = id;
			return (B)this;
		}

		
		public B withVisualizationName(VisualizationConfigName name) {
			assertNotInvalidated(getClass(), metric);
			((VisualizationConfigInfo)metric).visualizationName = name;
			return (B)this;
		}
		
		public B withCategory(String category) {
			assertNotInvalidated(getClass(), metric);
			((VisualizationConfigInfo)metric).category = category;
			return (B)this;
		}

		public B withDescription(String description) {
			assertNotInvalidated(getClass(), metric);
			((VisualizationConfigInfo)metric).description = description;
			return (B)this;
		}
		
		public B withVisualizationType(String visualizationType) {
			assertNotInvalidated(getClass(), metric);
			((VisualizationConfigInfo)metric).visualizationType = visualizationType;
			return (B)this;
		}
		
		public T build() {
			try {
				assertNotInvalidated(getClass(), metric);
				return metric;
			} finally {
				this.metric = null;
			}
		}
		
	}
	
	public static class Builder extends BaseBuilder<VisualizationConfigInfo,Builder>{
		protected Builder() {
			super(new VisualizationConfigInfo());
		}
	}
	
	private VisualizationConfigId visualizationId;
	private VisualizationConfigName visualizationName;
	private String visualizationType;
	private String category;
	private String description;
	
	public VisualizationConfigId getVisualizationId() {
		return visualizationId;
	}
	
	public VisualizationConfigName getVisualizationName() {
		return visualizationName;
	}
	
	public String getVisualizationType() {
		return visualizationType;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getDescription() {
		return description;
	}
	
}

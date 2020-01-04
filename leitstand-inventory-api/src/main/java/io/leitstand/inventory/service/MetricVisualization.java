/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

public class MetricVisualization extends BaseMetricEnvelope {

	public static Builder newMetricVisualization() {
		return new Builder();
	}
	
	public static class Builder extends BaseBuilder<MetricVisualization, Builder>{
		
		protected Builder() {
			super(new MetricVisualization());
		}
		
		public Builder withMetricVisualization(VisualizationConfig metricVisualization) {
			assertNotInvalidated(getClass(), metricVisualization);
			this.metric.visualization = metricVisualization;
			return this;
		}
		
		public Builder withMetricVisualization(VisualizationConfig.Builder metricVisualization) {
			return withMetricVisualization(metricVisualization.build());
		}

	}
	
	private VisualizationConfig visualization;
	
	public VisualizationConfig getVisualization() {
		return visualization;
	}
	
}

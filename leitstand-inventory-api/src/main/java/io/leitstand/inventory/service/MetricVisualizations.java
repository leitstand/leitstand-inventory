/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import io.leitstand.commons.model.BuilderUtil;

public class MetricVisualizations extends BaseMetricEnvelope {

	public static Builder newMetricVisualizations() {
		return new Builder();
	}
	
	public static class Builder extends BaseBuilder<MetricVisualizations,Builder>{
		protected Builder() {
			super(new MetricVisualizations());
		}
		
		public Builder withMetricVisualizations(VisualizationConfigInfo.Builder... visualizations) {
			return withMetricVisualizations(stream(visualizations)
											.map(VisualizationConfigInfo.Builder::build)
											.collect(toList()));
		}
		
		public Builder withMetricVisualizations(List<VisualizationConfigInfo> visualizations) {
			BuilderUtil.assertNotInvalidated(getClass(), metric);
			this.metric.visualizations = new ArrayList<>(visualizations);
			return this;
		}
	}
	
	
	private List<VisualizationConfigInfo> visualizations = emptyList();
	
	
	public List<VisualizationConfigInfo> getVisualizations() {
		return unmodifiableList(visualizations);
	}
}

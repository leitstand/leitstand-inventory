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

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

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A summary of all metrics available on an element.
 * <p>
 * The {@link MetricSettings} does not contain the current metric values but 
 * all necessary information to read the metrics from the telemetry platform.
 * </p>
 */
public class ElementMetrics extends BaseElementEnvelope {

	/**
	 * Returns a builder to create an immutable <code>ElementMetrics</code> instance.
	 * @return a builder to create an immutable <code>ElementMetrics</code> instance.
	 */
	public static Builder newElementMetrics() {
		return new Builder();
	}
	
	/**
	 * The builder to create an immutable <code>ElementMetrics</code> instance.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementMetrics, Builder> {
		
		public Builder() {
			super(new ElementMetrics());
		}
		
		/**
		 * Sets all available metrics.
		 * @param metrics
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withMetrics(MetricSettings.Builder... metrics) {
			return withMetrics(stream(metrics)
							   .map(MetricSettings.Builder::build)
							   .collect(toList()));
		}
		
		/**
		 * Sets all available metrics.
		 * @param metrics
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withMetrics(MetricSettings... metrics) {
			return withMetrics(asList(metrics));
		}
		
		/**
		 * Sets all available metrics.
		 * @param metrics
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withMetrics(List<MetricSettings> metrics) {
			assertNotInvalidated(getClass(), object);
			SortedMap<MetricName,MetricSettings> map = new TreeMap<>();
			for(MetricSettings metric : metrics) {
				map.put(metric.getMetricName(),metric);
			}
			return withMetrics(map);
		}
		
		/**
		 * Sets all available metrics.
		 * @param metrics
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withMetrics(SortedMap<MetricName,MetricSettings> metrics) {
			assertNotInvalidated(getClass(), object);
			object.metrics = unmodifiableMap(new TreeMap<>(metrics));
			return this;
		}

	}
	
	private Map<MetricName,MetricSettings> metrics;
	
	/**
	 * Returns an unmodifiable map of available metrics.
	 * Please note that {@link MetricSettings} does not supply the current metric values 
	 * but all necessary information to read the metric from the telemetry platform.
	 * @return the available metrics
	 */
	public Map<MetricName,MetricSettings> getMetrics() {
		return metrics;
	}
	
	
	
}

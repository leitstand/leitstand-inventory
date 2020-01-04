/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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

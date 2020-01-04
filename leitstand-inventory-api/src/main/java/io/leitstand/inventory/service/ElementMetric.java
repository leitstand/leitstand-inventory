/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

/**
 * A summary of all metrics available on an element.
 * <p>
 * The {@link MetricExport} does not contain the current metric values but 
 * all necessary information to read the metrics from the telemetry platform.
 * </p>
 */
public class ElementMetric extends BaseElementEnvelope {

	/**
	 * Returns a builder to create an immutable <code>ElementMetric</code> instance.
	 * @return a builder to create an immutable <code>ElementMetric</code> instance.
	 */
	public static Builder newElementMetric() {
		return new Builder();
	}
	
	/**
	 * The builder to create an immutable <code>ElementMetric</code> instance.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementMetric, Builder> {
		
		public Builder() {
			super(new ElementMetric());
		}

		/**
		 * Sets the metric description.
		 * @param metric
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withMetric(MetricSettings.Builder metric) {
			return withMetric(metric.build());
		}
		
		/**
		 * Sets the metric description.
		 * @param metric
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withMetric(MetricSettings metric) {
			assertNotInvalidated(getClass(),object);
			object.metric = metric;
			return this;
		}

	}
	
	private MetricSettings metric;
	
	/**
	 * Returns the immutable metric description
	 * Please note that {@link MetricExport} does not supply the current metric values 
	 * but all necessary information to read the metric from the telemetry platform.
	 * @return the available metrics
	 */
	public MetricSettings getMetric() {
		return metric;
	}
	
	
}

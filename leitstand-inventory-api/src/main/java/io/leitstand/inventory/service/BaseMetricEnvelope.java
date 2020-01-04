/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static io.leitstand.inventory.service.MetricId.randomMetricId;

import io.leitstand.commons.model.StringUtil;
import io.leitstand.commons.model.ValueObject;

public abstract class BaseMetricEnvelope extends ValueObject {

	static class BaseBuilder<T extends BaseMetricEnvelope,B extends BaseBuilder>{
		
		protected T metric;
		
		protected BaseBuilder(T metric) {
			this.metric = metric;
		}

		public B withMetricId(MetricId metricId) {
			assertNotInvalidated(getClass(), metric);
			((BaseMetricEnvelope)metric).metricId = metricId;
			return (B) this;
		}
		
		public B withMetricName(MetricName metricName) {
			assertNotInvalidated(getClass(), metric);
			((BaseMetricEnvelope)metric).metricName = metricName;
			return (B) this;
		}

		public B withDisplayName(String displayName) {
			assertNotInvalidated(getClass(), metric);
			((BaseMetricEnvelope)metric).displayName = displayName;
			return (B) this;
		}

		
		public B withMetricScope(MetricScope metricScope) {
			assertNotInvalidated(getClass(), metric);
			((BaseMetricEnvelope)metric).metricScope = metricScope;
			return (B) this;
		}

		public B withMetricUnit(String metricUnit) {
			assertNotInvalidated(getClass(), metric);
			((BaseMetricEnvelope)metric).metricUnit = metricUnit;
			return (B) this;
		}

		public T build() {
			try {
				assertNotInvalidated(getClass(), metric);
				if(StringUtil.isEmptyString(metric.getDisplayName())) {
					((BaseMetricEnvelope)metric).displayName = MetricName.toString(metric.getMetricName());
				}
				return metric;
			} finally {
				this.metric = null;
			}
		}
	}
	
	private MetricId metricId = randomMetricId();
	private MetricName metricName;
	private String displayName;
	private MetricScope metricScope;
	private String metricUnit;
	
	
	public MetricId getMetricId() {
		return metricId;
	}
	
	public MetricName getMetricName() {
		return metricName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public MetricScope getMetricScope() {
		return metricScope;
	}
	
	public String getMetricUnit() {
		return metricUnit;
	}
}

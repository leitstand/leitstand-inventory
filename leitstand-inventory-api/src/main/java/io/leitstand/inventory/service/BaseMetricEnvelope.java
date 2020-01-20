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

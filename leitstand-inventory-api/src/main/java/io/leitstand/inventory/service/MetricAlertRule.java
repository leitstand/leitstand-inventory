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

public class MetricAlertRule extends BaseMetricEnvelope {

	public static Builder newMetricAlertRule() {
		return new Builder();
	}
	
	public static class Builder extends BaseBuilder<MetricAlertRule,Builder>{
		
		protected Builder() {
			super(new MetricAlertRule());
		}
		
		public Builder withAlertRule(AlertRule rule) {
			assertNotInvalidated(getClass(), metric);
			metric.rule = rule;
			return this;
		}

		public Builder withAlertRule(AlertRule.Builder rule) {
			return withAlertRule(rule.build());
		}
		
	}
	
	
	private AlertRule rule;
	
	public AlertRule getRule() {
		return rule;
	}
	
}

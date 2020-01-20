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

public class MetricAlertRuleRevisions extends BaseMetricEnvelope {

	public static Builder newMetricAlertRuleRevisions() {
		return new Builder();
	}
	
	public static class Builder extends BaseBuilder<MetricAlertRuleRevisions,Builder>{
		protected Builder() {
			super(new MetricAlertRuleRevisions());
		}
		
		public Builder withRule(AlertRuleRevisions rule) {
			assertNotInvalidated(getClass(), metric);
			metric.rule = rule;
			return this;
		}
		
		public Builder withRule(AlertRuleRevisions.Builder rule) {
			return withRule(rule.build());
		}

	}
	

	private AlertRuleRevisions rule;
	
	public AlertRuleRevisions getRevisions() {
		return rule;
	}
	
}

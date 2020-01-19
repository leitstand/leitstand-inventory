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
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import io.leitstand.commons.model.BuilderUtil;

public class MetricAlertRules extends BaseMetricEnvelope {

	public static Builder newMetricAlertRules() {
		return new Builder();
	}
	
	public static class Builder extends BaseBuilder<MetricAlertRules, Builder>{
		
		protected Builder() {
			super(new MetricAlertRules());
		}
		
		public Builder withRules(AlertRuleInfo.Builder... rules) {
			return withRules(stream(rules)
							 .map(AlertRuleInfo.Builder::build)
							 .collect(toList()));
		}
		
		public Builder withRules(List<AlertRuleInfo> rules) {
			BuilderUtil.assertNotInvalidated(getClass(), metric);
			metric.rules = new ArrayList<>(rules);
			return this;
		}
	}

	private List<AlertRuleInfo> rules;
	
	public List<AlertRuleInfo> getRules(){
		return unmodifiableList(rules);
	}
	
}

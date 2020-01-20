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
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableSortedMap;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class MetricExport extends MetricSettings{
	
	public static Builder newMetricExport() {
		return new Builder();
	}

	public static class Builder extends BaseSettingsBuilder<MetricExport,Builder> {
		
		protected Builder() {
			super(new MetricExport());
		}
		
		public Builder withAlertRules(AlertRuleExport.Builder... alertRules) {
			return withAlertRules(stream(alertRules)
								  .map(AlertRuleExport.Builder::build)
								  .collect(toMap(AlertRuleExport::getRuleName,
										  		 identity())));
		}
		
		public Builder withAlertRules(List<AlertRuleExport> alertRules) {
			return withAlertRules(alertRules
								  .stream()
								  .collect(toMap(AlertRuleExport::getRuleName, 
										  		 identity())));
		}
		
		public Builder withAlertRules(Map<AlertRuleName,AlertRuleExport> alertRules) {
			assertNotInvalidated(getClass(),metric);
			metric.alertrules = new TreeMap<>(alertRules);
			return this;
		}
		
		public Builder withVisualizations(VisualizationConfig.Builder... visualization) {
			return withVisualizations(stream(visualization)
									  .map(VisualizationConfig.Builder::build)
									  .collect(toMap(VisualizationConfig::getVisualizationName,
										  		 identity())));
		}
		
		public Builder withVisualizations(List<VisualizationConfig> alertRules) {
			return withVisualizations(alertRules
								  	 .stream()
								  	 .collect(toMap(VisualizationConfig::getVisualizationName, 
										  		 identity())));
		}
		
		public Builder withVisualizations(Map<VisualizationConfigName,
										  VisualizationConfig> visualizations) {
			assertNotInvalidated(getClass(),metric);
			metric.visualizations = new TreeMap<>(visualizations);
			return this;
		}
	}
	
	
	private SortedMap<AlertRuleName,AlertRuleExport> alertrules;
	private SortedMap<VisualizationConfigName,VisualizationConfig> visualizations;

	
	public Map<AlertRuleName, AlertRuleExport> getAlertrules() {
		return unmodifiableSortedMap(alertrules);
	}
	
	public Map<VisualizationConfigName, VisualizationConfig> getVisualizations() {
		return unmodifiableSortedMap(visualizations);
	}


	
}

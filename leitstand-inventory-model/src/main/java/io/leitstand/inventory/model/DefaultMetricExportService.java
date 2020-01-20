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
package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.MetricExport.newMetricExport;
import static io.leitstand.inventory.service.MetricsExport.newMetricsExport;
import static java.lang.String.format;
import static java.util.logging.Level.FINEST;
import static java.util.stream.Collectors.toList;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.leitstand.inventory.service.AlertRuleExport;
import io.leitstand.inventory.service.AlertRuleInfo;
import io.leitstand.inventory.service.AlertRuleName;
import io.leitstand.inventory.service.MetricAlertRuleService;
import io.leitstand.inventory.service.MetricExport;
import io.leitstand.inventory.service.MetricExportService;
import io.leitstand.inventory.service.MetricSettings;
import io.leitstand.inventory.service.MetricSettingsService;
import io.leitstand.inventory.service.MetricVisualizationService;
import io.leitstand.inventory.service.MetricsExport;
import io.leitstand.inventory.service.VisualizationConfig;
import io.leitstand.inventory.service.VisualizationConfigInfo;
import io.leitstand.inventory.service.VisualizationConfigName;

@ApplicationScoped
public class DefaultMetricExportService implements MetricExportService {
	
	private static final Logger LOG = Logger.getLogger(DefaultMetricExportService.class.getName());
	
	@Inject
	private MetricSettingsService metricService;
	
	@Inject
	private MetricAlertRuleService alertRuleService;
	
	@Inject
	private MetricVisualizationService visualizationService;
	
	
	@Inject
	private AlertRuleExportService ruleService;
	
	public MetricsExport exportMetrics(String filter) {
		List<MetricExport> metrics = new LinkedList<>();
		for(MetricSettings settings : metricService.findMetrics(filter)) {
			List<VisualizationConfig> visualizations = loadVisualizations(settings);
			List<AlertRuleExport> alertRules = loadAlertRules(settings);
			metrics.add(newMetricExport()
						.withMetricId(settings.getMetricId())
						.withMetricName(settings.getMetricName())
						.withMetricScope(settings.getMetricScope())
						.withMetricUnit(settings.getMetricUnit())
						.withDescription(settings.getDescription())
						.withDisplayName(settings.getDisplayName())
						.withElementRoles(settings.getElementRoles())
						.withVisualizations(visualizations)
						.withAlertRules(alertRules)
						.build());
		}
		
		return newMetricsExport()
			   .withDateCreated(new Date())
			   .withMetrics(metrics)
			   .build();
	
	}
	
	private List<AlertRuleExport> loadAlertRules(MetricSettings settings){
		List<AlertRuleExport> alertrules = new LinkedList<>();
		for(AlertRuleName ruleName : alertRuleService.getMetricAlertRules(settings.getMetricName())
												  	 .getRules()
												  	 .stream()
												  	 .map(AlertRuleInfo::getRuleName)
												  	 .collect(toList())) {
			alertrules.add(ruleService.exportRule(settings.getMetricName(), 
												  ruleName));
		}
		return alertrules;
	}

	private List<VisualizationConfig> loadVisualizations(MetricSettings settings) {
		List<VisualizationConfig> visualizations = new LinkedList<>();
		for(VisualizationConfigName visualizationName : visualizationService.getMetricVisualizations(settings.getMetricName())
																	 		.getVisualizations()
																	 		.stream()
																	 		.map(VisualizationConfigInfo::getVisualizationName)
																	 		.collect(toList())) {
			visualizations.add(visualizationService.getMetricVisualization(settings.getMetricName(), 
																		   visualizationName).getVisualization());
		}
		return visualizations;
	}

	@Override
	public void importMetrics(MetricsExport export) {
		for(MetricExport metric : export.getMetrics()) {
			try {
				metricService.storeMetricSettings(metric);
				for(VisualizationConfig visualization : metric.getVisualizations().values()) {
					visualizationService.storeMetricVisualization(metric.getMetricName(), 
																  visualization);
				}
				for(AlertRuleExport alertRule : metric.getAlertrules().values()) {
					ruleService.importRule(metric.getMetricName(), 
										   alertRule);
				}
			} catch (Exception e) {
				LOG.fine(() -> format("Failed to import metric %s (%s) due to %s. Continue with import of remaining metrics.",
									  metric.getMetricName(),
									  metric.getMetricId(),
									  e.getMessage()));
				LOG.log(FINEST,e.getMessage(),e);
			}
		}
	}
	
}

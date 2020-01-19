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

import static io.leitstand.inventory.model.Metric_AlertRule.findAlertRule;
import static io.leitstand.inventory.model.Metric_AlertRule_Definition.findAlertRuleDefinitions;
import static io.leitstand.inventory.service.AlertRuleExport.newAlertRuleExport;
import static io.leitstand.inventory.service.AlertRuleRevision.newAlertRuleRevision;
import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.inject.Inject;

import io.leitstand.commons.model.Repository;
import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.AlertRuleExport;
import io.leitstand.inventory.service.AlertRuleName;
import io.leitstand.inventory.service.AlertRuleRevision;
import io.leitstand.inventory.service.MetricId;
import io.leitstand.inventory.service.MetricName;

@Service
public class AlertRuleExportService {

	@Inject
	private MetricProvider metrics;

	@Inject
	@Inventory
	private Repository repository;
	
	public AlertRuleExport exportRule(MetricId metricId, 
									  AlertRuleName ruleName) {
		Metric metric = metrics.fetchMetric(metricId);
		return exportRule(metric, ruleName);
	}
	
	public AlertRuleExport exportRule(MetricName metricName, 
									  AlertRuleName ruleName) {
		Metric metric = metrics.fetchMetric(metricName);
		return exportRule(metric,ruleName);
	}
	
	protected AlertRuleExport exportRule(Metric metric, 
										 AlertRuleName ruleName) {
		Metric_AlertRule alertRule = repository.execute(findAlertRule(metric, 
																	  ruleName));
		List<AlertRuleRevision> revisions = repository.execute(findAlertRuleDefinitions(metric, 
																						ruleName))
													  .stream()
													  .map(revision -> newAlertRuleRevision()
															  		   .withRuleId(revision.getRuleId())
															  		   .withRuleState(revision.getRuleState())
															  		   .withCreator(revision.getCreator())
															  		   .withRuleType(revision.getRuleType())
															  		   .withRuleDefinition(revision.getRuleDefinition())
															  		   .withDateModified(revision.getDateModified())
															  		   .build())
													  .collect(toList());
		
		return newAlertRuleExport()
			   .withRuleName(alertRule.getRuleName())
			   .withCategory(alertRule.getCategory())
			   .withDescription(alertRule.getDescription())
			   .withRevisions(revisions)
			   .build();
	}
	
	public void importRule(MetricId metricId, 
						   AlertRuleExport alertRule) {
		
	}
	
	public void importRule(MetricName metricName, 
						   AlertRuleExport alertRule) {
		
	}
	
	
}

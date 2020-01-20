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

import java.util.Date;

public interface MetricAlertRuleService {
	
	MetricAlertRule getMetricAlertRule(MetricId metricId,
									   AlertRuleName ruleName,
									   Date dateModified);
	MetricAlertRule getMetricAlertRule(MetricName metricName,
									   AlertRuleName ruleName,
									   Date dateModified);
	MetricAlertRuleRevisions getMetricAlertRuleRevisions(MetricId metricId, 
														 AlertRuleName ruleName);
	MetricAlertRuleRevisions getMetricAlertRuleRevisions(MetricName metricName, 
														 AlertRuleName ruleName);

	MetricAlertRules getMetricAlertRules(MetricId metricId);
	MetricAlertRules getMetricAlertRules(MetricName metricName);
	MetricAlertRule getMetricAlertRule(AlertRuleId ruleId);
	
	void removeAlertRule(MetricId metricId,
						 AlertRuleName ruleName);
	
	void removeAlertRule(MetricName metricName,
						 AlertRuleName ruleName);

	
	
	void removeAlertRuleRevision(MetricId metricId, 
								 AlertRuleName ruleName, 
								 Date dateModified);
	
	void removeAlertRuleRevision(MetricName metricName, 
								 AlertRuleName ruleName, 
								 Date dateModified);
	
	void removeAlertRuleRevision(AlertRuleId ruleId);

	boolean storeAlertRule(MetricId metricId,
						   AlertRule rule);
	
	boolean storeAlertRule(MetricName metricName,
						   AlertRule rule);
	
}

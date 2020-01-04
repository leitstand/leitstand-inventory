/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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

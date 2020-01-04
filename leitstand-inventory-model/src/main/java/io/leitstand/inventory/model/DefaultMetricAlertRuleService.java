/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import java.util.Date;

import javax.inject.Inject;

import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.AlertRule;
import io.leitstand.inventory.service.AlertRuleId;
import io.leitstand.inventory.service.AlertRuleName;
import io.leitstand.inventory.service.MetricAlertRule;
import io.leitstand.inventory.service.MetricAlertRuleRevisions;
import io.leitstand.inventory.service.MetricAlertRuleService;
import io.leitstand.inventory.service.MetricAlertRules;
import io.leitstand.inventory.service.MetricId;
import io.leitstand.inventory.service.MetricName;

@Service
public class DefaultMetricAlertRuleService implements MetricAlertRuleService{

	@Inject
	private MetricAlertRuleManager manager;
	
	@Inject
	private MetricProvider metrics;
	
	protected DefaultMetricAlertRuleService() {
		// CDI
	}
	
	protected DefaultMetricAlertRuleService(MetricAlertRuleManager manager,
											MetricProvider metrics) {
		this.manager = manager;
		this.metrics = metrics;
	}


	@Override
	public void removeAlertRule(MetricName metricName, 
								AlertRuleName ruleName) {
		Metric metric = metrics.fetchMetric(metricName);
		manager.removeAlertRule(metric,
								  ruleName);
	}

	@Override
	public MetricAlertRules getMetricAlertRules(MetricName metricName) {
		Metric metric = metrics.fetchMetric(metricName);
		return manager.getMetricAlertRules(metric);
	}

	@Override
	public MetricAlertRuleRevisions getMetricAlertRuleRevisions(MetricName metricName, 
																AlertRuleName ruleName) {
		Metric metric = metrics.fetchMetric(metricName);
		return manager.getMetricAlertRuleRevisions(metric,
													 ruleName);
	}

	@Override
	public MetricAlertRule getMetricAlertRule(MetricName metricName, 
											  AlertRuleName ruleName, 
											  Date dateModified) {
		Metric metric = metrics.fetchMetric(metricName);
		return manager.getMetricAlertRule(metric,
										    ruleName,
										    dateModified);
	}

	@Override
	public boolean storeAlertRule(MetricName metricName, 
								  AlertRule rule) {
		Metric metric = metrics.fetchMetric(metricName);
		return manager.storeAlertRule(metric,
										rule);
	}

	@Override
	public void removeAlertRuleRevision(MetricName metricName, 
										AlertRuleName ruleName, 
										Date dateModified) {
		Metric metric = metrics.fetchMetric(metricName);
		manager.removeAlertRuleRevision(metric,
										  ruleName,
										  dateModified);
	}

	@Override
	public MetricAlertRules getMetricAlertRules(MetricId metricId) {
		Metric metric = metrics.fetchMetric(metricId);
		return manager.getMetricAlertRules(metric);
	}

	@Override
	public MetricAlertRuleRevisions getMetricAlertRuleRevisions(MetricId metricId, 
																AlertRuleName ruleName) {
		Metric metric = metrics.fetchMetric(metricId);
		return manager.getMetricAlertRuleRevisions(metric, 
												     ruleName);
	}

	@Override
	public MetricAlertRule getMetricAlertRule(MetricId metricId, 
											  AlertRuleName ruleName,
											  Date dateModified) {
		Metric metric = metrics.fetchMetric(metricId);
		return manager.getMetricAlertRule(metric,
											ruleName, 
											dateModified);
	}

	@Override
	public boolean storeAlertRule(MetricId metricId, 
								  AlertRule rule) {
		Metric metric = metrics.fetchMetric(metricId);
		return manager.storeAlertRule(metric, 
										rule);
	}

	@Override
	public void removeAlertRule(MetricId metricId, 
								AlertRuleName 
								ruleName) {
		Metric metric = metrics.fetchMetric(metricId);
		manager.removeAlertRule(metric, 
								  ruleName);
	}

	@Override
	public void removeAlertRuleRevision(MetricId metricId, 
										AlertRuleName ruleName, 
										Date dateModified) {
		Metric metric = metrics.fetchMetric(metricId);
		manager.removeAlertRuleRevision(metric, 
										  ruleName, 
										  dateModified);
		
	}

	@Override
	public MetricAlertRule getMetricAlertRule(AlertRuleId ruleId) {
		return manager.getMetricAlertRule(ruleId);
	}

	@Override
	public void removeAlertRuleRevision(AlertRuleId ruleId) {
		manager.removeAlertRuleRevision(ruleId);
	}

}
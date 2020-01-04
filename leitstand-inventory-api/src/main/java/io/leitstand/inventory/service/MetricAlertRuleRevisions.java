/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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

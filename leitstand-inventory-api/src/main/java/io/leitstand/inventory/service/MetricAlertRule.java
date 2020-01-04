/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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

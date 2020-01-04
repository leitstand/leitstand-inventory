/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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

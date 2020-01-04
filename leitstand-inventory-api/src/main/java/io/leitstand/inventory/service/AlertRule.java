/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import javax.json.JsonObject;

import io.leitstand.commons.model.BuilderUtil;

public class AlertRule extends AlertRuleInfo {

	public static Builder newAlertRule() {
		return new Builder();
	}
	
	public static class Builder extends BaseBuilder<AlertRule,Builder>{
		
		protected Builder() {
			super(new AlertRule());
		}
		
		public Builder withRuleDefinition(JsonObject ruleDefinition) {
			BuilderUtil.assertNotInvalidated(getClass(), rule);
			rule.ruleDefinition = ruleDefinition;
			return this;
		}
	}
	
	private JsonObject ruleDefinition;
	
	public JsonObject getRuleDefinition() {
		return ruleDefinition;
	}
}

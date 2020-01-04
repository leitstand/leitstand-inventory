/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import javax.json.JsonObject;

public class AlertRuleRevision extends AlertRuleRevisionInfo{

	public static Builder newAlertRuleRevision() {
		return new Builder();
	}
	
	public static class Builder extends BaseBuilder<AlertRuleRevision,Builder> {
		
		protected Builder() {
			super(new AlertRuleRevision());
		}
		
		public Builder withRuleDefinition(JsonObject ruleDefinition) {
			assertNotInvalidated(getClass(), ruleDefinition);
			revision.ruleDefinition = ruleDefinition;
			return this;
		}
		
	}
	
	
	private JsonObject ruleDefinition;
	
	public JsonObject getRuleDefinition() {
		return ruleDefinition;
	}
}

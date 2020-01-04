/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import java.util.Date;

import io.leitstand.commons.model.ValueObject;

public class AlertRuleInfo extends ValueObject {
	
	public static Builder newAlertRuleInfo() {
		return new Builder();
	}

	public static class BaseBuilder<T extends AlertRuleInfo, B extends BaseBuilder<T,B>>{
		
		protected T rule;
		
		protected BaseBuilder(T rule) {
			this.rule = rule;
		}
		
		public B withRuleId(AlertRuleId ruleId) {
			assertNotInvalidated(getClass(), rule);
			((AlertRuleInfo)rule).ruleId = ruleId;
			return (B) this;
		}
		
		
		public B withRuleName(AlertRuleName ruleName) {
			assertNotInvalidated(getClass(), rule);
			((AlertRuleInfo)rule).ruleName = ruleName;
			return (B) this;
		}
		
		
		public B withRuleType(String ruleType) {
			assertNotInvalidated(getClass(), rule);
			((AlertRuleInfo)rule).ruleType = ruleType;
			return (B) this;
		}
		
		
		public B withRuleState(AlertRuleState ruleState) {
			assertNotInvalidated(getClass(), rule);
			((AlertRuleInfo)rule).ruleState = ruleState;
			return (B) this;
		}
		
		
		public B withCategory(String category) {
			assertNotInvalidated(getClass(), rule);
			((AlertRuleInfo)rule).category = category;
			return (B) this;
		}
		
		
		public B withCreator(String creator) {
			assertNotInvalidated(getClass(), rule);
			((AlertRuleInfo)rule).creator = creator;
			return (B) this;
		}
		
		public B withDescription(String description) {
			assertNotInvalidated(getClass(), rule);
			((AlertRuleInfo)rule).description = description;
			return (B) this;
		}
		
		
		public B withDateModified(Date dateModified) {
			assertNotInvalidated(getClass(), rule);
			((AlertRuleInfo)rule).dateModified = new Date(dateModified.getTime());
			return (B) this;
		}
		
		
		public T build() {
			try {
				assertNotInvalidated(getClass(), rule);
				return rule;
			} finally {
				this.rule = null;
			}
		}
		
	}
	
	public static class Builder extends BaseBuilder<AlertRuleInfo,Builder>{
		protected Builder() {
			super(new AlertRuleInfo());
		}
	}
	
	private AlertRuleId ruleId;
	private AlertRuleName ruleName;
	private AlertRuleState ruleState;
	private String ruleType;
	private String description;
	private String category;
	private String creator;
	private Date dateModified;
	
	
	public AlertRuleId getRuleId() {
		return ruleId;
	}
	
	public AlertRuleName getRuleName() {
		return ruleName;
	}
	
	public AlertRuleState getRuleState() {
		return ruleState;
	}
	
	public String getRuleType() {
		return ruleType;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getCreator() {
		return creator;
	}
	
	public Date getDateModified() {
		return new Date(dateModified.getTime());
	}
	
}

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

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import java.util.Date;

import io.leitstand.commons.model.ValueObject;

public class AlertRuleRevisionInfo extends ValueObject {

	public static Builder newAlertRuleRevisionInfo() {
		return new Builder();
	}
	
	protected static class BaseBuilder<T extends AlertRuleRevisionInfo,B extends BaseBuilder<T,B>> {
		
		protected T revision;
		
		protected BaseBuilder(T revision) {
			this.revision = revision;
		}
		
		public B withRuleId(AlertRuleId ruleId) {
			assertNotInvalidated(getClass(), revision);
			((AlertRuleRevisionInfo)revision).ruleId = ruleId;
			return (B) this;
		}
		
		public B withRuleState(AlertRuleState state) {
			assertNotInvalidated(getClass(), revision);
			((AlertRuleRevisionInfo)revision).ruleState = state;
			return (B) this;
		}
		
		
		public B withRuleType(String ruleType) {
			assertNotInvalidated(getClass(), revision);
			((AlertRuleRevisionInfo)revision).ruleType = ruleType;
			return (B) this;
		}
		
		
		public B withCreator(String creator) {
			assertNotInvalidated(getClass(), revision);
			((AlertRuleRevisionInfo)revision).creator = creator;
			return (B) this;
		}
		
		
		public B withDateModified(Date dateModified) {
			assertNotInvalidated(getClass(), revision);
			((AlertRuleRevisionInfo)revision).dateModified = new Date(dateModified.getTime());
			return (B) this;
		}
		
		public T build() {
			try {
				assertNotInvalidated(getClass(), revision);
				return revision;
			} finally {
				this.revision = null;
			}
		}
		
	}
	
	public static class Builder extends BaseBuilder<AlertRuleRevisionInfo,Builder>{
		protected Builder() {
			super(new AlertRuleRevisionInfo());
		}
	}
	
	private AlertRuleId ruleId;
	private AlertRuleState ruleState;
	private String ruleType;
	private String creator;
	private Date dateModified;
	
	
	public AlertRuleId getRuleId() {
		return ruleId;
	}
	
	public AlertRuleState getRuleState() {
		return ruleState;
	}
	
	public String getRuleType() {
		return ruleType;
	}
	
	public String getCreator() {
		return creator;
	}
	
	public Date getDateModified() {
		return new Date(dateModified.getTime());
	}
	
}

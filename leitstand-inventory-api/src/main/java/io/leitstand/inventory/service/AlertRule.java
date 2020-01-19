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
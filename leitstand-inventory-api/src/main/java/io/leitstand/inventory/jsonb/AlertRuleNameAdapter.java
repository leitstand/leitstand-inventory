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
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.AlertRuleName;

public class AlertRuleNameAdapter implements JsonbAdapter<AlertRuleName,String> {

	@Override
	public String adaptToJson(AlertRuleName ruleName) throws Exception {
		return AlertRuleName.toString(ruleName);
	}

	@Override
	public AlertRuleName adaptFromJson(String ruleName) throws Exception {
		return AlertRuleName.valueOf(ruleName);
	}

}

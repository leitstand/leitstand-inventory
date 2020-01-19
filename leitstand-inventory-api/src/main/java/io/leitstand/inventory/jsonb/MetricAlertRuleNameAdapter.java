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

/**
 * Translates an <code>AlertRuleName</code> into a string and vice versa.
 */
public class MetricAlertRuleNameAdapter implements JsonbAdapter<AlertRuleName, String> {

	/**
	 * Translates a string into an <code>AlertRuleName</code>.
	 * @param profileName the alert profile name
	 * @return the <code>AlertRuleName</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	@Override
	public AlertRuleName adaptFromJson(String profileName) throws Exception {
		return AlertRuleName.valueOf(profileName);
	}

	/**
	 * Translates an <code>AlertRuleName</code> into a string.
	 * @param profileName the alert profile name
	 * @return the alert profile name string or <code>null</code> if the specified profile name is <code>null</code>.
	 */
	@Override
	public String adaptToJson(AlertRuleName profileName) throws Exception {
		return AlertRuleName.toString(profileName);
	}

}

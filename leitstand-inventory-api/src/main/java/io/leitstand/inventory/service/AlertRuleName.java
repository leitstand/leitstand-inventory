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

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.AlertRuleNameAdapter;

/**
 * Unique alert rule name.
 * <p>
 * The alert rule name is unique per alert profile.
 */
@JsonbTypeAdapter(AlertRuleNameAdapter.class)
public class AlertRuleName extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * Creates an <code>AlertRuleName</code> from the specified string.
	 * @param name the alert rule name
	 * @return the <code>AlertRuleName</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static AlertRuleName valueOf(String name) {
		return fromString(name, AlertRuleName::new);
	}
	
	private String value;
	
	/**
	 * Creates an <code>AlertRuleName</code>.
	 * @param name the alert rule name
	 */
	public AlertRuleName(String name) {
		this.value = name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return value;
	}
	
}

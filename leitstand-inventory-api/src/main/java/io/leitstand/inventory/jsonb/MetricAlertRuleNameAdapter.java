/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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

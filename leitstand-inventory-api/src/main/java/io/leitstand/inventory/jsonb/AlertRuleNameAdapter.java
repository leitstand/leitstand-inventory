/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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

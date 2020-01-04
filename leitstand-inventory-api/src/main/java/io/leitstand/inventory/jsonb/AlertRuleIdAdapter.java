/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.AlertRuleId;

public class AlertRuleIdAdapter implements JsonbAdapter<AlertRuleId, String> {

	@Override
	public String adaptToJson(AlertRuleId ruleId) throws Exception {
		return AlertRuleId.toString(ruleId);
	}

	@Override
	public AlertRuleId adaptFromJson(String ruleId) throws Exception {
		return AlertRuleId.valueOf(ruleId);
	}

}

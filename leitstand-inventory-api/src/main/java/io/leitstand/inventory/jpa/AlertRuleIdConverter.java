/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.AlertRuleId;

@Converter(autoApply=true)
public class AlertRuleIdConverter implements AttributeConverter<AlertRuleId, String> {

	@Override
	public String convertToDatabaseColumn(AlertRuleId ruleId) {
		return AlertRuleId.toString(ruleId);
	}

	@Override
	public AlertRuleId convertToEntityAttribute(String ruleId) {
		return AlertRuleId.valueOf(ruleId);
	}

}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.AlertRuleName;

@Converter
public class AlertRuleNameConverter implements AttributeConverter<AlertRuleName, String> {

	@Override
	public String convertToDatabaseColumn(AlertRuleName name) {
		return AlertRuleName.toString(name);
	}

	@Override
	public AlertRuleName convertToEntityAttribute(String name) {
		return AlertRuleName.valueOf(name);
	}

}

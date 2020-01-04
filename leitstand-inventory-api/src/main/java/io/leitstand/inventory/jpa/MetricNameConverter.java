/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.MetricName;

@Converter
public class MetricNameConverter implements AttributeConverter<MetricName, String> {

	@Override
	public String convertToDatabaseColumn(MetricName attribute) {
		return MetricName.toString(attribute);
	}

	@Override
	public MetricName convertToEntityAttribute(String dbData) {
		return MetricName.valueOf(dbData);
	}

}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.Period;

@Converter
public class PeriodConverter implements AttributeConverter<Period, String> {

	@Override
	public String convertToDatabaseColumn(Period period) {
		return Period.toString(period);
	}

	@Override
	public Period convertToEntityAttribute(String period) {
		return Period.valueOf(period);
	}


}

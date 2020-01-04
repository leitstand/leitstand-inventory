/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.RackName;

@Converter
public class RackNameConverter implements AttributeConverter<RackName, String>{

	@Override
	public String convertToDatabaseColumn(RackName attribute) {
		return RackName.toString(attribute);
	}

	@Override
	public RackName convertToEntityAttribute(String dbData) {
		return RackName.valueOf(dbData);
	}

}

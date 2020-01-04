/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.ElementConfigId;

@Converter
public class ElementConfigIdConverter implements AttributeConverter<ElementConfigId, String>{

	@Override
	public String convertToDatabaseColumn(ElementConfigId attribute) {
		return ElementConfigId.toString(attribute);
	}

	@Override
	public ElementConfigId convertToEntityAttribute(String dbData) {
		return ElementConfigId.valueOf(dbData);
	}

}
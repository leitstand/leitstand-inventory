/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.ElementId;

@Converter
public class ElementIdConverter implements AttributeConverter<ElementId, String>{

	@Override
	public String convertToDatabaseColumn(ElementId attribute) {
		return ElementId.toString(attribute);
	}

	@Override
	public ElementId convertToEntityAttribute(String dbData) {
		return ElementId.valueOf(dbData);
	}

}

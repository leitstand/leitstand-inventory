/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.ElementGroupId;

@Converter
public class ElementGroupIdConverter implements AttributeConverter<ElementGroupId, String>{

	@Override
	public String convertToDatabaseColumn(ElementGroupId attribute) {
		return ElementGroupId.toString(attribute);
	}

	@Override
	public ElementGroupId convertToEntityAttribute(String dbData) {
		return ElementGroupId.valueOf(dbData);
	}

}

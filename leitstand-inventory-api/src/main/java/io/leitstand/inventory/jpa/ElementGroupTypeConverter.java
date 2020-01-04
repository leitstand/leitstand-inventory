/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.ElementGroupType;

@Converter
public class ElementGroupTypeConverter implements AttributeConverter<ElementGroupType, String>{

	@Override
	public String convertToDatabaseColumn(ElementGroupType attribute) {
		return ElementGroupType.toString(attribute);
	}

	@Override
	public ElementGroupType convertToEntityAttribute(String dbData) {
		return ElementGroupType.valueOf(dbData);
	}

}

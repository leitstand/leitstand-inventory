/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.ElementGroupName;

@Converter
public class ElementGroupNameConverter implements AttributeConverter<ElementGroupName, String>{

	@Override
	public String convertToDatabaseColumn(ElementGroupName attribute) {
		return ElementGroupName.toString(attribute);
	}

	@Override
	public ElementGroupName convertToEntityAttribute(String dbData) {
		return ElementGroupName.valueOf(dbData);
	}

}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.ElementName;

@Converter
public class ElementNameConverter implements AttributeConverter<ElementName, String>{

	@Override
	public String convertToDatabaseColumn(ElementName attribute) {
		return ElementName.toString(attribute);
	}

	@Override
	public ElementName convertToEntityAttribute(String dbData) {
		return ElementName.valueOf(dbData);
	}

}

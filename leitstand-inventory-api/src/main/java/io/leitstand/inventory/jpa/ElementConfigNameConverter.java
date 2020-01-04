/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.ElementConfigName;

@Converter
public class ElementConfigNameConverter implements AttributeConverter<ElementConfigName, String>{

	@Override
	public String convertToDatabaseColumn(ElementConfigName attribute) {
		return ElementConfigName.toString(attribute);
	}

	@Override
	public ElementConfigName convertToEntityAttribute(String dbData) {
		return ElementConfigName.valueOf(dbData);
	}

}

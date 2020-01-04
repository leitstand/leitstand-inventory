/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.ElementAlias;

@Converter
public class ElementAliasConverter implements AttributeConverter<ElementAlias, String>{

	@Override
	public String convertToDatabaseColumn(ElementAlias attribute) {
		return ElementAlias.toString(attribute);
	}

	@Override
	public ElementAlias convertToEntityAttribute(String dbData) {
		return ElementAlias.valueOf(dbData);
	}

}

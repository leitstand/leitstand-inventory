/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.ElementRoleName;

@Converter
public class ElementRoleNameConverter implements AttributeConverter<ElementRoleName, String>{

	@Override
	public String convertToDatabaseColumn(ElementRoleName attribute) {
		return ElementRoleName.toString(attribute);
	}

	@Override
	public ElementRoleName convertToEntityAttribute(String dbData) {
		return ElementRoleName.valueOf(dbData);
	}

}

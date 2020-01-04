/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.ElementRoleId;

@Converter
public class ElementRoleIdConverter implements AttributeConverter<ElementRoleId, String> {

	@Override
	public String convertToDatabaseColumn(ElementRoleId platformId) {
		return ElementRoleId.toString(platformId);
	}
	
	@Override
	public ElementRoleId convertToEntityAttribute(String platformId) {
		return ElementRoleId.valueOf(platformId);
	}

	

}

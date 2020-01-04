/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.InterfaceName;

@Converter(autoApply=true)
public class InterfaceNameConverter implements AttributeConverter<InterfaceName, String>{

	@Override
	public String convertToDatabaseColumn(InterfaceName attribute) {
		return InterfaceName.toString(attribute);
	}

	@Override
	public InterfaceName convertToEntityAttribute(String dbData) {
		return InterfaceName.valueOf(dbData);
	}

}

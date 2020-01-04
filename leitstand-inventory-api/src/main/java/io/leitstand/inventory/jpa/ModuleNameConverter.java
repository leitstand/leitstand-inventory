/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.ModuleName;

@Converter
public class ModuleNameConverter implements AttributeConverter<ModuleName, String> {

	@Override
	public String convertToDatabaseColumn(ModuleName attribute) {
		return ModuleName.toString(attribute);
	}

	@Override
	public ModuleName convertToEntityAttribute(String dbData) {
		return ModuleName.valueOf(dbData);
	}

}

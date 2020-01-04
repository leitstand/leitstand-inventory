/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.ApplicationName;

@Converter
public class ApplicationNameConverter implements AttributeConverter<ApplicationName, String> {

	@Override
	public String convertToDatabaseColumn(ApplicationName name) {
		return ApplicationName.toString(name);
	}

	@Override
	public ApplicationName convertToEntityAttribute(String s) {
		return ApplicationName.valueOf(s);
	}

}

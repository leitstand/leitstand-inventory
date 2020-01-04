/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.ServiceName;

@Converter
public class ServiceNameConverter implements AttributeConverter<ServiceName, String> {

	@Override
	public String convertToDatabaseColumn(ServiceName attribute) {
		return ServiceName.toString(attribute);
	}

	@Override
	public ServiceName convertToEntityAttribute(String dbData) {
		return ServiceName.valueOf(dbData);
	}

}

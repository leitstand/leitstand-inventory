/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019 
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.ServiceContextState;
@Converter
public class ServiceContextStateConverter implements AttributeConverter<ServiceContextState, String> {

	@Override
	public String convertToDatabaseColumn(ServiceContextState attribute) {
		if(attribute == null) {
			return null;
		}
		return attribute.name().substring(0, 1);
	}

	@Override
	public ServiceContextState convertToEntityAttribute(String dbData) {
		return ServiceContextState.parse(dbData);
	}

}

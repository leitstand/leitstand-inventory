/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.ServiceType;

@Converter
public class ServiceTypeConverter implements AttributeConverter<ServiceType, String> {

	@Override
	public String convertToDatabaseColumn(ServiceType type) {
		if(type == null) {
			return null;
		}
		return type.name().substring(0, 1);
	}

	@Override
	public ServiceType convertToEntityAttribute(String s) {
		return parse(s);
	}

	public static ServiceType parse(String s) {
		if(s == null || s.isEmpty()) {
			return null;
		}
		switch (s) {
			case "S":
				return ServiceType.SERVICE;
			case "V":
				return ServiceType.VM;
			case "O":
				return ServiceType.OS;
			case "C":
				return ServiceType.CONTAINER;
			case "D":
				return ServiceType.DAEMON;
			default:
				return null;
		}
	}

}

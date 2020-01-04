/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.IPv4Prefix;

@Converter
public class IPv4PrefixConverter implements AttributeConverter<IPv4Prefix, String>{

	@Override
	public String convertToDatabaseColumn(IPv4Prefix attribute) {
		return IPv4Prefix.toString(attribute);
	}

	@Override
	public IPv4Prefix convertToEntityAttribute(String dbData) {
		return IPv4Prefix.valueOf(dbData);
	}

}

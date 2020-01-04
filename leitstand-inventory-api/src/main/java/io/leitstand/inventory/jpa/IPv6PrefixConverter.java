/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.IPv6Prefix;

@Converter
public class IPv6PrefixConverter implements AttributeConverter<IPv6Prefix, String>{

	@Override
	public String convertToDatabaseColumn(IPv6Prefix attribute) {
		return IPv6Prefix.toString(attribute);
	}

	@Override
	public IPv6Prefix convertToEntityAttribute(String dbData) {
		return IPv6Prefix.valueOf(dbData);
	}

}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.DnsName;

@Converter
public class DnsNameConverter implements AttributeConverter<DnsName, String> {

	@Override
	public String convertToDatabaseColumn(DnsName name) {
		return DnsName.toString(name);
	}

	@Override
	public DnsName convertToEntityAttribute(String name) {
		return DnsName.valueOf(name);
	}

}

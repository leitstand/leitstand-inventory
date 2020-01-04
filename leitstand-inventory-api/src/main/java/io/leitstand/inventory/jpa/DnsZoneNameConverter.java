/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.DnsZoneName;

@Converter
public class DnsZoneNameConverter implements AttributeConverter<DnsZoneName, String> {

	@Override
	public String convertToDatabaseColumn(DnsZoneName name) {
		return DnsZoneName.toString(name);
	}

	@Override
	public DnsZoneName convertToEntityAttribute(String name) {
		return DnsZoneName.valueOf(name);
	}

}

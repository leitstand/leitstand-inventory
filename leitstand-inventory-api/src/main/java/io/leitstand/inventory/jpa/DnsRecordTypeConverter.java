/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.DnsRecordType;

@Converter
public class DnsRecordTypeConverter implements AttributeConverter<DnsRecordType, String> {

	@Override
	public String convertToDatabaseColumn(DnsRecordType id) {
		return DnsRecordType.toString(id);
	}

	@Override
	public DnsRecordType convertToEntityAttribute(String id) {
		return DnsRecordType.valueOf(id);
	}

}

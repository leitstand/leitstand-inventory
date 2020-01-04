/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.DnsRecordSetId;

@Converter
public class DnsRecordSetIdConverter implements AttributeConverter<DnsRecordSetId, String> {

	@Override
	public String convertToDatabaseColumn(DnsRecordSetId id) {
		return DnsRecordSetId.toString(id);
	}

	@Override
	public DnsRecordSetId convertToEntityAttribute(String id) {
		return DnsRecordSetId.valueOf(id);
	}

}

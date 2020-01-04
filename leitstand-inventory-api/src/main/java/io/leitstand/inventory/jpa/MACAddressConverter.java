/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.MACAddress;

@Converter
public class MACAddressConverter implements AttributeConverter<MACAddress, String>{

	@Override
	public String convertToDatabaseColumn(MACAddress attribute) {
		return MACAddress.toString(attribute);
	}

	@Override
	public MACAddress convertToEntityAttribute(String dbData) {
		return MACAddress.valueOf(dbData);
	}

}

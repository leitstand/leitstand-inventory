/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.PlatformId;

@Converter
public class PlatformIdConverter implements AttributeConverter<PlatformId, String> {

	@Override
	public String convertToDatabaseColumn(PlatformId platformId) {
		return PlatformId.toString(platformId);
	}
	
	@Override
	public PlatformId convertToEntityAttribute(String platformId) {
		return PlatformId.valueOf(platformId);
	}

	

}

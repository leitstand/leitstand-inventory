/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import static io.leitstand.commons.model.StringUtil.isEmptyString;
import static io.leitstand.inventory.service.AdministrativeState.UNKNOWN;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.AdministrativeState;

@Converter
public class AdministrativeStateConverter implements AttributeConverter<AdministrativeState, String> {

	@Override
	public String convertToDatabaseColumn(AdministrativeState state) {
		if(state == null) {
			return "UNKNOWN";
		}
		return administrativeStateDbString(state);
	}

	@Override
	public AdministrativeState convertToEntityAttribute(String s) {
		return toAdministrativeState(s);
	}

	public static String administrativeStateDbString(AdministrativeState state) {
		return AdministrativeState.toString(state);
	}
	
	public static AdministrativeState toAdministrativeState(String s) {
		if(isEmptyString(s)){
			return UNKNOWN;
		}
		return AdministrativeState.valueOf(s);
	}

}

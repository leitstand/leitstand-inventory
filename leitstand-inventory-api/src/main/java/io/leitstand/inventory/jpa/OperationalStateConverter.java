/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import static io.leitstand.inventory.service.OperationalState.UNKNOWN;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.OperationalState;

@Converter
public class OperationalStateConverter implements AttributeConverter<OperationalState, String> {

	@Override
	public String convertToDatabaseColumn(OperationalState state) {
		return operationalStateDbString(state);
	}

	@Override
	public OperationalState convertToEntityAttribute(String s) {
		return toOperationalState(s);
	}

	public static String operationalStateDbString(OperationalState state) {
		if(state == null){
			return UNKNOWN.getValue();
		}
		return OperationalState.toString(state);
	}
	
	public static OperationalState toOperationalState(String s) {
		OperationalState state = OperationalState.valueOf(s);
		if(state != null) {
			return state;
		}
		return UNKNOWN;
	}

}

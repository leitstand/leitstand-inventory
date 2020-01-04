/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.VlanId;
@Converter(autoApply=true)
public class VlanIdConverter implements AttributeConverter<VlanId, Integer> {

	@Override
	public Integer convertToDatabaseColumn(VlanId attribute) {
		if(attribute == null) {
			return null;
		}
		return attribute.getValue();
	}

	@Override
	public VlanId convertToEntityAttribute(Integer dbData) {
		if(dbData == null) {
			return null;
		}
		return new VlanId(dbData);
	}

}

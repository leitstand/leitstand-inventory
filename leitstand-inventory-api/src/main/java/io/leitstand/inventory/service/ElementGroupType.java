/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.ElementGroupTypeAdapter;

@JsonbTypeAdapter(ElementGroupTypeAdapter.class)
public class ElementGroupType extends Scalar<String> {

	private static final long serialVersionUID = 1L;

	public static ElementGroupType groupType(String type) {
		return valueOf(type);
	}

	
	public static ElementGroupType valueOf(String type) {
		return fromString(type, ElementGroupType::new);
	}
	
	private String value;
	
	public ElementGroupType(String value) {
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return value;
	}

}

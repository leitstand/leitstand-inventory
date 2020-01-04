/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.ElementConfigNameAdapter;

@JsonbTypeAdapter(ElementConfigNameAdapter.class)
public class ElementConfigName extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	private String value;

	public static ElementConfigName elementConfigName(String name) {
		return valueOf(name);
	}

	
	public static ElementConfigName valueOf(String name) {
		return Scalar.fromString(name, ElementConfigName::new);
	}
	
	public static String toString(ElementConfigName name) {
		return Scalar.toString(name);
	}
	
	public ElementConfigName(String name) {
		this.value = name;
	}
	
	@Override
	public String getValue() {
		return value;
	}


}

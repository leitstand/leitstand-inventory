/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.RackNameAdapter;

@JsonbTypeAdapter(RackNameAdapter.class)
public class RackName extends Scalar<String>{

	private static final long serialVersionUID = 1L;


	public static RackName rackName(String name) {
		return valueOf(name);
	}
	
	public static RackName valueOf(String name) {
		return fromString(name, RackName::new);
	}
	
	private String value;
	
	public RackName(String value) {
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return value;
	}

	
}

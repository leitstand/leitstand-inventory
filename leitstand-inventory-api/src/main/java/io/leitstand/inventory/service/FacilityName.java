package io.leitstand.inventory.service;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.FacilityNameAdapter;

@JsonbTypeAdapter(FacilityNameAdapter.class)
public class FacilityName extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	public static FacilityName facilityName(String name) {
		return valueOf(name);
	}
	
	public static FacilityName valueOf(String name) {
		return fromString(name,FacilityName::new);
	}

	private String value;
	
	public FacilityName(String name) {
		this.value = name;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	
}

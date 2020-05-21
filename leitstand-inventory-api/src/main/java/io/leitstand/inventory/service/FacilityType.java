package io.leitstand.inventory.service;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.FacilityTypeAdapter;

@JsonbTypeAdapter(FacilityTypeAdapter.class)
public class FacilityType extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	public static FacilityType facilityType(String name) {
		return valueOf(name);
	}
	
	public static FacilityType valueOf(String name) {
		return fromString(name,FacilityType::new);
	}

	private String value;
	
	public FacilityType(String name) {
		this.value = name;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	
}

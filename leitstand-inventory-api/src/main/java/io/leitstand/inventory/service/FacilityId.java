package io.leitstand.inventory.service;

import java.util.UUID;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.FacilityIdAdapter;

@JsonbTypeAdapter(FacilityIdAdapter.class)
public class FacilityId extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	public static FacilityId randomFacilityId() {
		return valueOf(UUID.randomUUID().toString());
	}
	
	public static FacilityId facilityId(String id) {
		return valueOf(id);
	}
	
	public static FacilityId valueOf(String id) {
		return fromString(id,FacilityId::new);
	}
	
	private String value;
	
	public FacilityId(String id) {
		this.value = id;
	}
	
	@Override
	public String getValue() {
		return value;
	}

}

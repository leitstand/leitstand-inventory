package io.leitstand.inventory.service;

import java.util.UUID;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.RackIdAdapter;

@JsonbTypeAdapter(RackIdAdapter.class)
public class RackId extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	public static RackId randomRackId() {
		return valueOf(UUID.randomUUID().toString());
	}
	
	public static RackId rackId(String rackId) {
		return valueOf(rackId);
	}
	
	public static RackId valueOf(String rackId) {
		return fromString(rackId, RackId::new);
	}
	
	private String value;

	public RackId(String rackId) {
		this.value = rackId;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	
}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import java.util.UUID;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.VisualizationConfigIdAdapter;

@JsonbTypeAdapter(VisualizationConfigIdAdapter.class)
public class VisualizationConfigId extends Scalar<String> {

	private static final long serialVersionUID = 1L;

	public static VisualizationConfigId randomVisualizationId() {
		return valueOf(UUID.randomUUID().toString());
	}
	
	public static VisualizationConfigId valueOf(String id) {
		return fromString(id, VisualizationConfigId::new);
	}
	
	private String value;
	
	public VisualizationConfigId(String id) {
		this.value = id;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	
}

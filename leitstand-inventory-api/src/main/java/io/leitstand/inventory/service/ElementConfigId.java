/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.Patterns.UUID_PATTERN;

import java.util.UUID;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.ElementConfigIdAdapter;

@JsonbTypeAdapter(ElementConfigIdAdapter.class)
public class ElementConfigId extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	public static ElementConfigId randomConfigId() {
		return valueOf(UUID.randomUUID().toString());
	}
	
	public static ElementConfigId elementConfigId(String configId) {
		return valueOf(configId);
	}
	
	public static ElementConfigId valueOf(String configId) {
		return fromString(configId,ElementConfigId::new);
	}
	
	@NotNull(message="{config_id.required}")
	@Pattern(message="{config_id.invalid}", regexp=UUID_PATTERN)
	private String value;
	
	public ElementConfigId(String value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}
	
}

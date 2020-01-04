/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.Patterns.UUID_PATTERN;
import static java.util.UUID.randomUUID;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.PlatformIdAdapter;

@JsonbTypeAdapter(PlatformIdAdapter.class)
public class PlatformId extends Scalar<String>{

	private static final long serialVersionUID = 1L;
	
	public static PlatformId randomPlatformId() {
		return valueOf(randomUUID().toString());
	}

	public static PlatformId valueOf(String id) {
		return fromString(id, PlatformId::new);
	}
	
	@NotNull(message="{platform_id.required}")
	@Pattern(message="{platform_id.invalid}", regexp=UUID_PATTERN)
	private String value;
	
	public PlatformId(String value) {
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return value;
	}


	
}

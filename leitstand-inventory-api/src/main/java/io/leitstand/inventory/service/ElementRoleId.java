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
import io.leitstand.inventory.jsonb.ElementRoleIdAdapter;

@JsonbTypeAdapter(ElementRoleIdAdapter.class)
public class ElementRoleId extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	public static ElementRoleId randomElementRoleId() {
		return valueOf(UUID.randomUUID().toString());
	}
	
	public static ElementRoleId elementRoleId(String uuid) {
		return valueOf(uuid);
	}
	
	public static ElementRoleId valueOf(String uuid) {
		return fromString(uuid,ElementRoleId::new);
	}

	@NotNull(message="{role_id.required}")
	@Pattern(regexp=UUID_PATTERN, message="{role_id.required}")
	private String value;
	
	public ElementRoleId(String value) {
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	
}

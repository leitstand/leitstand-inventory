/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.ElementRoleNameAdapter;

@JsonbTypeAdapter(ElementRoleNameAdapter.class)
public class ElementRoleName extends Scalar<String> {

	private static final long serialVersionUID = 1L;

	public static ElementRoleName elementRoleName(String role) {
		return valueOf(role);
	}
	
	public static ElementRoleName valueOf(String role) {
		return fromString(role,ElementRoleName::new);
	}
	
	@NotNull(message="{element_type.required}")
	@Pattern(regexp="[A-Za-z0-9_-]{1,64}", message="{element_type.invalid}")
	private String value;
	
	public ElementRoleName(String role){
		this.value = role;
	}
	
	@Override
	public String getValue() {
		return value;
	}


}

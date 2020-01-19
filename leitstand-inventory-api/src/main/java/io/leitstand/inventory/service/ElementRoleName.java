/*
 * Copyright 2020 RtBrick Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
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

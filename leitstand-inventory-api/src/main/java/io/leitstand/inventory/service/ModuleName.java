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
import io.leitstand.inventory.jsonb.ModuleNameAdapter;

@JsonbTypeAdapter(ModuleNameAdapter.class)
public class ModuleName extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * Alias for {@link #getValue()} to improve readability.
	 * Creates a <code>ModuleName</code> from the specified string.
	 * @param name the module name
	 * @return the <code>ModuleName</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */

	public static ModuleName moduleName(String name) {
		return valueOf(name);
	}
	
	/**
	 * Creates a <code>ModuleName</code> from the specified string.
	 * @param name the module name
	 * @return the <code>ModuleName</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static ModuleName valueOf(String name) {
		return fromString(name,ModuleName::new);
	}

	
	@NotNull(message="{module_name.required}")
	@Pattern(regexp="\\p{Print}{1,64}",message="{module_name.invalid}" )
	private String value;
	
	public ModuleName(String name){
		this.value = name;
	}
	
	@Override
	public String getValue() {
		return value;
	}

}

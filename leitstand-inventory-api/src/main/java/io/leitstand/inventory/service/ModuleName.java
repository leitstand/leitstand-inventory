/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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

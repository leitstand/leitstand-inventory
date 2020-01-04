/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.EnvironmentNameAdapter;

/**
 * Unique environment name.
 */
@JsonbTypeAdapter(EnvironmentNameAdapter.class)
public class EnvironmentName extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * Alias for {@link #valueOf(String)} to improve readability.
	 * <p>
	 * Converts a string to an <code>EnvironmentName</code>.
	 * @param name the environment name
	 * @return the <code>EnvironmentName</code> or <code>null</code> if the specified name is <code>null</code> or empty.
	 */
	public static EnvironmentName environmentName(String name) {
		return valueOf(name);
	}
	
	/**
	 * Converts a string to an <code>EnvironmentName</code>.
	 * @param name the environment name
	 * @return the <code>EnvironmentName</code> or <code>null</code> if the specified name is <code>null</code> or empty.
	 */
	public static EnvironmentName valueOf(String name) {
		return Scalar.fromString(name,EnvironmentName::new);
	}
	
	// TODO Add pattern
	private String value;
	
	public EnvironmentName(String value) {
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	
}

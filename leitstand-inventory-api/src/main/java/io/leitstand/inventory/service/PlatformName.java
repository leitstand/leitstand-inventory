package io.leitstand.inventory.service;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.PlatformNameAdapter;

/**
 * Unique name of an element (hardware) platform.
 */
@JsonbTypeAdapter(PlatformNameAdapter.class)
public class PlatformName extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a <code>PlatformName</code>.
	 * This method is an alias of the {@link #valueOf(String)} method that can be used for static imports.
	 * Returns <code>null</code> if the specified name is <code>null</code> or an empty string.
	 * @param name the platform name
	 * @return the platform name or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static final PlatformName platformName(String name) {
		return valueOf(name);
	}
	
	/**
	 * Creates a <code>PlatformName</code>.
	 * Returns <code>null</code> if the specified name is <code>null</code> or an empty string.
	 * @param name the platform name
	 * @return the platform name or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static final PlatformName valueOf(String name) {
		return fromString(name,PlatformName::new);
	}
	
	private String value;
	
	/**
	 * Creates a platform name.
	 * @param name the platform name
	 */
	public PlatformName(String name) {
		this.value = name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return value;
	}
	
}

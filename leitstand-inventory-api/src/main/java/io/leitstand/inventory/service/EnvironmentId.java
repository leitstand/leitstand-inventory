/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import java.util.UUID;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.EnvironmentIdAdapter;

/**
 * Unique environment ID in UUIDv4 format.
 */
@JsonbTypeAdapter(EnvironmentIdAdapter.class)
public class EnvironmentId extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a random environment ID.
	 * @return a random environment ID.
	 */
	public static EnvironmentId randomEnvironmentId() {
		return valueOf(UUID.randomUUID().toString());
	}

	/**
	 * Alias for {@link #valueOf(String)} to improve readability.
	 * <p>
	 * Converts a string to an <code>EnvironmentName</code>.
	 * @param name the environment name
	 * @return the <code>EnvironmentName</code> or <code>null</code> if the specified name is <code>null</code> or empty.
	 */
	public static EnvironmentId environmentId(String name) {
		return valueOf(name);
	}
	
	/**
	 * Converts a string to an <code>EnvironmentName</code>.
	 * @param name the environment name
	 * @return the <code>EnvironmentName</code> or <code>null</code> if the specified name is <code>null</code> or empty.
	 */
	public static EnvironmentId valueOf(String name) {
		return Scalar.fromString(name,EnvironmentId::new);
	}
	
	// TODO Add pattern
	private String value;
	
	public EnvironmentId(String value) {
		this.value = value;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return value;
	}
	
}

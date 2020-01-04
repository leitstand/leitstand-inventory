/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.EnvironmentId;

/**
 * Translates an <code>EnvironmentId</code> into a string and vice versa.
 */
public class EnvironmentIdAdapter implements JsonbAdapter<EnvironmentId, String>{

	/**
	 * Translates a string into an <code>EnvironmentId</code>.
	 * @param id the environment ID
	 * @return the <code>EnvironmentId</code> or <code>null</code> if the specified ID is <code>null</code> or empty.
	 */
	@Override
	public EnvironmentId adaptFromJson(String id) throws Exception {
		return EnvironmentId.valueOf(id);
	}

	/** 
	 * Translates an <code>EnvironmentId</code> into a string.
	 * @param id the environment ID
	 * @returns the string value or <code>null</code> if the specified ID is <code>null</code>.
	 */
	@Override
	public String adaptToJson(EnvironmentId id) throws Exception {
		return EnvironmentId.toString(id);
	}

}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.EnvironmentName;

/**
 * Translates an <code>EnvironmentName</code> into a string and vice versa.
 */
public class EnvironmentNameAdapter implements JsonbAdapter<EnvironmentName, String>{

	/**
	 * Translates a string into an <code>EnvironmentName</code>.
	 * @param name the environment name
	 * @return the <code>EnvironmentName</code> or <code>null</code> if the specified name is <code>null</code> or empty.
	 */
	@Override
	public EnvironmentName adaptFromJson(String name) throws Exception {
		return EnvironmentName.valueOf(name);
	}

	/** 
	 * Translates an <code>EnvironmentName</code> into a string.
	 * @param name the environment name
	 * @returns the string value or <code>null</code> if the specified name is <code>null</code>.
	 */
	@Override
	public String adaptToJson(EnvironmentName name) throws Exception {
		return EnvironmentName.toString(name);
	}

}

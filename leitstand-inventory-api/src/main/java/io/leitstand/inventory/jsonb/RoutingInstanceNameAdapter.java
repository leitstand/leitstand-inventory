/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.RoutingInstanceName;

/**
 * Translates a <code>RoutingInstanceName</code> into a string and vice versa.
 */
public class RoutingInstanceNameAdapter implements JsonbAdapter<RoutingInstanceName, String> {

	/**
	 * Translates a string into a <code>RoutingInstanceName</code>.
	 * @param name the routing instance name
	 * @return the <code>RoutingInstanceName</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	@Override
	public RoutingInstanceName adaptFromJson(String name) {
		return RoutingInstanceName.valueOf(name);
	}

	/**
	 * Translates a routing instance name into a string.
	 * @param name the routing instance name
	 * @return the routing instance name or <code>null</code> if the specified name is <code>null</code>.
	 */
	@Override
	public String adaptToJson(RoutingInstanceName name) {
		return RoutingInstanceName.toString(name);
	}

}
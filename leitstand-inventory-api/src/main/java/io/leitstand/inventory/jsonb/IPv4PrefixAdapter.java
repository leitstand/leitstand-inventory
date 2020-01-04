/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.IPv4Prefix;

public class IPv4PrefixAdapter implements JsonbAdapter<IPv4Prefix,String> {

	@Override
	public IPv4Prefix adaptFromJson(String v) throws Exception {
		return IPv4Prefix.valueOf(v);
	}

	@Override
	public String adaptToJson(IPv4Prefix v) throws Exception {
		return IPv4Prefix.toString(v);
	}

}
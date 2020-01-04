/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.IPv6Prefix;

public class IPv6PrefixAdapter implements JsonbAdapter<IPv6Prefix,String> {

	@Override
	public IPv6Prefix adaptFromJson(String v) throws Exception {
		return IPv6Prefix.valueOf(v);
	}

	@Override
	public String adaptToJson(IPv6Prefix v) throws Exception {
		return IPv6Prefix.toString(v);
	}

}

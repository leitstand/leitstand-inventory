/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.DnsZoneId;

public class DnsZoneIdAdapter implements JsonbAdapter<DnsZoneId, String>{

	@Override
	public String adaptToJson(DnsZoneId name) throws Exception {
		return DnsZoneId.toString(name);
	}

	@Override
	public DnsZoneId adaptFromJson(String name) throws Exception {
		return DnsZoneId.valueOf(name);
	}

}

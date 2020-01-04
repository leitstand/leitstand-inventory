/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.DnsZoneName;

public class DnsZoneNameAdapter implements JsonbAdapter<DnsZoneName, String>{

	@Override
	public String adaptToJson(DnsZoneName name) throws Exception {
		return DnsZoneName.toString(name);
	}

	@Override
	public DnsZoneName adaptFromJson(String name) throws Exception {
		return DnsZoneName.valueOf(name);
	}

}

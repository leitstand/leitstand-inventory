/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.DnsName;

public class DnsNameAdapter implements JsonbAdapter<DnsName, String>{

	@Override
	public String adaptToJson(DnsName name) throws Exception {
		return DnsName.toString(name);
	}

	@Override
	public DnsName adaptFromJson(String name) throws Exception {
		return DnsName.valueOf(name);
	}

}

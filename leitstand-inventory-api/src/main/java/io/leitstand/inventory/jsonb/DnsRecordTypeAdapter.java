/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.DnsRecordType;

public class DnsRecordTypeAdapter implements JsonbAdapter<DnsRecordType, String> {

	@Override
	public String adaptToJson(DnsRecordType type) throws Exception {
		return DnsRecordType.toString(type);
	}

	@Override
	public DnsRecordType adaptFromJson(String type) throws Exception {
		return DnsRecordType.valueOf(type);
	}

}

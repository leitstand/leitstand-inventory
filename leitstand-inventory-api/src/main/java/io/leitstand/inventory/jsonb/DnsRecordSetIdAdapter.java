/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.DnsRecordSetId;

public class DnsRecordSetIdAdapter implements JsonbAdapter<DnsRecordSetId, String> {

	@Override
	public String adaptToJson(DnsRecordSetId id) throws Exception {
		return DnsRecordSetId.toString(id);
	}

	@Override
	public DnsRecordSetId adaptFromJson(String id) throws Exception {
		return DnsRecordSetId.valueOf(id);
	}

}

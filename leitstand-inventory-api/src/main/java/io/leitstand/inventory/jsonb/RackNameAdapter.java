/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.RackName;

public class RackNameAdapter implements JsonbAdapter<RackName,String> {

	@Override
	public RackName adaptFromJson(String v) throws Exception {
		return RackName.valueOf(v);
	}

	@Override
	public String adaptToJson(RackName v) throws Exception {
		return RackName.toString(v);
	}

}

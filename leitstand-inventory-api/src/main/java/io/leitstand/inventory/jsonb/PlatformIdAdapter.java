/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.PlatformId;

public class PlatformIdAdapter implements JsonbAdapter<PlatformId,String> {

	@Override
	public String adaptToJson(PlatformId obj) throws Exception {
		return PlatformId.toString(obj);
	}

	@Override
	public PlatformId adaptFromJson(String obj) throws Exception {
		return PlatformId.valueOf(obj);
	}



}

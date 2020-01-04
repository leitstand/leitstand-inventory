/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.Version;

public class VersionAdapter implements JsonbAdapter<Version,String> {

	@Override
	public Version adaptFromJson(String v) throws Exception {
		return Version.valueOf(v);
	}

	@Override
	public String adaptToJson(Version v) throws Exception {
		return Version.toString(v);
	}
	
}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.ModuleName;

public class ModuleNameAdapter implements JsonbAdapter<ModuleName,String> {

	@Override
	public ModuleName adaptFromJson(String v) throws Exception {
		return ModuleName.valueOf(v);
	}

	@Override
	public String adaptToJson(ModuleName v) throws Exception {
		return ModuleName.toString(v);
	}

}

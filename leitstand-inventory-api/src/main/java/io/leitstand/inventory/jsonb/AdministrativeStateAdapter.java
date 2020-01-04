/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.AdministrativeState;

public class AdministrativeStateAdapter implements JsonbAdapter<AdministrativeState,String> {

	@Override
	public String adaptToJson(AdministrativeState obj) throws Exception {
		return AdministrativeState.toString(obj);
	}

	@Override
	public AdministrativeState adaptFromJson(String obj) throws Exception {
		return AdministrativeState.valueOf(obj);
	}



}

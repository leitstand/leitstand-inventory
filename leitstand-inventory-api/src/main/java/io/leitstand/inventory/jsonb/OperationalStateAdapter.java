/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.OperationalState;

public class OperationalStateAdapter implements JsonbAdapter<OperationalState,String> {

	@Override
	public String adaptToJson(OperationalState obj) throws Exception {
		return OperationalState.toString(obj);
	}

	@Override
	public OperationalState adaptFromJson(String obj) throws Exception {
		return OperationalState.valueOf(obj);
	}



}

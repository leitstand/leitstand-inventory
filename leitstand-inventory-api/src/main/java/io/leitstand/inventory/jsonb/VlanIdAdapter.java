/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.VlanId;

public class VlanIdAdapter implements JsonbAdapter<VlanId,Integer> {

	@Override
	public VlanId adaptFromJson(Integer v) throws Exception {
		if(v == null){
			return null;
		}
		return new VlanId(v);
	}

	@Override
	public Integer adaptToJson(VlanId v) throws Exception {
		if(v == null){
			return null;
		}
		return v.getValue();
	}

}

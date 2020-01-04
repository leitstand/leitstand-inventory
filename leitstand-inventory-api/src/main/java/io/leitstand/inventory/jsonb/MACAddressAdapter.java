/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.MACAddress;

public class MACAddressAdapter implements JsonbAdapter<MACAddress,String> {

	@Override
	public MACAddress adaptFromJson(String v) throws Exception {
		return MACAddress.valueOf(v);
	}

	@Override
	public String adaptToJson(MACAddress v) throws Exception {
		return MACAddress.toString(v);
	}

}

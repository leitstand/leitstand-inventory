/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.InterfaceName;

public class InterfaceNameAdapter implements JsonbAdapter<InterfaceName,String> {

	@Override
	public InterfaceName adaptFromJson(String v) throws Exception {
		return InterfaceName.valueOf(v);
	}

	@Override
	public String adaptToJson(InterfaceName v) throws Exception {
		return InterfaceName.toString(v);
	}

}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.ElementConfigId;

public class ElementConfigIdAdapter implements JsonbAdapter<ElementConfigId,String> {

	@Override
	public String adaptToJson(ElementConfigId obj) throws Exception {
		return ElementConfigId.toString(obj);
	}

	@Override
	public ElementConfigId adaptFromJson(String obj) throws Exception {
		return ElementConfigId.valueOf(obj);
	}

}

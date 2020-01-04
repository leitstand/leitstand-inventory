/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.ElementId;

public class ElementIdAdapter implements JsonbAdapter<ElementId,String> {

	@Override
	public String adaptToJson(ElementId obj) throws Exception {
		return ElementId.toString(obj);
	}

	@Override
	public ElementId adaptFromJson(String obj) throws Exception {
		return ElementId.valueOf(obj);
	}

}

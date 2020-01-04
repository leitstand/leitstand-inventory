/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.ElementGroupId;

public class ElementGroupIdAdapter implements JsonbAdapter<ElementGroupId,String> {

	@Override
	public ElementGroupId adaptFromJson(String v) throws Exception {
		return ElementGroupId.valueOf(v);
	}

	@Override
	public String adaptToJson(ElementGroupId v) throws Exception {
		return ElementGroupId.toString(v);
	}

}
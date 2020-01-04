/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.ElementGroupName;

public class ElementGroupNameAdapter implements JsonbAdapter<ElementGroupName,String> {

	@Override
	public ElementGroupName adaptFromJson(String v) throws Exception {
		return ElementGroupName.valueOf(v);
	}

	@Override
	public String adaptToJson(ElementGroupName v) throws Exception {
		return ElementGroupName.toString(v);
	}

}
/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.ElementGroupType;

public class ElementGroupTypeAdapter implements JsonbAdapter<ElementGroupType,String> {

	@Override
	public ElementGroupType adaptFromJson(String v) throws Exception {
		return ElementGroupType.valueOf(v);
	}

	@Override
	public String adaptToJson(ElementGroupType v) throws Exception {
		return ElementGroupType.toString(v);
	}

}
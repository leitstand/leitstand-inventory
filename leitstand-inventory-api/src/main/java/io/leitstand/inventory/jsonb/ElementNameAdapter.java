/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.ElementName;

public class ElementNameAdapter implements JsonbAdapter<ElementName,String> {

	@Override
	public String adaptToJson(ElementName obj) throws Exception {
		return ElementName.toString(obj);
	}

	@Override
	public ElementName adaptFromJson(String obj) throws Exception {
		return ElementName.valueOf(obj);
	}

}
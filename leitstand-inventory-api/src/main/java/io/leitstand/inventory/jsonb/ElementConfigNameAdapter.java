/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.ElementConfigName;

public class ElementConfigNameAdapter implements JsonbAdapter<ElementConfigName,String> {

	@Override
	public String adaptToJson(ElementConfigName obj) throws Exception {
		return ElementConfigName.toString(obj);
	}

	@Override
	public ElementConfigName adaptFromJson(String obj) throws Exception {
		return ElementConfigName.valueOf(obj);
	}

}

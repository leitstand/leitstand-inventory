/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.ElementAlias;

public class ElementAliasAdapter implements JsonbAdapter<ElementAlias,String> {

	@Override
	public String adaptToJson(ElementAlias obj) throws Exception {
		return ElementAlias.toString(obj);
	}

	@Override
	public ElementAlias adaptFromJson(String obj) throws Exception {
		return ElementAlias.valueOf(obj);
	}

}
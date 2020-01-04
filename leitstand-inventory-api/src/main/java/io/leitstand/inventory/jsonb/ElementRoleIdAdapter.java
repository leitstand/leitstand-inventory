/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.ElementRoleId;

public class ElementRoleIdAdapter implements JsonbAdapter<ElementRoleId,String> {

	@Override
	public String adaptToJson(ElementRoleId obj) throws Exception {
		return ElementRoleId.toString(obj);
	}

	@Override
	public ElementRoleId adaptFromJson(String obj) throws Exception {
		return ElementRoleId.valueOf(obj);
	}



}

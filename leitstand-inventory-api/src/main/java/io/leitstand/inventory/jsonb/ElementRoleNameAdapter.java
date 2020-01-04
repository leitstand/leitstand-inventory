/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.ElementRoleName;

public class ElementRoleNameAdapter implements JsonbAdapter<ElementRoleName,String> {

	@Override
	public String adaptToJson(ElementRoleName obj) throws Exception {
		return ElementRoleName.toString(obj);
	}

	@Override
	public ElementRoleName adaptFromJson(String obj) throws Exception {
		return ElementRoleName.valueOf(obj);
	}


}

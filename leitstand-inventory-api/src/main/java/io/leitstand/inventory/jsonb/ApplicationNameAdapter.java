/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.ApplicationName;

public class ApplicationNameAdapter implements JsonbAdapter<ApplicationName,String> {

	@Override
	public String adaptToJson(ApplicationName obj) throws Exception {
		return ApplicationName.toString(obj);
	}

	@Override
	public ApplicationName adaptFromJson(String obj) throws Exception {
		return ApplicationName.valueOf(obj);
	}



}

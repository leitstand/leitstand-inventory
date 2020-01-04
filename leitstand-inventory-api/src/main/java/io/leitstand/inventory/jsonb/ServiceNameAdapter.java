/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.ServiceName;

public class ServiceNameAdapter implements JsonbAdapter<ServiceName,String> {

	@Override
	public ServiceName adaptFromJson(String v) throws Exception {
		return ServiceName.valueOf(v);
	}

	@Override
	public String adaptToJson(ServiceName v) throws Exception {
		return ServiceName.toString(v);
	}

}

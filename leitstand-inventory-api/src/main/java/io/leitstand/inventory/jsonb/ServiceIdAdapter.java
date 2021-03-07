package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.service.ServiceId;

public class ServiceIdAdapter implements JsonbAdapter<ServiceId,String>{

	@Override
	public ServiceId adaptFromJson(String v) throws Exception {
		return ServiceId.valueOf(v);
	}

	@Override
	public String adaptToJson(ServiceId v) throws Exception {
		return Scalar.toString(v);
	}
	
}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.ServiceNameAdapter;

@JsonbTypeAdapter(ServiceNameAdapter.class)
public class ServiceName extends Scalar<String>{

	private static final long serialVersionUID = 1L;
	
	public static ServiceName serviceName(String name) {
		return valueOf(name);
	}
	
	public static ServiceName valueOf(String name) {
		return fromString(name,ServiceName::new);
	}

	@NotNull(message="{service_name.required}")
	@Pattern(regexp="\\p{Print}{1,64}",message="{service_name.invalid}" )
	private String value;
	
	public ServiceName(String name){
		this.value = name;
	}
	
	@Override
	public String getValue() {
		return value;
	}




}

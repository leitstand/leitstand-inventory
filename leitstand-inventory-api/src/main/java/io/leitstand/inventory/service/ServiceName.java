/*
 * Copyright 2020 RtBrick Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
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

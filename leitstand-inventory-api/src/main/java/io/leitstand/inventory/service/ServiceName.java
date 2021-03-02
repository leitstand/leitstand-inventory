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

/**
 * Unique service name.
 * <p>
 * The service name can be changed provided that new service name is also unique.
 * @see ServiceId
 * @see ServiceDefinition
 */
@JsonbTypeAdapter(ServiceNameAdapter.class)
public class ServiceName extends Scalar<String>{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a service name from the specified string.
	 * Returns <code>null</code> if the specified string is <code>null</code> or empty.
	 * <p>
	 * This method is an alias of the {@link ServiceName#valueOf(String)} method to improve readability by avoiding static import conflicts.
	 * @param serviceName the service name
	 * @return the service name or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static ServiceName serviceName(String serviceName) {
		return valueOf(serviceName);
	}
	
	/**
     * Creates a service name from the specified string.
     * Returns <code>null</code> if the specified string is <code>null</code> or empty.
     * @param serviceName the service name
     * @return the service name or <code>null</code> if the specified string is <code>null</code> or empty.
     */
	public static ServiceName valueOf(String name) {
		return fromString(name,ServiceName::new);
	}

	@NotNull(message="{service_name.required}")
	@Pattern(regexp="\\p{Print}{1,64}",message="{service_name.invalid}" )
	private String value;

	/**
	 * Creates a service name.
	 * @param serviceName the service name.
	 */
	public ServiceName(String serviceName){
		this.value = serviceName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return value;
	}

}
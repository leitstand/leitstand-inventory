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

import static io.leitstand.commons.model.Patterns.UUID_PATTERN;
import static java.util.UUID.randomUUID;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.ServiceIdAdapter;

/**
 * A unique service ID in UUIDv4 format.
 * <p>
 * A service ID is immutable and cannot be changed. It forms a persistent unique key for a service definition.
 * @see ServiceName
 * @see ServiceDefinition
 */
@JsonbTypeAdapter(ServiceIdAdapter.class)
public class ServiceId extends Scalar<String>{

	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Creates a random service ID.
	 * @return a random service ID.
	 */
	public static ServiceId randomServiceId() {
        return valueOf(randomUUID().toString());
    }
	
	/**
	 * Creates a service ID from the specified string.
	 * Returns <code>null</code> if the specified string is <code>null</code> or empty.
	 * <p>
	 * This method is an alias of the {@link #valueOf(String)} method to improve readability by avoiding static import conflicts.
	 * @param serviceId the service ID 
	 * @return the service ID or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static ServiceId serviceId(String serviceId) {
		return valueOf(serviceId);
	}
	
    /**
     * Creates a service ID from the specified string.
     * Returns <code>null</code> if the specified string is <code>null</code> or empty.
     * @param serviceId the service ID 
     * @return the service ID or <code>null</code> if the specified string is <code>null</code> or empty.
     */
	public static ServiceId valueOf(String serviceId) {
		return fromString(serviceId,ServiceId::new);
	}

	@NotNull(message="{service_id.required}")
	@Pattern(regexp=UUID_PATTERN ,message="{service_id.invalid}" )
	private String value;
	
	/**
	 * Creates a service ID.
	 * @param serviceId the service ID.
	 */
	public ServiceId(String serviceId){
		this.value = serviceId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return value;
	}


}
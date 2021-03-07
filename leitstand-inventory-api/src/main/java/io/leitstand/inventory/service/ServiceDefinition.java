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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.leitstand.commons.model.ValueObject;


/**
 * The definition of a service running on an element in the network.
 */
public class ServiceDefinition extends ValueObject {

    /**
     * Creates a builder for an immutable <code>ServiceDefinition</code> value object.
     * @return a builder for an immutable <code>ServiceDefinition</code> value object.
     */
	public static Builder newServiceDefinition(){
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>ServiceDefinition</code> value object.
	 */
	public static class Builder {
		
		private ServiceDefinition info = new ServiceDefinition();

		/**
		 * Sets the service ID.
		 * @param serviceId the service ID.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withServiceId(ServiceId serviceId){
			info.serviceId = serviceId;
			return this;
		}
		
		/**
		 * Sets the service name.
		 * @param serviceName the service name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withServiceName(ServiceName serviceName){
			info.serviceName = serviceName;
			return this;
		}

		/**
		 * Sets the service display name.
		 * @param displayName the service display name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withDisplayName(String displayName){
			info.displayName = displayName;
			return this;
		}
		
		/**
		 * Sets the service description.
		 * @param description the service description.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withDescription(String description){
			info.description= description;
			return this;
		}
		
		/**
		 * Sets the service type.
		 * @param serviceType the service type.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withServiceType(ServiceType serviceType){
			info.serviceType = serviceType;
			return this;
		}
		
		/**
		 * Creates an immutable <code>ServiceDefinition</code> value object and invalidates this builder.
		 * Subsequent invocations of the <code>build()</code> method raise an exception.
		 * @return the immutable <code>ServiceDefinition</code> value object.
		 */
		public ServiceDefinition build(){
			try{
				return info;
			} finally {
				this.info = null;
			}
		}
		
	}
	
	@Valid
	@NotNull(message="{service_id.required}")
	private ServiceId serviceId;

	@Valid
	@NotNull(message="{service_name.required}")
	private ServiceName serviceName;
	
	private String displayName;
	
	private ServiceType serviceType;
	
	private String description;
	
	/**
	 * Returns the service ID.
	 * @return the service ID.
	 */
	public ServiceId getServiceId() {
        return serviceId;
    }
	
	/**
	 * Returns the service name.
	 * @return the service name.
	 */
	public ServiceName getServiceName() {
		return serviceName;
	}
	
	/**
	 * Returns the service display name.
	 * @return the service display name.
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * Returns the service description.
	 * @return the service description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Returns the service type.
	 * @return the service type.
	 */
	public ServiceType getServiceType() {
		return serviceType;
	}
	
}

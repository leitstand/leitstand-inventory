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

import javax.json.bind.annotation.JsonbProperty;

import io.leitstand.commons.model.ValueObject;


public class ServiceDefinition extends ValueObject {

	public static Builder newServiceDefinition(){
		return new Builder();
	}
	
	public static class Builder {
		
		private ServiceDefinition info = new ServiceDefinition();
		
		public Builder withServiceName(ServiceName name){
			info.serviceName = name;
			return this;
		}

		public Builder withDisplayName(String displayName){
			info.displayName = displayName;
			return this;
		}
		

		public Builder withDescription(String description){
			info.description= description;
			return this;
		}
		

		public Builder withServiceType(ServiceType type){
			info.serviceType = type;
			return this;
		}
		
		public ServiceDefinition build(){
			try{
				return info;
			} finally {
				this.info = null;
			}
		}

		
	}
	
	@JsonbProperty("service_name")
	private ServiceName serviceName;
	
	@JsonbProperty("display_name")
	private String displayName;
	
	@JsonbProperty("service_type")
	private ServiceType serviceType;
	
	@JsonbProperty
	private String description;
	
	public ServiceName getServiceName() {
		return serviceName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getDescription() {
		return description;
	}
	
	public ServiceType getServiceType() {
		return serviceType;
	}
	
}

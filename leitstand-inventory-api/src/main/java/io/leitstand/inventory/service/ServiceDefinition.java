/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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

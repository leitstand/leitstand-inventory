/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import javax.json.JsonObject;
import javax.json.bind.annotation.JsonbProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.leitstand.commons.model.ValueObject;

public class ElementServiceSubmission extends ValueObject {
	
	public static Builder newElementServiceSubmission() {
		return new Builder();
	}
	
	public static class Builder {
		private ElementServiceSubmission service = new ElementServiceSubmission();
		
		public Builder withServiceName(ServiceName name) {
			assertNotInvalidated(getClass(), service);
			service.serviceName = name;
			return this;
		}
		
		public Builder withServiceType(ServiceType type) {
			assertNotInvalidated(getClass(), service);
			service.serviceType = type;
			return this;
		}
		
		public Builder withOperationalState(OperationalState state) {
			assertNotInvalidated(getClass(), service);
			service.operationalState = state;
			return this;
		}
		
		public Builder withParentService(ElementServiceReference parent) {
			assertNotInvalidated(getClass(), service);
			service.parentService = parent;
			return this;
		}
		
		public Builder withServiceContextType(String type) {
			assertNotInvalidated(getClass(), service);
			service.serviceContextType = type;
			return this;
		}
		
		public Builder withServiceContext(JsonObject serviceContext) {
			assertNotInvalidated(getClass(), service);
			service.serviceContext = serviceContext;
			return this;
		}
		
		public ElementServiceSubmission build() {
			try {
				assertNotInvalidated(getClass(), service);
				return service;
			} finally {
				this.service = null;
			}
		}

		public Builder withParentService(ElementServiceReference.Builder service) {
			return withParentService(service.build());
		}
	}
	
	
	@JsonbProperty("service_name")
	@Valid
	@NotNull(message="{service_name.required}")
	private ServiceName serviceName;

	@JsonbProperty("service_type")
	@NotNull(message="{service_type.required}")
	private ServiceType serviceType;
	
	@JsonbProperty("parent_service")
	@Valid
	private ElementServiceReference parentService;
	
	private JsonObject serviceContext;
	private String serviceContextType;
	
	@NotNull(message="{operational_state.required}")
	private OperationalState operationalState;
	
	
	public JsonObject getServiceContext() {
		return serviceContext;
	}
	
	public ServiceName getServiceName() {
		return serviceName;
	}
	
	public ServiceType getServiceType() {
		return serviceType;
	}
	
	public ElementServiceReference getParentService() {
		return parentService;
	}
	
	public OperationalState getOperationalState() {
		return operationalState;
	}
	
	public String getServiceContextType() {
		return serviceContextType;
	}

}

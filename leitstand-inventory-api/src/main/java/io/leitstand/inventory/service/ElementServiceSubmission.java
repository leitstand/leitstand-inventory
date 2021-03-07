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

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static io.leitstand.inventory.service.AdministrativeState.UP;

import javax.json.JsonObject;
import javax.json.bind.annotation.JsonbProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.leitstand.commons.model.ValueObject;

/**
 * A submission for storing a new element service.
 */
public class ElementServiceSubmission extends ValueObject {

    /**
     * Creates a builder for an immutable <code>ElementServiceSubmission</code> value object.
     * @return a builder for an immutable <code>ElementServiceSubmission</code> value object.
     */
	public static Builder newElementServiceSubmission() {
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>ElementServiceSubmission</code> value object.
	 */
	public static class Builder {
		
	    private ElementServiceSubmission service = new ElementServiceSubmission();
		
	    /**
	     * Sets the service name.
	     * @param serviceName the service name.
	     * @return a reference to this builder to continue object creation.
	     */
		public Builder withServiceName(ServiceName serviceName) {
			assertNotInvalidated(getClass(), service);
			service.serviceName = serviceName;
			return this;
		}
		
		/**
		 * Sets the service type.
		 * @param serviceType the service type.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withServiceType(ServiceType serviceType) {
			assertNotInvalidated(getClass(), service);
			service.serviceType = serviceType;
			return this;
		}
		
		/**
		 * Sets the service administrative state.
		 * @param state the administrative state.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withAdministrativeState(AdministrativeState state) {
		    assertNotInvalidated(getClass(), service);
		    service.administrativeState = state;
		    return this;
		}
		
	    /**
         * Sets the service operational state.
         * @param state the operational state.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withOperationalState(OperationalState state) {
			assertNotInvalidated(getClass(), service);
			service.operationalState = state;
			return this;
		}
		
		/**
		 * Sets the parent service.
		 * @param parent the parent service.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withParentService(ElementServiceReference parent) {
			assertNotInvalidated(getClass(), service);
			service.parentService = parent;
			return this;
		}
		
		/**
		 * Sets the service context type.
		 * @param type the service context type.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withServiceContextType(String type) {
			assertNotInvalidated(getClass(), service);
			service.serviceContextType = type;
			return this;
		}
		
		/**
		 * Sets the service context.
		 * @param serviceContext the service context. 
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withServiceContext(JsonObject serviceContext) {
			assertNotInvalidated(getClass(), service);
			service.serviceContext = serviceContext;
			return this;
		}
		
		/**
		 * Creates an immutable <code>ElementServiceSubmission</code> value object and invalidates this builder.
		 * Subsequent invocations of the <code>build()</code> method raise an exception.
		 * @return the immutable <code>ElementServiceSubmission</code> value object.
		 */
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
	
	private AdministrativeState administrativeState = UP;
	
	/**
	 * Returns the service context.
	 * @return the service context.
	 */
	public JsonObject getServiceContext() {
		return serviceContext;
	}
	
	/**
	 * Returns the service name.
	 * @return the service name.
	 */
	public ServiceName getServiceName() {
		return serviceName;
	}
	
	/**
	 * Returns the service type.
	 * @return the service type.
	 * @return
	 */
	public ServiceType getServiceType() {
		return serviceType;
	}
	
	/**
	 * Returns a reference to the parent service.
	 * @return a reference to the parent service.
	 */
	public ElementServiceReference getParentService() {
		return parentService;
	}
	
	/**
	 * Returns the service operational state.
	 * @return the service operational state.
	 */
	public OperationalState getOperationalState() {
		return operationalState;
	}
	
	/**
	 * Returns the service administrative state.
	 * @return the service administrative state.
	 */
	public AdministrativeState getAdministrativeState() {
        return administrativeState;
    }
	
	/**
	 * Returns the service context type.
	 * @return the service context type.
	 */
	public String getServiceContextType() {
		return serviceContextType;
	}

}

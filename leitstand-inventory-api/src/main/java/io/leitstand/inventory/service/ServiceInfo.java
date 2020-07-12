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
import static io.leitstand.commons.model.ObjectUtil.optional;

import io.leitstand.commons.model.ValueObject;

/**
 * Contains all properties of a service instance.
 */
public class ServiceInfo extends ValueObject {

	/**
	 * Returns a builder to create an immutable <code>ElementServiceData</code> instance.
	 * @return a builder to create an immutable <code>ElementServiceData</code> instance.
	 */
	public static Builder newServiceInfo(){
		return new Builder();
	}
	
	/**
	 * The builder to create an immutable <code>ElementServiceData</code> instance.
	 */
	@SuppressWarnings("unchecked")
	public static class BaseServiceBuilder<T extends ServiceInfo, B extends BaseServiceBuilder<T,B>> {
		
		protected T service;
		
		protected BaseServiceBuilder(T service){
			this.service = service;
		}
		
		public B withElementId(ElementId id){
			assertNotInvalidated(getClass(), service);
			((ServiceInfo)service).elementId = id;
			return (B) this;
		}
		
		public B withElementName(ElementName name){
			assertNotInvalidated(getClass(), service);
			((ServiceInfo)service).elementName = name;
			return (B) this;
		}
		
		public B withParent(ElementServiceReference parent){
			assertNotInvalidated(getClass(), service);
			((ServiceInfo)service).parent = parent;
			return (B) this;
		}

		public B withParent(ElementServiceReference.Builder parent){
			return withParent(optional(parent, ElementServiceReference.Builder::build));
		}

		
		/**
		 * Sets the  service name
		 * @param name the service name.
		 * @return a reference to this builder to continue object creation
		 */
		public B withServiceName(ServiceName name){
			assertNotInvalidated(getClass(), service);
			((ServiceInfo)service).serviceName = name;
			return (B) this;
		}
		
		/**
		 * Sets the service display name.
		 * @param name the display name
		 * @return a reference to this builder to continue object creation
		 */
		public B withDisplayName(String name){
			assertNotInvalidated(getClass(), service);
			((ServiceInfo)service).displayName = name;
			return (B) this;
		}
		
		/**
		 * Sets the administrative state of the service.
		 * @param state the administrative state
		 * @return a reference to this builder to continue object creation
		 */
	     public B withAdministrativeState(AdministrativeState state){
	         assertNotInvalidated(getClass(), service);
	         ((ServiceInfo)service).administrativeState = state;
	         return (B) this;
	     }
		
		
		/**
		 * Sets the operational state of the service.
		 * @param state the operational state
		 * @return a reference to this builder to continue object creation
		 */
		public B withOperationalState(OperationalState state){
			assertNotInvalidated(getClass(), service);
			((ServiceInfo)service).operationalState = state;
			return (B) this;
		}
		
		/**
		 * Sets the service type.
		 * @param serviceType the service type
		 * @return a reference to this builder to continue object creation
		 */
		public B withServiceType(ServiceType serviceType) {
			assertNotInvalidated(getClass(), service);
			((ServiceInfo)service).serviceType = serviceType;
			return (B) this;
		}
		
		public B withDescription(String description) {
			assertNotInvalidated(getClass(), service);
			((ServiceInfo)service).description = description;
			return (B) this;
		}
		
		/**
		 * Returns an immutable <code>ElementServiceData</code> instance and invalidates this builder.
		 * Any further interaction with this builder raises an exception.
		 * @return an immutable <code>ElementServiceData</code> instance.
		 */
		public T build(){
			try{
				assertNotInvalidated(getClass(), service);
				return service;
			} finally {
				this.service = null;
			}
		}

	}
	
	public static class Builder extends BaseServiceBuilder<ServiceInfo,Builder>{
		protected Builder() {
			super(new ServiceInfo());
		}

	}
	
	private ElementId elementId;
	
	private ElementName elementName;
	
	private ElementServiceReference parent;
	
	private ServiceName serviceName;
	
	private String displayName;
	
	private String description;
	
	private AdministrativeState administrativeState;
	
	private OperationalState operationalState;
	
	private ServiceType serviceType;
	
	/**
	 * Returns the service name
	 * @return the service name
	 */
	public ServiceName getServiceName() {
		return serviceName;
	}
	
	/**
	 * Returns the service display name.
	 * @return the display name.
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	public AdministrativeState getAdministrativeState() {
        return administrativeState;
    }
	
	/**
	 * Returns the operational state of the service.
	 * @return the operational state
	 */
	public OperationalState getOperationalState() {
		return operationalState;
	}
	
	/**
	 * Returns the service type.
	 * @return the service type.
	 */
	public ServiceType getServiceType() {
		return serviceType;
	}
	
	public String getDescription() {
		return description;
	}
	
	public ElementId getElementId() {
		return elementId;
	}
	
	public ElementName getElementName() {
		return elementName;
	}
	
	public ElementServiceReference getParent() {
		return parent;
	}
	
}

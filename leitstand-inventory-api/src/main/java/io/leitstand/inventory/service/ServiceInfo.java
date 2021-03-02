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
 * Metadata of a service running on an element.
 * <p>
 * The <code>ServiceInfo</code> consists of
 * <ul>
 *  <li>the element ID and element name of the element hosting the service</li>
 *  <li>an optional parent service to model service hierarchies</li>
 *  <li>the service name and the service display name</li>
 *  <li>the service type</li>
 *  <li>the administrative state and the operational state of the service and</li>
 *  <li>the service description</li>
 * </ul>
 */
public class ServiceInfo extends ValueObject {

	/**
	 * Returns a builder to create an immutable <code>ServiceInfo</code> value object.
	 * @return a builder to create an immutable <code>ServiceInfo</code> value object.
	 */
	public static Builder newServiceInfo(){
		return new Builder();
	}

	/**
     * A base builder to create an service-related value objects.
	 *
	 * @param <T> the service-related value object.
	 * @param <B> the service-related value object builder.
	 */
	@SuppressWarnings("unchecked")
	public static class BaseServiceBuilder<T extends ServiceInfo, B extends BaseServiceBuilder<T,B>> {
		
		protected T service;
		
		/**
		 * Creates a builder for a service-related value object.
		 * @param service the value object under construction.
		 */
		protected BaseServiceBuilder(T service){
			this.service = service;
		}
		
		/**
		 * Sets the element ID of the element hosting the service.
		 * @param elementId the element ID
		 * @return a reference to this builder to continue object creation.
		 */
		public B withElementId(ElementId elementId){
			assertNotInvalidated(getClass(), service);
			((ServiceInfo)service).elementId = elementId;
			return (B) this;
		}
		
		/**
         * Sets the element name of the element hosting the service.
         * @param elementName the element name
         * @return a reference to this builder to continue object creation.
         */
		public B withElementName(ElementName name){
			assertNotInvalidated(getClass(), service);
			((ServiceInfo)service).elementName = name;
			return (B) this;
		}
		
		/**
		 * Sets the parent service hosting this service.
		 * @param parent the parent service.
		 * @return a reference to this builder to continue object creation.
		 */
		public B withParent(ElementServiceReference parent){
			assertNotInvalidated(getClass(), service);
			((ServiceInfo)service).parent = parent;
			return (B) this;
		}

		/**
         * Sets the parent service hosting this service.
         * @param parent the parent service.
         * @return a reference to this builder to continue object creation.
         */
		public B withParent(ElementServiceReference.Builder parent){
			return withParent(optional(parent, ElementServiceReference.Builder::build));
		}
		
		/**
		 * Sets the service name
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
		 * @param name the display name.
		 * @return a reference to this builder to continue object creation.
		 */
		public B withDisplayName(String name){
			assertNotInvalidated(getClass(), service);
			((ServiceInfo)service).displayName = name;
			return (B) this;
		}
		
		/**
		 * Sets the administrative state of the service.
		 * @param state the administrative state.
		 * @return a reference to this builder to continue object creation.
		 */
	     public B withAdministrativeState(AdministrativeState state){
	         assertNotInvalidated(getClass(), service);
	         ((ServiceInfo)service).administrativeState = state;
	         return (B) this;
	     }
		
		
		/**
		 * Sets the operational state of the service.
		 * @param state the operational state.
		 * @return a reference to this builder to continue object creation.
		 */
		public B withOperationalState(OperationalState state){
			assertNotInvalidated(getClass(), service);
			((ServiceInfo)service).operationalState = state;
			return (B) this;
		}
		
		/**
		 * Sets the service type.
		 * @param serviceType the service type.
		 * @return a reference to this builder to continue object creation.
		 */
		public B withServiceType(ServiceType serviceType) {
			assertNotInvalidated(getClass(), service);
			((ServiceInfo)service).serviceType = serviceType;
			return (B) this;
		}
		
		/**
		 * Sets the service description.
		 * @param description the service description.
		 * @return a reference to this builder to continue object creation.
		 */
		public B withDescription(String description) {
			assertNotInvalidated(getClass(), service);
			((ServiceInfo)service).description = description;
			return (B) this;
		}
		
		/**
		 * Returns an immutable service-related value object and invalidates this builder.
		 * Subsequent invocations of the <code>build()</code> method raises an exception.
		 * @return an immutable service-related value object.
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
	
	/**
	 * A builder for an immutable <code>ServiceInfo</code> value object.
	 */
	public static class Builder extends BaseServiceBuilder<ServiceInfo,Builder>{

	    /**
	     * Creates a builder for a <code>ServiceInfo</code> value object.
	     */
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
	
	/**
	 * Returns the administrative state of the service.
	 * @return the administrative state of the service.
	 */
	public AdministrativeState getAdministrativeState() {
        return administrativeState;
    }
	
	/**
	 * Returns the operational state of the service.
	 * @return the operational state of the service.
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
	
	/**
	 * Returns the service description.
	 * @return the service description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Returns the element ID of the element hosting the service.
	 * @return the element ID of the element hosting the service.
	 */
	public ElementId getElementId() {
		return elementId;
	}
	
	/**
	 * Returns the element name of the element hosting the service.
	 * @return the element name of the element hosting the service.
	 */
	public ElementName getElementName() {
		return elementName;
	}
	
	/**
	 * Returns the reference to a parent service or <code>null</code> if no parent service exists.
	 * @return the reference to a parent service or <code>null</code> if no parent service exists.
	 */
	public ElementServiceReference getParent() {
		return parent;
	}
	
}

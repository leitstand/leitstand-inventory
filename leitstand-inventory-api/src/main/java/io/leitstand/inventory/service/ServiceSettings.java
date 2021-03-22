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

import javax.json.JsonObject;

/**
 * Settings of a service running on an element.
 * <p>
 * <code>ServiceSettings</code> extends the {@link ServiceInfo} value object by the capability to store service configurations in JSON format.
 */
public class ServiceSettings extends ServiceInfo{

    /**
     * Creates a builder for an immutable <code>ServiceSettings</code> value object.
     * @return a builder for an immutable <code>ServiceSettings</code> value object.
     */
	public static Builder newServiceSettings() {
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>ServiceSettings</code> value object.
	 */
	public static class Builder extends BaseServiceBuilder<ServiceSettings, Builder>{

	    /**
	     * Creates a builder for an immutable <code>ServiceSettings</code> value object.
	     */
	    protected Builder() {
			super(new ServiceSettings());
		}

	    /**
	     * Sets the service context type.
	     * The service context type gives a hint how to process the service context.
	     * @param type the service context type
	     * @return a reference to this builder to continue object creation.
	     */
		public Builder withServiceContextType(String type) {
			assertNotInvalidated(getClass(), service);
			service.serviceContextType = type;
			return this;
		}
		
		/**
		 * Sets the service context. The service context is a JSON object containing service-specific settings.
		 * @param context the service context
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withServiceContext(JsonObject context) {
			assertNotInvalidated(getClass(), service);
			service.serviceContext = context;
			return this;
		}

	}
	
	private String serviceContextType;
	private JsonObject serviceContext;
	
	/**
	 * Returns the service context.
	 * The service context is a JSON object with service-specific settings.
	 * @return the service context.
	 */
	public JsonObject getServiceContext() {
		return serviceContext;
	}
	
	/**
	 * Returns the service context type.
	 * The service context type gives a hint how to process the service context.
	 * @return the service context type.
	 */
	public String getServiceContextType() {
		return serviceContextType;
	}
	
}

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
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.json.bind.annotation.JsonbProperty;

/**
 * The service hierarchy of a service running on an element.
 */
public class ElementServiceStack extends BaseElementEnvelope {

    /**
     * Creates a builder for an immutable <code>ElementServiceStack</code> value object.
     * @return a builder for an immutable <code>ElementServiceStack</code> value object.
     */
	public static Builder newElementServiceStack(){
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>ElementServiceStack</code> value object.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementServiceStack, Builder> {
		
	    /**
	     * Creates a builder for an immutable <code>ElementServiceStack</code> value object.
	     */
		public Builder() {
			super(new ElementServiceStack());
		}
		
		/**
		 * Sets the services.
		 * @param services the services
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withServices(ServiceInfo... services){
			return withServices(asList(services));
		}
		
	    /**
         * Sets the services.
         * @param services the services
         * @return a reference to this builder to continue object creation.
         */
		public Builder withServices(ServiceInfo.Builder... services){
			return withServices(stream(services)
							    .map(ServiceInfo.Builder::build)
							    .collect(toList()));
		}

        /**
         * Sets the services.
         * @param services the services
         * @return a reference to this builder to continue object creation.
         */
		public Builder withServices(List<ServiceInfo> services){
			assertNotInvalidated(getClass(), object);
			object.services = services;
			return this;
		}

	}
	
	
	@JsonbProperty("stack")
	private List<ServiceInfo> services = emptyList();
	
	/**
	 * Returns the services.
	 * @return the services.
	 */
	public List<ServiceInfo> getServices() {
		return unmodifiableList(services);
	}
	
}

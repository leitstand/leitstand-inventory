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

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.LinkedList;
import java.util.List;

import javax.json.bind.annotation.JsonbProperty;

/**
 * The services running on an element.
 */
public class ElementServices extends BaseElementEnvelope{

	/**
	 * Returns a builder for an immutable <code>ElementServices</code> value object.
	 * @return a builder for an immutable <code>ElementServices</code> value object.
	 */
	public static Builder newElementServices(){
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>ElementServices</code> value object.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementServices, Builder>{
		
	    /**
	     * Creates a builder for an immutable <code>ElementServices</code> value object.
	     */
		public Builder() {
			super(new ElementServices());
		}
		
		/**
		 * Sets the running services.
		 * @param services the installed services.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withServices(ServiceInfo.Builder... services) {
			return withServices(stream(services)
							   .map(ServiceInfo.Builder::build)
							   .collect(toList()));
		}
		
		/**
		 * Sets the running services.
		 * @param services the installed services.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withServices(ServiceInfo... services) {
			return withServices(asList(services));
		}
		
		/**
		 * Sets the installed services.
		 * @param services the installed services.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withServices(List<ServiceInfo> services) {
			object.services = unmodifiableList(new LinkedList<>(services));
			return this;
		}

	}
	
	@JsonbProperty
	private List<ServiceInfo> services;
	
	/**
	 * Returns the running services.
	 * @return the list of running services
	 */
	public List<ServiceInfo> getServices() {
		return unmodifiableList(services);
	}
	
}

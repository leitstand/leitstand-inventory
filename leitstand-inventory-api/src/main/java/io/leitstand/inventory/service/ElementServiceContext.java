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

/**
 * A service running on an element.
 */
public class ElementServiceContext extends BaseElementEnvelope {

    /**
     * Creates a builder for an immutable <code>ElementServiceContext</code> value object.
     * @return a builder for an immutable <code>ElementServiceContext</code> value object.
     */
	public static Builder newElementServiceContext(){
		return new Builder();
	}

	/**
	 * A builder for an immutable <code>ElementServiceContext</code> value object.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementServiceContext, Builder>{
		
	    /**
	     * Create a builder for an immutable <code>ElementServiceContext</code> value object.
	     */
		protected Builder() {
			super(new ElementServiceContext());
		}
		
		/**
		 * Sets the service settings.
		 * @param service the service settings.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withService(ServiceSettings.Builder service) {
			return withService(service.build());
		}

	    /**
         * Sets the service settings.
         * @param service the service settings.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withService(ServiceSettings service) {
			assertNotInvalidated(getClass(), object);
			object.service = service;
			return this;
		}
		
	}
	
	
	private ServiceSettings service;
	
	/**
	 * Returns the service settings.
	 * @return the service settings.
	 */
	public ServiceSettings getService() {
		return service;
	}
	
}

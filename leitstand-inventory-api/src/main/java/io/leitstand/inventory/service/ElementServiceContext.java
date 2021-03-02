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
 * Contains all properties of a service instance.
 */
public class ElementServiceContext extends BaseElementEnvelope {

	public static Builder newElementServiceContext(){
		return new Builder();
	}

	
	public static class Builder extends BaseElementEnvelopeBuilder<ElementServiceContext, Builder>{
		
		protected Builder() {
			super(new ElementServiceContext());
		}
		
		public Builder withService(ServiceSettings.Builder service) {
			return withService(service.build());
		}
		
		public Builder withService(ServiceSettings service) {
			assertNotInvalidated(getClass(), object);
			object.service = service;
			return this;
		}
		
	}
	
	
	private ServiceSettings service;
	
	public ServiceSettings getService() {
		return service;
	}
	
}

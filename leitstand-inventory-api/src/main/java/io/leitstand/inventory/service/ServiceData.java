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

public class ServiceData extends ServiceInfo{

	public static Builder newServiceData() {
		return new Builder();
	}
	
	public static class Builder extends BaseServiceBuilder<ServiceData, Builder>{
		protected Builder() {
			super(new ServiceData());
		}

		public Builder withServiceContextType(String type) {
			assertNotInvalidated(getClass(), service);
			service.serviceContextType = type;
			return this;
		}
		
		public Builder withServiceContext(JsonObject context) {
			assertNotInvalidated(getClass(), service);
			service.serviceContext = context;
			return this;
		}

	}
	
	private String serviceContextType;
	private JsonObject serviceContext;
	
	public JsonObject getServiceContext() {
		return serviceContext;
	}
	
	public String getServiceContextType() {
		return serviceContextType;
	}
	
}

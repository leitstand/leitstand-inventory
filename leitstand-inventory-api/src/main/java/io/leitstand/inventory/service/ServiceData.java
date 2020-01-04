/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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

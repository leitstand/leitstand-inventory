/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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
		
		public Builder withService(ServiceData.Builder service) {
			return withService(service.build());
		}
		
		public Builder withService(ServiceData service) {
			assertNotInvalidated(getClass(), object);
			object.service = service;
			return this;
		}
		
	}
	
	
	private ServiceData service;
	
	public ServiceData getService() {
		return service;
	}
	
}
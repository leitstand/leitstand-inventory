/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import io.leitstand.commons.model.ValueObject;

/**
 * A reference to an existing service.
 */
public class ElementServiceReference extends ValueObject{

	public static Builder newElementServiceReference() {
		return new Builder();
	}
	
	public static class Builder {
		
		private ElementServiceReference reference = new ElementServiceReference();
		
		public Builder withElementName(ElementName elementName) {
			assertNotInvalidated(getClass(), reference);
			reference.elementName = elementName;
			return this;
		}
		
		public Builder withServiceName(ServiceName serviceName) {
			assertNotInvalidated(getClass(), reference);
			reference.serviceName = serviceName;
			return this;
		}
		
		public ElementServiceReference build() {
			try {
				assertNotInvalidated(getClass(), reference);
				return reference;
			} finally {
				this.reference = null;
			}
		}
		
	}
	
	private ElementName elementName;
	
	private ServiceName serviceName;
	
	
	/**
	 * Returns the service name.
	 * @return the service name.
	 */
	public ServiceName getServiceName() {
		return serviceName;
	}
	
	/**
	 * Returns the name of the element that runs the service.
	 * @return the name of the element that runs the service.
	 */
	public ElementName getElementName() {
		return elementName;
	}
	
}

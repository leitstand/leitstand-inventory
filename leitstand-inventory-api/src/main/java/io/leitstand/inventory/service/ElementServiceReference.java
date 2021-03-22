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

import io.leitstand.commons.model.ValueObject;

/**
 * A reference to a service running on an element.
 */
public class ElementServiceReference extends ValueObject{

    /**
     * Creates a builder for an immutable <code>ElementServiceReference</code> value object.
     * @return a builder for an immutable <code>ElementServiceReference</code> value object.
     */
	public static Builder newElementServiceReference() {
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>ElementServiceReference</code> value object.
	 */
	public static class Builder {
		
		private ElementServiceReference reference = new ElementServiceReference();

		/**
		 * Sets the element name.
		 * @param elementName the element name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withElementName(ElementName elementName) {
			assertNotInvalidated(getClass(), reference);
			reference.elementName = elementName;
			return this;
		}
		
		/**
		 * Sets the service name.
		 * @param serviceName the service name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withServiceName(ServiceName serviceName) {
			assertNotInvalidated(getClass(), reference);
			reference.serviceName = serviceName;
			return this;
		}
		
		/**
		 * Creates an immutable <code>ElementServiceReference</code> value object and invalidates this builder.
		 * Subsequent invocations of the <code>build()</code> method raise an exception.
		 * @return the immutable <code>ElementServiceReference</code> value object.
		 */
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

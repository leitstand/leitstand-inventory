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

import javax.json.bind.annotation.JsonbProperty;

import io.leitstand.commons.model.ValueObject;

/**
 * A reference to the neighbor interface of a physical interface. 
 */
public class ElementPhysicalInterfaceNeighbor extends ValueObject{

	/**
	 * Returns a builder for an immutable <code>ElementPhysicalInterfaceNeighbor</code> value object.
	 * @return a builder for an immutable <code>ElementPhysicalInterfaceNeighbor</code> value object.
	 */
	public static Builder newPhysicalInterfaceNeighbor() {
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>ElementPhysicalInterfaceNeighbor</code> value object.
	 */
	public static class Builder {
		
		private ElementPhysicalInterfaceNeighbor neighbor = new ElementPhysicalInterfaceNeighbor();
		
		/**
		 * Sets the neighbor element ID.
		 * @param elementId the neighbor element ID.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withElementId(ElementId elementId) {
            assertNotInvalidated(getClass(), neighbor);
			neighbor.elementId = elementId;
			return this;
		}

		/**
		 * Sets the neighbor element name.
		 * @param elementName the element name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withElementName(ElementName elementName) {
            assertNotInvalidated(getClass(), neighbor);
			neighbor.elementName = elementName;
			return this;
		}
		
		/**
		 * Sets the neighbor physical interface name.
		 * @param ifpName the interface name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withInterfaceName(InterfaceName ifpName) {
            assertNotInvalidated(getClass(), neighbor);
			neighbor.ifpName = ifpName;
			return this;
		}
		
		/**
		 * Creates an immutable <code>ElementPhysicalInterfaceNeighbor</code> value object and invalidated this builder.
		 * Subsequent invocation of the <code>build()</code> method raise an exception.
		 * @return the immutable <code>ElementPhysicalInterfaceNeighbor</code> value object.
		 */
		public ElementPhysicalInterfaceNeighbor build() {
			try {
			    assertNotInvalidated(getClass(), neighbor);
				return neighbor;
			} finally {
				this.neighbor = null;
			}
		}
	}
	
	
	@JsonbProperty("element_id")
	private ElementId elementId;

	@JsonbProperty("element_name")
	private ElementName elementName;
	
	@JsonbProperty("ifp_name")
	private InterfaceName ifpName;
	
	
	/**
	 * Returns the neighbor element ID.
	 * @return the neighbor element ID.
	 */
	public ElementId getElementId() {
		return elementId;
	}
	
	/**
	 * Returns the neighbor element name.
	 * @return the neighbor element name.
	 */
	public ElementName getElementName() {
		return elementName;
	}
	
	/**
	 * Returns the neighbor interface name.
	 * @return the neighbor interface name.
	 */
	public InterfaceName getInterfaceName() {
		return ifpName;
	}
	
}

/*
 * (c) RtBrick, Inc. - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import javax.json.bind.annotation.JsonbProperty;

import io.leitstand.commons.model.ValueObject;

/**
 * A reference to the neighbor interface of a physical interface. 
 */
public class ElementPhysicalInterfaceNeighbor extends ValueObject{

	/**
	 * Returns a builder to create an immutable <code>ElementPhysicalInterfaceNeighbor</code> instance.
	 * @return a builder to create an immutable <code>ElementPhysicalInterfaceNeighbor</code> instance.
	 */
	public static Builder newPhysicalInterfaceNeighbor() {
		return new Builder();
	}
	
	/**
	 * The builder to create an immutable <code>ElementPhysicalInterfaceNeighbor</code> instance.
	 */
	public static class Builder {
		
		private ElementPhysicalInterfaceNeighbor neighbor = new ElementPhysicalInterfaceNeighbor();
		
		/**
		 * Sets the neighbor element ID.
		 * @param elementId - the element ID
		 * @return a reference to this builder to continue object creation 
		 */
		public Builder withElementId(ElementId elementId) {
			neighbor.elementId = elementId;
			return this;
		}

		/**
		 * Sets the neighbor element name.
		 * @param elementName - the element name
		 * @return a reference to this builder to continue object creation 
		 */
		public Builder withElementName(ElementName elementName) {
			neighbor.elementName = elementName;
			return this;
		}
		
		/**
		 * Sets the neighbor physical interface name.
		 * @param ifpName - the interface name
		 * @return a reference to this builder to continue object creation 
		 */
		public Builder withInterfaceName(InterfaceName ifpName) {
			neighbor.ifpName = ifpName;
			return this;
		}
		
		/**
		 * Returns the immutable neighbor information of a physical interface.
		 * @return the immutable neighbor information of a physical interface.
		 */
		public ElementPhysicalInterfaceNeighbor build() {
			try {
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

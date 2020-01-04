/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import javax.json.bind.annotation.JsonbProperty;

/**
 * Contains all information of a certain physical interface of an element.
 */

public class ElementPhysicalInterface extends BaseElementEnvelope{
	
	/**
	 * Returns a builder to create an immutable <code>ElementPhysicalInterface</code> instance.
	 * @return a builder to create an immutable <code>ElementPhysicalInterface</code> instance.
	 */
	public static Builder newPhysicalInterface(){
		return new Builder();
	}
	
	/**
	 * The builder to create an immutable <code>ElementPhysicalInterface</code> instance.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementPhysicalInterface,Builder> {
		
		
		public Builder() {
			super(new ElementPhysicalInterface());
		}
		
		/**
		 * Sets the physical interface data.
		 * @param physicalInterface - the physical interface data
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withPhysicalInterface(ElementPhysicalInterfaceData physicalInterface){
			assertNotInvalidated(getClass(), object);
			object.physicalInterface = physicalInterface;
			return this;
		}
		
	}
	
	
	@JsonbProperty("physical_interface")
	private ElementPhysicalInterfaceData physicalInterface;
	
	/**
	 * Returns the properties of the physical interface.
	 * @return the properties of the physical interface.
	 */
	public ElementPhysicalInterfaceData getPhysicalInterface() {
		return physicalInterface;
	}
}

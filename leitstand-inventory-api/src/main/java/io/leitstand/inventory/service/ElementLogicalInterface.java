/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Provides all information of logical interface.
 */

public class ElementLogicalInterface extends BaseElementEnvelope{
	
	/**
	 * Returns a builder to create a new logical interface.
	 * @return a builder to create a new logical interface.
	 */
	public static Builder newLogicalInterface(){
		return new Builder();
	}
	
	/**
	 * The builder to create an immutable <code>ElementLogicalInterface</code> instance.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementLogicalInterface, Builder> {
		
		
		public Builder() {
			super(new ElementLogicalInterface());
		}
		
		/**
		 * Sets the interface data
		 * @param logicalInterface - the logical interface data
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withLogicalInterface(ElementLogicalInterfaceData.Builder logicalInterface){
			assertNotInvalidated(getClass(), object);
			object.logicalInterface = logicalInterface.build();
			return this;
		}
		
		/**
		 * Sets the interface data
		 * @param logicalInterface - the logical interface data
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withLogicalInterface(ElementLogicalInterfaceData logicalInterface){
			assertNotInvalidated(getClass(), object);
			object.logicalInterface = logicalInterface;
			return this;
		}
		
		/**
		 * Returns the immutable logical interface data.
		 * @return the immutable logical interface data.
		 */
		@Override
		public ElementLogicalInterface build(){
			try{
				assertNotInvalidated(getClass(), object);
				return object;
			} finally {
				this.object = null;
			}
		}
		
	}
	
	@Valid
	@NotNull(message="{logical_interface.required}")
	private ElementLogicalInterfaceData logicalInterface;
	
	public ElementLogicalInterfaceData getLogicalInterface() {
		return logicalInterface;
	}
}

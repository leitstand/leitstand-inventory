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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Logical interface of an element.
 */
public class ElementLogicalInterface extends BaseElementEnvelope{
	
	/**
	 * Creates a builder for an immutable <code>ElementLogicalInterface</code> value object.
	 * @return a builder for an immutable <code>ElementLogicalInterface</code> value object.
	 */
	public static Builder newLogicalInterface(){
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>ElementLogicalInterface</code> value object.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementLogicalInterface, Builder> {
		
		/**
		 * Creates a builder for an immutable <code>ElementLogicalInterface</code> value object.
		 */
		public Builder() {
			super(new ElementLogicalInterface());
		}
		
		/**
		 * Sets the logical interface.
		 * @param logicalInterface the logical interface data.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withLogicalInterface(ElementLogicalInterfaceData.Builder logicalInterface){
			assertNotInvalidated(getClass(), object);
			object.logicalInterface = logicalInterface.build();
			return this;
		}
		
		/**
		 * Sets the logical interface.
		 * @param logicalInterface the logical interface data.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withLogicalInterface(ElementLogicalInterfaceData logicalInterface){
			assertNotInvalidated(getClass(), object);
			object.logicalInterface = logicalInterface;
			return this;
		}
		
		/**
		 * Creates an immutable <code>ElementLogicalInterface</code> value object and invalidates this builder.
		 * Subsequent invocation of the <code>build()</code> method raise an exception.
		 * @return the immutable <code>ElementLogicalInterface</code> value object.
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
	
	/**
	 * Returns the logical interface settings.
 	 * @return the logical interface settings.
	 */
	public ElementLogicalInterfaceData getLogicalInterface() {
		return logicalInterface;
	}
}

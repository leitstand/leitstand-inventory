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

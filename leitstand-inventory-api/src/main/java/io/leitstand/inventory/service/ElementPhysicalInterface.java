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

/**
 * Physical interface of an element.
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

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
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import javax.json.bind.annotation.JsonbProperty;

/**
 * The physcial interfaces of an element.
 */
public class ElementPhysicalInterfaces extends BaseElementEnvelope{

	/**
	 * Creates a builder for an immutable <code>ElementPhysicalInterfaces</code> value object.
	 * @return a builder for an immutable <code>ElementPhysicalInterfaces</code> value object.
	 */
	public static Builder newPhysicalInterfaces(){
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>ElementPhysicalInterfaces</code> value object.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementPhysicalInterfaces, Builder>{
		
		/**
		 * Creates a builder for an <code>ElementPhysicalInterfaces</code> value object.
		 */
		public Builder() {
			super(new ElementPhysicalInterfaces());
		}
	
		/**
		 * Sets the physical interfaces.
		 * @param interfaces the physical interfaces.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withPhysicalInterface(ElementPhysicalInterfaceData.Builder... interfaces){
			return withPhysicalInterfaces(stream(interfaces)
										 .map(ElementPhysicalInterfaceData.Builder::build)
										 .collect(toList()));
		}
		
		
		/**
		 * Sets the physical interfaces.
		 * @param interfaces the physical interfaces.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withPhysicalInterface(ElementPhysicalInterfaceData... interfaces){
			assertNotInvalidated(getClass(), object);
			return withPhysicalInterfaces(asList(interfaces));
		}
		
		/**
		 * Sets the physical interfaces.
		 * @param interfaces the physical interfaces.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withPhysicalInterfaces(List<ElementPhysicalInterfaceData> physicalInterface){
			assertNotInvalidated(getClass(), object);
			object.physicalInterfaces = unmodifiableList(new ArrayList<>(physicalInterface));
			return this;
		}
		
	}
	
	
	@JsonbProperty("physical_interfaces")
	private List<ElementPhysicalInterfaceData> physicalInterfaces;
	
	/**
	 * Returns the list of physical interfaces.
	 * @return the list of physical interfaces.
	 */
	public List<ElementPhysicalInterfaceData> getPhysicalInterfaces() {
		return physicalInterfaces;
	}
}

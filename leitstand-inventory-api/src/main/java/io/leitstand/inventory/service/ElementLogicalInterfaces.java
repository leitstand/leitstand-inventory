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
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import javax.json.bind.annotation.JsonbProperty;


/**
 * Logical interfaces of an element.
 */
public class ElementLogicalInterfaces extends BaseElementEnvelope{

	/**
	 * Returns a builder for an immutable <code>ElementLogicalInterfaces</code> value object.
	 * @return a builder for an immutable <code>ElementLogicalInterface</code> value object.
	 */
	public static Builder newLogicalInterfaces(){
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>ElementLogicalInterfaces</code> value object.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementLogicalInterfaces,Builder>{
		
	    /**
	     * Creates a builder for an immutable <code>ElementLogicalInterfaces</code> value object.
	     */
		public Builder() {
			super(new ElementLogicalInterfaces());
		}
		
		/**
		 * Sets the logical interfaces.
		 * @param interfaces the logical interfaces
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withLogicalInterfaces(ElementLogicalInterfaceData.Builder... interfaces){
			return withLogicalInterfaces(stream(interfaces)
										 .map(ElementLogicalInterfaceData.Builder::build)
										 .collect(toList()));
		}

		/**
		 * Sets the logical interfaces.
		 * @param interfaces the logical interfaces
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withLogicalInterfaces(List<ElementLogicalInterfaceData> logicalInterface){
			assertNotInvalidated(getClass(), object);
			object.logicalInterfaces = unmodifiableList(new ArrayList<>(logicalInterface));
			return this;
		}
		
	}
	
	@JsonbProperty("logical_interfaces")
	private List<ElementLogicalInterfaceData> logicalInterfaces = emptyList();
	
	/**
	 * Returns the list of logical interfaces.
	 * @return the list of logical interfaces
	 */
	public List<ElementLogicalInterfaceData> getLogicalInterfaces() {
		return logicalInterfaces;
	}
}

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
import static java.util.Collections.unmodifiableList;

import java.util.LinkedList;
import java.util.List;

/**
 * Element settings of all elements in an element group.
 */
public class ElementGroupElements extends BaseElementGroupEnvelope {
	
    /**
     * Returns a builder for the <code>ElementGroupElements</code>.
     * @return a builder to create the <code>ElementGroupElements</code>.
     */
	public static Builder newElementGroupElements(){
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>ElementGroupElements</code> instance.
	 */
	public static class Builder extends BaseElementGroupEnvelopeBuilder<ElementGroupElements, Builder>{
		
	    /**
	     * Creates a new <code>ElementGroupElements</Code> builder.
	     */
		protected Builder(){
			super(new ElementGroupElements());
		}
		
		/** 
		 * Sets the group description
		 * @param description the group description
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withDescription(String description){
			assertNotInvalidated(getClass(), object);
			object.description = description;
			return this;
		}
		
		/**
		 * Add the settings of all elements in this group.
		 * @param elements the element settings
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withElements(List<ElementSettings> elements){
			assertNotInvalidated(getClass(), object);
			object.elements = unmodifiableList(new LinkedList<>(elements));
			return this;
		}

	}
	
	private String description;
	private List<ElementSettings> elements;

	/**
	 * Returns the group description.
	 * @return the group description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Returns the element settings of all elements in this element group.
	 * @return the element settings of all elements in this element group.
	 */
	public List<ElementSettings> getElements() {
		return unmodifiableList(elements);
	}

}

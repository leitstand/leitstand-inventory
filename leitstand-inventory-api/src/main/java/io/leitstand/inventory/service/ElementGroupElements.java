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


public class ElementGroupElements extends BaseElementGroupEnvelope {
	
	public static Builder newElementGroupElements(){
		return new Builder();
	}
	
	public static class Builder extends BaseElementGroupEnvelopeBuilder<ElementGroupElements, Builder>{
		
		protected Builder(){
			super(new ElementGroupElements());
		}
		
		public Builder withDescription(String description){
			assertNotInvalidated(getClass(), object);
			object.description = description;
			return this;
		}
		
		public Builder withElements(List<ElementSettings> elements){
			assertNotInvalidated(getClass(), object);
			object.elements = unmodifiableList(new LinkedList<>(elements));
			return this;
		}

	}
	
	private String description;
	private List<ElementSettings> elements;

	public String getDescription() {
		return description;
	}
	
	public List<ElementSettings> getElements() {
		return elements;
	}

}

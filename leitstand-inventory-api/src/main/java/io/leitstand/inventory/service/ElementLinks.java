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

import javax.validation.Valid;

/**
 * All links of an element.
 */
public class ElementLinks extends BaseElementEnvelope {
	
	/**
	 * Returns a builder to create an immutable <code>ElementLinks</code> value object.
	 * @return a builder to create an immutable <code>ElementLinks</code> value object.
	 */
	public static Builder newElementLinks(){
		return new Builder();
	}
	
	/**
	 * A builder to create an immutable <code>ElementLinks</code> value object.
	 */
	public static final class Builder extends BaseElementEnvelopeBuilder<ElementLinks, Builder>{
		
	    /**
	     * Creates a builder for an immutable <code>ElementLinks</code> value object.
	     */
		public Builder() {
			super(new ElementLinks());
		}
		
		/**
		 * Sets the element links.
		 * @param links the element links.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withLinks(ElementLinkData.Builder... links){
			return withLinks(stream(links)
							 .map(ElementLinkData.Builder::build)
							 .collect(toList()));
		}
		
		/**
		 * Sets the element links.
		 * @param links the links of this element.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withLinks(ElementLinkData... links){
			return withLinks(asList(links));
		}
		
		/**
         * Sets the element links.
         * @param links the links of this element.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withLinks(List<ElementLinkData> links){
			assertNotInvalidated(getClass(), object);
			object.links = unmodifiableList(new ArrayList<ElementLinkData>(links));
			return this;
		}
		
	}

	@Valid
	private List<ElementLinkData> links;
	
	/**
	 * Returns an unmodifiable list of all element links.
	 * @return the element links.
	 */
	public List<ElementLinkData> getLinks() {
		return unmodifiableList(links);
	}
	
}

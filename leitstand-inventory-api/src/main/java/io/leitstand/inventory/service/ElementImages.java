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

import java.util.LinkedList;
import java.util.List;

import javax.json.bind.annotation.JsonbProperty;
import javax.validation.Valid;

/**
 * A summary of all images element on a certain element.
 */

public class ElementImages extends BaseElementEnvelope{

	/**
	 * Returns a builder to create a summary of element images.
	 * @return a builder to create a summary of element images.
	 */
	public static Builder newElementImages(){
		return new Builder();
	}
	
	/**
	 * The builder to create an immutable <code>ElementInstalledImages</code> instance.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementImages, Builder>{
		
		public Builder() {
			super(new ElementImages());
		}
		
		/**
		 * Sets the list of element images.
		 * @param images the element images.
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withElementImages(ElementImageData.Builder... images) {
			return withElementImages(stream(images)
									 .map(ElementImageData.Builder::build)
									 .collect(toList()));
		}
		
		/**
		 * Sets the list of element images.
		 * @param images - the element images.
		 * @return a reference to this builder to continue with object creation
		 */		
		public Builder withElementImages(ElementImageData... images) {
			return withElementImages(asList(images));
		}
		
		/**
		 * Sets the list of element images.
		 * @param images - the element images.
		 * @return a reference to this builder to continue with object creation
		 */		
		public Builder withElementImages(List<ElementImageData> images) {
			assertNotInvalidated(getClass(), object);
			object.images = unmodifiableList(new LinkedList<>(images));
			return this;
		}
		
		/**
		 * Returns an immutable list of element images.
		 * @return an immutable list of element images.
		 */
		@Override
		public ElementImages build(){
			try{
				assertNotInvalidated(getClass(), object);
				return object;
			} finally {
				this.object = null;
			}
		}

	}
	
	@JsonbProperty
	@Valid
	private List<ElementImageData> images;
	
	/**
	 * Returns the information of the element image.
	 * @return the information of the element image.
	 */
	public List<ElementImageData> getImages() {
		return images;
	}
	
}

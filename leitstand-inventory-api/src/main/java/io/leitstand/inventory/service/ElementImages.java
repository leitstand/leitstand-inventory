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
 * A summary of all images installed on an element or eligible for deployment.
 */
public class ElementImages extends BaseElementEnvelope{

	/**
	 * Creates a builder for an immutable <code>ElementImages</code> value object.
	 * @return a builder for an immutable <code>ElementImages</code> value object.
	 */
	public static Builder newElementImages(){
		return new Builder();
	}
	
	/**
	 * A builder to create an immutable <code>ElementImages</code> value object.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementImages, Builder>{
		
	    /**
	     * Creates a builder for an immutable <code>ElementImages</code> value object.
	     */
		public Builder() {
			super(new ElementImages());
		}
		
		/**
		 * Sets the element images.
		 * @param images the element images.
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withElementImages(ElementImageData.Builder... images) {
			return withElementImages(stream(images)
									 .map(ElementImageData.Builder::build)
									 .collect(toList()));
		}
		
		/**
		 * Sets the element images.
		 * @param images he element images.
		 * @return a reference to this builder to continue with object creation
		 */		
		public Builder withElementImages(ElementImageData... images) {
			return withElementImages(asList(images));
		}
		
		/**
		 * Sets the element images.
		 * @param images the element images.
		 * @return a reference to this builder to continue with object creation
		 */		
		public Builder withElementImages(List<ElementImageData> images) {
			assertNotInvalidated(getClass(), object);
			object.images = unmodifiableList(new LinkedList<>(images));
			return this;
		}
		
		/**
		 * Returns an immutable <code>ElementImages</code> value object and invalidates this builder.
		 * Subsequent invocations of the <code>build()</code> method raises an exception.
		 * @return an immutable <code>ElementImages</code> value object.
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
	 * Returns the element images.
	 * @return the element images
	 */
	public List<ElementImageData> getImages() {
		return images;
	}
	
}

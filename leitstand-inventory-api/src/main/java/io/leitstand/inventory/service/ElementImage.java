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
 * Details of an image installed on an element or eligible for deployment.
 */
public class ElementImage extends BaseElementEnvelope{

	/**
	 * Returns a builder to create an immutable <code>ElementImage</code> value object.
	 * @return a builder to create an immutable <code>ElementImage</code> value object.
	 */
	public static Builder newElementImage(){
		return new Builder();
	}
	
	/**
	 * A builder to create an immutable <code>ElementImage</code> value object.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementImage, Builder>{
		
		public Builder() {
			super(new ElementImage());
		}
		
		/**
		 * Sets the image information
		 * @param image - the image information.
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withImage(ElementImageData.Builder image){
			return withImage(image.build());
		}
		
		/**
		 * Sets the image information
		 * @param image - the image information.
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withImage(ElementImageData image){
			assertNotInvalidated(getClass(), object);
			object.image = image;
			return this;
		}

		/**
		 * Returns an immutable <code>ElementInstalledImage</code> instance and 
		 * invalidates this builde. Any further interaction with this builder raises an exception.
		 * @return
		 */
		@Override
		public ElementImage build(){
			try{
				assertNotInvalidated(getClass(), object);
				return object;
			} finally {
				this.object = null;
			}
		}

	}
	
	@Valid
	@NotNull(message="{image.required}")
	private ElementImageData image;

	/**
	 * Returns the element image information
	 * @return the element image information
	 */
	public ElementImageData getImage() {
		return image;
	}
	
}

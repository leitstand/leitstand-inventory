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
 * A summary of all images installed on a certain element.
 */

public class ElementInstalledImages extends BaseElementEnvelope{

	/**
	 * Returns a builder to create a summary of installed images.
	 * @return a builder to create a summary of installed images.
	 */
	public static Builder newElementInstalledImages(){
		return new Builder();
	}
	
	/**
	 * The builder to create an immutable <code>ElementInstalledImages</code> instance.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementInstalledImages, Builder>{
		
		public Builder() {
			super(new ElementInstalledImages());
		}
		
		/**
		 * Sets the list of installed images.
		 * @param images - the installed images.
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withInstalledImages(ElementInstalledImageData.Builder... images) {
			return withInstalledImages(stream(images)
									  .map(ElementInstalledImageData.Builder::build)
									  .collect(toList()));
		}
		
		/**
		 * Sets the list of installed images.
		 * @param images - the installed images.
		 * @return a reference to this builder to continue with object creation
		 */		
		public Builder withInstalledImages(ElementInstalledImageData... images) {
			return withInstalledImages(asList(images));
		}
		
		/**
		 * Sets the list of installed images.
		 * @param images - the installed images.
		 * @return a reference to this builder to continue with object creation
		 */		
		public Builder withInstalledImages(List<ElementInstalledImageData> images) {
			assertNotInvalidated(getClass(), object);
			object.images = unmodifiableList(new LinkedList<>(images));
			return this;
		}
		
		/**
		 * Returns an immutable list of installed images.
		 * @return an immutable list of installed images.
		 */
		@Override
		public ElementInstalledImages build(){
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
	private List<ElementInstalledImageData> images;
	
	/**
	 * Returns the information of the installed image.
	 * @return the information of the installed image.
	 */
	public List<ElementInstalledImageData> getImages() {
		return images;
	}
	
}

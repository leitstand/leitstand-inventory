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
import static io.leitstand.inventory.service.ElementImageState.ACTIVE;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.leitstand.commons.model.ValueObject;

/**
 * A reference to the installed image or an image eligible for deployment on the element.
 */
public class ElementImageReference extends ValueObject {
	
    /**
     * Creates a builder for an immutable <code>ElementImageReference</code> value object.
     * @return a builder for an immutable <code>ElementImageReference</code> value object.
     */
	public static Builder newElementImageReference() {
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>ElementImageReference</code> value object.
	 */
	public static class Builder {
		
		private ElementImageReference ref = new ElementImageReference();

		/**
		 * Sets the image ID.
		 * @param imageId the image ID.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withImageId(ImageId imageId) {
			assertNotInvalidated(getClass(),ref);
			ref.imageId = imageId;
			return this;
		}
		
	    /**
         * Sets the image type.
         * @param imageType the image type.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withImageType(ImageType imageType) {
			assertNotInvalidated(getClass(), ref);
			ref.imageType = imageType;
			return this;
		}
		
	    /**
         * Sets the image name.
         * @param imageName the image name.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withImageName(ImageName imageName) {
			assertNotInvalidated(getClass(), ref);
			ref.imageName = imageName;
			return this;
		}
		
        /**
         * Sets the element image lifecycle state.
         * @param imageState the element image lifecycle state.
         * @return a reference to this builder to continue object creation.
         */		
		public Builder withElementImageState(ElementImageState imageState) {
			assertNotInvalidated(getClass(), ref);
			ref.elementImageState = imageState;
			return this;
		}
		
		/**
		 * Sets the image version.
		 * @param imageVersion the image version.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withImageVersion(Version imageVersion) {
			assertNotInvalidated(getClass(), ref);
			ref.imageVersion = imageVersion;
			return this;
		}
		
		/**
		 * Creates an immutable <code>ElementImageReference</code> value object and invalidated this builder. 
		 * Subsequent invocations of the <code>build()</code> method raise an exception.
		 * @return the immutable <code>ElementImageReference</code> value object.
		 */
		public ElementImageReference build() {
			try {
				assertNotInvalidated(getClass(), ref);
				return ref;
			} finally {
				this.ref = null;
			}
		}
		
	}
	
	@Valid
	@NotNull(message="{image_id.required}")
	private ImageId imageId;

	@Valid
	@NotNull(message="{image_type.required}")
	private ImageType imageType;
	
	private ImageName imageName;

	@Valid
	@NotNull(message="{image_version.required}")
	private Version imageVersion;
	
	private ElementImageState elementImageState;
	
	/** 
	 * Returns the image type.
	 * @return the image type.
	 */
	public ImageType getImageType() {
		return imageType;
	}

	/**
	 * Returns the image name.
	 * @return the image name.
	 */
	public ImageName getImageName() {
		return imageName;
	}

	/**
	 * Returns the image version.
	 * @return the image version.
	 */
	public Version getImageVersion() {
		return imageVersion;
	}
	
	/**
	 * Returns the element image lifecycle state.
	 * @return the element image lifecycle state.
	 */
	public ElementImageState getElementImageState() {
		return elementImageState;
	}
	
	/**
	 * Returns whether this image is currently active or not.
	 * @return <code>true</code> if the image is currently active.
	 */
	public boolean isActive() {
		return elementImageState == ACTIVE;
	}

	/**
	 * Returns the image ID.
	 * @return the image ID.
	 */
	public ImageId getImageId() {
		return imageId;
	}
	
}

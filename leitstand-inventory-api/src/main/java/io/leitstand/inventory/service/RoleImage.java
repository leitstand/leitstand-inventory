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

import io.leitstand.commons.model.CompositeValue;

/**
 * A reference to a software image eligible for deployment on the given element role.
 */
public class RoleImage extends CompositeValue{

    /**
     * Creates a builder for an immutable <code>RoleImage</code> value object.
     * @return a builder for an immutable <code>RoleImage</code> value object.
     */
	public static Builder newRoleImage() {
		return new Builder();
	}
	
	/**
     * A builder for an immutable <code>RoleImage</code> value object.	 
     */
	public static class Builder {
		
		private RoleImage image = new RoleImage();
		
		/**
		 * Sets the image UUID.
		 * @param imageId the image ID
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withImageId(ImageId imageId) {
		    assertNotInvalidated(getClass(),image);
		    image.imageId = imageId;
		    return this;
		}
		
		/**
		 * Sets the image name.
 		 * @param imageName the image name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withImageName(ImageName imageName) {
			assertNotInvalidated(getClass(), image);
			image.imageName = imageName;
			return this;
		}

		/**
		 * Sets the image type.
		 * @param imageType the image type.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withImageType(ImageType imageType) {
			assertNotInvalidated(getClass(), image);
			image.imageType = imageType;
			return this;
		}
		
		/**
		 * Sets the image version.
		 * @param imageVersion the image version
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withImageVersion(Version imageVersion) {
		    assertNotInvalidated(getClass(),image);
		    image.imageVersion = imageVersion;
		    return this;
		}
		
		/**
		 * Sets the supported platform chipset name.
		 * @param platformChipset the platform chipset name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withPlatformChipset(PlatformChipsetName platformChipset) {
		    assertNotInvalidated(getClass(), image);
		    image.platformChipset = platformChipset;
		    return this;
		}
		
		/**
		 * Creates an immutable <code>RoleImage</code> value object and invalidates this builder.
		 * Subsequent invocations of the <code>build()</code> methods raises an exception.
		 * @return the immutable <code>RoleImage</code> value object.
		 */
		public RoleImage build() {
			try {
				assertNotInvalidated(getClass(), image);
				return image;
			} finally {
				this.image = null;
			}
		}
		
	}
	
	private ImageId imageId;
	private ImageName imageName;
	private ImageType imageType;
	private Version imageVersion;
	private PlatformChipsetName platformChipset;
	
	/**
	 * Returns the image ID.
	 * @return the image ID.
	 */
	public ImageId getImageId() {
        return imageId;
    }
	
	/**
	 * Returns the image name.
	 * @return the image name.
	 */
	public ImageName getImageName() {
		return imageName;
	}
	
	/**
	 * Returns the image type.
	 * @return the image type.
	 */
	public ImageType getImageType() {
		return imageType;
	}
	
	/**
	 * Returns the image version.
	 * @return the image version.
	 */
	public Version getImageVersion() {
        return imageVersion;
    }
	
	/**
	 * Returns the supported platform chipset.
	 * @return the supported platform chipset.
	 */
	public PlatformChipsetName getPlatformChipset() {
        return platformChipset;
    }
	
}

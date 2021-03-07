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

import java.util.Date;
import java.util.List;

/**
 * An image reference.
 * <p>
 * The <code>ImageReference</code> includes
 * <ul>
 *  <li>the image ID,</li>
 *  <li>the image type,</li>
 *  <li>the image name,</li>
 *  <li>the image lifecycle state,</li>
 *  <li>the image version,</li>
 *  <li>the image build date,</li>
 *  <li>the supported chipset and </i>
 *  <li>the element roles the image is built for.</li>
 * </ul>
 */
public class ImageReference {

    /**
     * Creates a builder for a <code>ImageReference</code> value object.
     * @return a builder for a <code>ImageReference</code> value object.
     */
	public static Builder newImageReference(){
		return new Builder();
	}
	
	/**
	 * A builder for a <code>ImageReference</code> value object.
	 */
	public static class Builder{
		
		private ImageReference image = new ImageReference();
		
		/**
		 * Sets the image ID.
		 * @param imageId the image ID.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withImageId(ImageId imageId){
			assertNotInvalidated(getClass(), image);
			image.imageId = imageId;
			return this;
		}
		
	    /**
         * Sets the element roles supported by this image.
         * @param roles the element roles.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withElementRoles(List<ElementRoleName> roles){
			assertNotInvalidated(getClass(), image);
			image.elementRoles = roles;
			return this;
		}
		
		/**
		 * Sets the element name the image is bound to.
		 * @param elementName the element name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withElementName(ElementName elementName){
			assertNotInvalidated(getClass(), image);
			image.elementName = elementName;
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
		public Builder withImageType(ImageType imageType){
			assertNotInvalidated(getClass(), image);
			image.imageType = imageType;
			return this;
		}
		
	    /**
         * Sets the image lifecycle state.
         * @param imageState the image lifecycle state.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withImageState(ImageState imageState) {
			assertNotInvalidated(getClass(), image);
			image.imageState = imageState;
			return this;
		}
		
	    /**
         * Sets the image version.
         * @param imageVersion the image name.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withImageVersion(Version imageVersion){
			assertNotInvalidated(getClass(), image);
			image.imageVersion = imageVersion;
			return this;
		}
		
		
	    /**
         * Sets the platform chipset name.
         * @param platformChipset the platform chipset.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withPlatformChipset(PlatformChipsetName platformChipset){
			assertNotInvalidated(getClass(), image);
			image.platformChipset = platformChipset;
			return this;			
		}
		
	    /**
         * Sets the image build date.
         * @param buildDate the build date.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withBuildDate(Date buildDate){
			assertNotInvalidated(getClass(), image);
			if(buildDate != null) {
				image.buildDate =  new Date(buildDate.getTime());
			}
			return this;
		}
		
		/**
		 * Creates an immutable <code>ImageReference</code> value object and invalidates this builder.
		 * Subsequence invocation of the <code>build()</code> method raise an exception.
		 * @return the immutable <code>ImageReference</code> value object.
		 */
		public ImageReference build(){
			try{
				assertNotInvalidated(getClass(), image);
				return this.image;
			} finally {
				this.image = null;
			}
		}
	}

	private ImageId imageId;
	
	private List<ElementRoleName> elementRoles;
	
	private ElementName elementName;

	private ImageName imageName;
	
	private ImageType imageType;
	
	private ImageState imageState;
	
	private Version imageVersion;
	
	private Date buildDate;
	
	private PlatformChipsetName platformChipset;
	
	/**
	 * Returns the image ID.
	 * @return the image ID.
	 */
	public ImageId getImageId() {
		return imageId;
	}
	
	/**
	 * Returns the image version.
	 * @return the image version.
	 */
	public Version getImageVersion() {
		return imageVersion;
	}
	
	/**
	 * Returns the supported element roles.
	 * @return the supported element roles.
	 */
	public List<ElementRoleName> getElementRoles() {
		return unmodifiableList(elementRoles);
	}
	
	/**
	 * Returns the element name this image is bound to or <code>null</code> if this image is not bound to an element.
	 * @return the element name this image is bound to or <code>null</code> if this image is not bound to an element.
	 */
	public ElementName getElementName(){
		return elementName;
	}
	
	/**
	 * Returns the build date of the image.
	 * @return the build date of the image.
	 */
	public Date getBuildDate() {
		return buildDate;
	}

	/**
	 * Returns the supported platform chipset.
	 * @return the supported platform chipset.
	 */
	public PlatformChipsetName getPlatformChipset() {
		return platformChipset;
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
	 * Returns the image lifecycle state.
	 * @return the image lifecycle state.
	 */
	public ImageState getImageState() {
		return imageState;
	}
	
}

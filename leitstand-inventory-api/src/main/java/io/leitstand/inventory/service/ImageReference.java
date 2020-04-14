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

import java.util.Date;


public class ImageReference {

	public static Builder newImageReference(){
		return new Builder();
	}
	
	public static class Builder{
		
		private ImageReference image = new ImageReference();
		
		public Builder withImageId(ImageId id){
			assertNotInvalidated(getClass(), image);
			image.imageId = id;
			return this;
		}
		
		public Builder withElementRole(ElementRoleName role){
			assertNotInvalidated(getClass(), image);
			image.elementRole = role;
			return this;
		}
		
		public Builder withElementName(ElementName name){
			assertNotInvalidated(getClass(), image);
			image.elementName = name;
			return this;
		}
		
		public Builder withPlatformId(PlatformId platformId) {
			assertNotInvalidated(getClass(), image);
			image.platformId = platformId;
			return this;
		}
		
		public Builder withPlatformName(PlatformName platformName) {
			assertNotInvalidated(getClass(), image);
			image.platformName = platformName;
			return this;
		}
		
		public Builder withImageName(ImageName imageName) {
			assertNotInvalidated(getClass(), image);
			image.imageName = imageName;
			return this;
		}
		
		public Builder withImageType(ImageType type){
			assertNotInvalidated(getClass(), image);
			image.imageType = type;
			return this;
		}
		
		public Builder withImageState(ImageState state) {
			assertNotInvalidated(getClass(), image);
			image.imageState = state;
			return this;
		}
		
		public Builder withImageVersion(Version version){
			assertNotInvalidated(getClass(), image);
			image.imageVersion = version;
			return this;
		}
		
		public Builder withBuildDate(Date date){
			assertNotInvalidated(getClass(), image);
			if(date != null) {
				image.buildDate =  new Date(date.getTime());
			}
			return this;
		}
		
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
	
	private ElementRoleName elementRole;
	
	private ElementName elementName;

	private PlatformId platformId;
	private PlatformName platformName;
	
	private ImageName imageName;
	
	private ImageType imageType;
	
	private ImageState imageState;
	
	private Version imageVersion;
	
	private Date buildDate;
	
	public ImageId getImageId() {
		return imageId;
	}
	
	public Version getRevision() {
		return imageVersion;
	}
	
	public ElementRoleName getElementRole() {
		return elementRole;
	}
	
	public ElementName getElementName(){
		return elementName;
	}
	
	public Date getBuildDate() {
		return buildDate;
	}

	public PlatformId getPlatformId() {
		return platformId;
	}
	
	public PlatformName getPlatformName() {
		return platformName;
	}
	
	public ImageName getImageName() {
		return imageName;
	}
	
	public ImageType getImageType() {
		return imageType;
	}
	
	public ImageState getImageState() {
		return imageState;
	}
	
	
}

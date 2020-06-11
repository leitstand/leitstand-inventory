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
		
		public Builder withElementRoles(List<ElementRoleName> roles){
			assertNotInvalidated(getClass(), image);
			image.elementRoles = roles;
			return this;
		}
		
		public Builder withElementName(ElementName name){
			assertNotInvalidated(getClass(), image);
			image.elementName = name;
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
		
		public Builder withPlatformChipset(PlatformChipsetName chipset){
			assertNotInvalidated(getClass(), image);
			image.platformChipset = chipset;
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
	
	private List<ElementRoleName> elementRoles;
	
	private ElementName elementName;

	private ImageName imageName;
	
	private ImageType imageType;
	
	private ImageState imageState;
	
	private Version imageVersion;
	
	private Date buildDate;
	
	private PlatformChipsetName platformChipset;
	
	public ImageId getImageId() {
		return imageId;
	}
	
	public Version getRevision() {
		return imageVersion;
	}
	
	public List<ElementRoleName> getElementRole() {
		return unmodifiableList(elementRoles);
	}
	
	public ElementName getElementName(){
		return elementName;
	}
	
	public Date getBuildDate() {
		return buildDate;
	}

	public PlatformChipsetName getPlatformChipset() {
		return platformChipset;
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

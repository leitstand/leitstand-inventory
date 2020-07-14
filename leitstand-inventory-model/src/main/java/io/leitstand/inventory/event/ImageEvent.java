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
package io.leitstand.inventory.event;

import java.util.List;
import java.util.Map;

import io.leitstand.commons.model.ValueObject;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ImageId;
import io.leitstand.inventory.service.ImageName;
import io.leitstand.inventory.service.ImageState;
import io.leitstand.inventory.service.ImageType;
import io.leitstand.inventory.service.Version;

public class ImageEvent extends ValueObject{
	
	public static class ImageEventBuilder<T extends ImageEvent,B extends ImageEventBuilder<T,B>>{
		
		protected T instance;
		
		protected ImageEventBuilder(T instance) {
			this.instance = instance;
		}
		
		public B withImageId(ImageId imageId) {
			((ImageEvent)instance).imageId = imageId;
			return (B) this;
		}
		
		public B withImageName(ImageName imageName) {
			((ImageEvent)instance).imageName = imageName;
			return (B) this;
		}
		
		public B withImageType(ImageType imageType) {
			((ImageEvent)instance).imageType = imageType;
			return (B) this;
		}
		
		public B withImageState(ImageState imageState) {
			((ImageEvent)instance).imageState = imageState;
			return (B) this;
		}
		
		public B withElementRoles(List<ElementRoleName> elementRoles) {
			((ImageEvent)instance).elementRoles = elementRoles;
			return (B) this;
		}
		
		public B withImageVersion(Version version) {
			((ImageEvent)instance).imageVersion = version;
			return (B) this;
		}
		
		public B withChecksums(Map<String,String> checksums) {
			((ImageEvent)instance).checksums = checksums;
			return (B) this;
		}
		
		public B withOrganization(String organization) {
			((ImageEvent)instance).organization = organization;
			return (B) this;
		}

		public B withCategory(String category) {
			((ImageEvent)instance).category = category;
			return (B) this;
		}
		
		public B withImageExtension(String extension) {
			((ImageEvent)instance).imageExtension = extension;
			return (B) this;
		}
		
		public T build() {
			try {
				return instance;
			} finally {
				this.instance = null;
			}
		}
		
	}

	private ImageId imageId;
	private ImageName imageName;
	private ImageType imageType;
	private ImageState imageState;
	private List<ElementRoleName> elementRoles;
	private Version imageVersion;
	private String organization;
	private String imageExtension;
	private String category;
	private Map<String,String> checksums;
	
	public ImageId getImageId() {
		return imageId;
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
	
	public List<ElementRoleName> getElementRoles() {
		return elementRoles;
	}
	
	public Version getImageVersion() {
		return imageVersion;
	}
	
	public Map<String,String> getChecksums() {
		return checksums;
	}
	
	public String getOrganization() {
		return organization;
	}
	
	public String getImageExtension() {
		return imageExtension;
	}
	
	public String getCategory() {
		return category;
	}
	
}

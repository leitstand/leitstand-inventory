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

import io.leitstand.commons.model.ValueObject;

/**
 * A reference to an installed image.
 */
public class ElementInstalledImageReference extends ValueObject {
	
	public static Builder newElementInstalledImageReference() {
		return new Builder();
	}
	
	public static class Builder {
		
		private ElementInstalledImageReference ref = new ElementInstalledImageReference();
		
		public Builder withImageType(ImageType type) {
			assertNotInvalidated(getClass(), ref);
			ref.imageType = type;
			return this;
		}
		
		public Builder withImageName(ImageName name) {
			assertNotInvalidated(getClass(), ref);
			ref.imageName = name;
			return this;
		}
		
		public Builder withElementImageState(ElementImageState state) {
			assertNotInvalidated(getClass(), ref);
			ref.elementImageState = state;
			return this;
		}
		
		public Builder withImageVersion(Version version) {
			assertNotInvalidated(getClass(), ref);
			ref.imageVersion = version;
			return this;
		}
		
		public ElementInstalledImageReference build() {
			try {
				assertNotInvalidated(getClass(), ref);
				return ref;
			} finally {
				this.ref = null;
			}
		}
		
	}
	
	
	private ImageType imageType;
	
	private ImageName imageName;
	
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
	 * Returns the image imageVersion.
	 * @return the image imageVersion.
	 */
	public Version getImageVersion() {
		return imageVersion;
	}
	
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

}

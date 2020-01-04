/*
 * (c) RtBrick, Inc. - All rights reserved, 2015 - 2019  
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static io.leitstand.inventory.service.ElementImageState.ACTIVE;
import static io.leitstand.inventory.service.ElementImageState.CACHED;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.ValueObject;
import io.leitstand.inventory.jsonb.VersionAdapter;

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
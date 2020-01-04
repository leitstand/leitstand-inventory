/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import io.leitstand.commons.model.CompositeValue;

public class RoleImage extends CompositeValue{

	public static Builder newRoleImage() {
		return new Builder();
	}
	
	public static class Builder {
		
		private RoleImage image = new RoleImage();
		
		public Builder withImageName(ImageName imageName) {
			assertNotInvalidated(getClass(), imageName);
			image.imageName = imageName;
			return this;
		}

		public Builder withImageType(ImageType imageType) {
			assertNotInvalidated(getClass(), imageType);
			image.imageType = imageType;
			return this;
		}
		
		public RoleImage build() {
			try {
				assertNotInvalidated(getClass(), image);
				return image;
			} finally {
				this.image = null;
			}
		}
		
	}
	
	private ImageName imageName;
	private ImageType imageType;
	
	public ImageName getImageName() {
		return imageName;
	}
	
	public ImageType getImageType() {
		return imageType;
	}
	
}
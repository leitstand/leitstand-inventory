package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import io.leitstand.commons.model.ValueObject;

/**
 * The <code>ImageDeploymentCount</code> contains the count of elements where a given image is installed.
 */
public class ImageDeploymentCount extends ValueObject {

	/**
	 * Creates a builder for an immutable <code>ImageDeploymentCount</code> value object.
	 * @return a <code>ImageDeploymentCount</code> builder.
	 */
	public static Builder newImageDeploymentCount() {
		return new Builder();
	}
	
	/**
	 * The builder for an immutable <code>ImageDeploymentCount</code> value object.
	 */
	public static class Builder {
		
		private ImageDeploymentCount image = new ImageDeploymentCount();
	
		
		/**
		 * Sets the image ID.
		 * @param imageId the image ID.
		 * @return a reference to this builder to continue with object creation.
		 */
		public Builder withImageId(ImageId imageId) {
			assertNotInvalidated(getClass(), image);
			image.imageId = imageId;
			return this;
		}

		/**
		 * Sets the image name.
		 * @param imageName the image name.
		 * @return a reference to this builder to continue with object creation.
		 */
		public Builder withImageName(ImageName imageName) {
			assertNotInvalidated(getClass(), image);
			image.imageName = imageName;
			return this;
		}

		/**
		 * Sets the image state.
		 * @param imageId the image state.
		 * @return a reference to this builder to continue with object creation.
		 */
		public Builder withImageState(ImageState imageState) {
			assertNotInvalidated(getClass(), image);
			image.imageState = imageState;
			return this;
		}
		

		/**
		 * Sets the number of elements where this image is installed.
		 * @param elements the number of elements where the image is installed.
		 * @return a reference to this builder to continue with object creation.
		 */
		public Builder withElements(int elements) {
			assertNotInvalidated(getClass(), image);
			image.elements = elements;
			return this;
		}
		
		
		/**
		 * Returns the <code>ImageDeploymentCount</code> object and invalidates this builder.
		 * Subsequent calls of the <code>build()</code> method raise an exception.
		 * @return the <code>ImageDeploymentCount</code> object.
		 */
		public ImageDeploymentCount build() {
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
	private ImageState imageState;
	private int elements;
	
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
	 * Returns the image lifecycle state.
	 * @return the image lifecycle state.
	 */
	public ImageState getImageState() {
		return imageState;
	}
	
	/**
	 * Returns the number of elements where the image is installed.
	 * @return the image installation count.
	 */
	public int getElements() {
		return elements;
	}
}

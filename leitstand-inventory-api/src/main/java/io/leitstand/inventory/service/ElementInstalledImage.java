/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * A summary of an image installed on an element.
 */
public class ElementInstalledImage extends BaseElementEnvelope{

	/**
	 * Returns a new builder to create an immutable <code>ElementInstalledImage</code> instance.
	 * @return a new builder to create an immutable <code>ElementInstalledImage</code> instance.
	 */
	public static Builder newElementInstalledImage(){
		return new Builder();
	}
	
	/**
	 * The builder to create a new immutable <code>ElementInstalledImage</code> instance.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementInstalledImage, Builder>{
		
		public Builder() {
			super(new ElementInstalledImage());
		}
		
		/**
		 * Sets the image information
		 * @param image - the image information.
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withImage(ElementInstalledImageData.Builder image){
			return withImage(image.build());
		}
		
		/**
		 * Sets the image information
		 * @param image - the image information.
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withImage(ElementInstalledImageData image){
			assertNotInvalidated(getClass(), object);
			object.image = image;
			return this;
		}

		/**
		 * Returns an immutable <code>ElementInstalledImage</code> instance and 
		 * invalidates this builde. Any further interaction with this builder raises an exception.
		 * @return
		 */
		@Override
		public ElementInstalledImage build(){
			try{
				assertNotInvalidated(getClass(), object);
				return object;
			} finally {
				this.object = null;
			}
		}

	}
	
	@Valid
	@NotNull(message="{image.required}")
	private ElementInstalledImageData image;

	/**
	 * Returns the element image information
	 * @return the element image information
	 */
	public ElementInstalledImageData getImage() {
		return image;
	}
	
}

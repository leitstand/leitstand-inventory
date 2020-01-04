/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import java.util.UUID;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.ImageIdAdapter;

/**
 * A unique identifier for an image stored in the resource inventory.
 * <p>
 * The image ID is immutable for a image and hence forms a persistent unique key for a image.
 * The image ID is compatible to the UUIDv4 format.
 * </p>
 * @see ImageInfo
 * @see ImageType
 */
@JsonbTypeAdapter(ImageIdAdapter.class)
public class ImageId extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	
	/**
	 * Creates an <code>ImageId</code> from the specified string.
	 * @param id the mage ID
	 * @return the <code>ImageId</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static ImageId valueOf(String id) {
		return fromString(id,ImageId::new);
	}

	
	
	private String value;

	/**
	 * Returns a random image ID.
	 * @return a random image ID.
	 */
	public static ImageId randomImageId() {
		return new ImageId(UUID.randomUUID());
	}
	
	/**
	 * Creates an <code>ImageId</code>.
	 * @param imageId the image ID.
	 */
	public ImageId(UUID imageId) {
		this(imageId.toString());
	}
	
	/**
	 * Creates an <code>ImageId</code>.
	 * @param imageId the image ID.
	 */
	public ImageId(String imageId){
		this.value = imageId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return value;
	}
	
}

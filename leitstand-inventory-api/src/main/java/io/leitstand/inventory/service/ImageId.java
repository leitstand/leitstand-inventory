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

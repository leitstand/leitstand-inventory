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

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.ImageNameAdapter;

/**
 * Unique image name.
 * <p>
 * The image name can be modified provided the new image name is also unique.
 * @see ImageId
 */
@JsonbTypeAdapter(ImageNameAdapter.class)
public class ImageName extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * Creates an <code>ImageName</code> from the specified string.
	 * Returns <code>null</code> if the specified string is <code>null</code> or empty.
	 * <p>
	 * This method is an alias for the {@link #valueOf(String)} method to improve readability by avoiding static import conflicts.
	 * @param name the image name.
	 * @return the <code>ImageName</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static ImageName imageName(String name) {
		return valueOf(name);
	}

	/**
     * Creates an <code>ImageName</code> from the specified string.
     * Returns <code>null</code> if the specified string is <code>null</code> or empty.
     * @param name the image name.
     * @return the <code>ImageName</code> or <code>null</code> if the specified string is <code>null</code> or empty.
     */
	public static ImageName valueOf(String name) {
		return fromString(name, ImageName::new);
	}
	
	@NotNull(message="{image_name.required}")
	@Pattern(message="{image_name.invalid}", regexp="\\p{Print}{1,128}")
	private String value;	
	
	/**
	 * Creates an image name.
	 * @param value the image name.
	 */
	public ImageName(String value) {
		this.value = value;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return value;
	}


}

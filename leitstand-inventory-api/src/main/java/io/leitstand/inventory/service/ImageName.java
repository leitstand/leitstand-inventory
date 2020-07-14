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
 * The name of the software image of a certain {@link ImageType} for further classification of an image.
 * For example, the RBFS LXC image, is the RtBrick Full Stack LXC container image.
 */
@JsonbTypeAdapter(ImageNameAdapter.class)
public class ImageName extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	public static ImageName imageName(String name) {
		return valueOf(name);
	}

	
	public static ImageName valueOf(String name) {
		return Scalar.fromString(name, ImageName::new);
	}
	
	@NotNull(message="{image_name.required}")
	@Pattern(message="{image_name.invalid}", regexp="\\p{Print}{1,128}")
	private String value;	
	
	public ImageName(String value) {
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return value;
	}


}

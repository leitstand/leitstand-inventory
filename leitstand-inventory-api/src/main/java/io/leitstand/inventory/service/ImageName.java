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
	@Pattern(message="{image_name.invalid}", regexp="\\p{Print}{1,64}")
	private String value;	
	
	public ImageName(String value) {
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return value;
	}


}

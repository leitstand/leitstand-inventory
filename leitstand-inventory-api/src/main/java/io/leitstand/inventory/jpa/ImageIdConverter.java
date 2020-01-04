/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.ImageId;

@Converter
public class ImageIdConverter implements AttributeConverter<ImageId, String>{

	@Override
	public String convertToDatabaseColumn(ImageId attribute) {
		return ImageId.toString(attribute);
	}

	@Override
	public ImageId convertToEntityAttribute(String dbData) {
		return ImageId.valueOf(dbData);
	}

}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.ImageName;

@Converter
public class ImageNameConverter implements AttributeConverter<ImageName, String>{

	@Override
	public String convertToDatabaseColumn(ImageName attribute) {
		return ImageName.toString(attribute);
	}

	@Override
	public ImageName convertToEntityAttribute(String dbData) {
		return ImageName.valueOf(dbData);
	}

}

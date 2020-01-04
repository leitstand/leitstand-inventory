/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import static io.leitstand.inventory.service.ImageState.REVOKED;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.ImageState;

@Converter
public class ImageStateConverter implements AttributeConverter<ImageState, String> {

	@Override
	public String convertToDatabaseColumn(ImageState state) {
		if(state == null){
			// All images without state are considered revoked!
			return "X";
		}
		return toDbValue(state);
	}

	@Override
	public ImageState convertToEntityAttribute(String s) {
		return toImageState(s);
	}

	public static ImageState toImageState(String s) {
		if("N".equalsIgnoreCase(s)){
			return ImageState.NEW;
		}
		if("C".equalsIgnoreCase(s)){
			return ImageState.CANDIDATE;
		}
		if("R".equalsIgnoreCase(s)){
			return ImageState.RELEASE;
		}
		if("S".equalsIgnoreCase(s)){
			return ImageState.SUPERSEDED;
		}
		return ImageState.REVOKED;
	}

	public static String toDbValue(ImageState state) {
		if(REVOKED == state) {
			return "X";
		}
		return state.name().substring(0,1);
	}

}

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

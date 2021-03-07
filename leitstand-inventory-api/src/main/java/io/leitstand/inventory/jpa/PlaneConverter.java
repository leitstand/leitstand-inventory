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

import static io.leitstand.commons.model.StringUtil.isEmptyString;

import java.util.NoSuchElementException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.Plane;

@Converter
public class PlaneConverter implements AttributeConverter<Plane, String> {

	@Override
	public String convertToDatabaseColumn(Plane plane) {	
		return convert(plane);
	}

	@Override
	public Plane convertToEntityAttribute(String mnemonic) {
		try {
			return parse(mnemonic);
		} catch(NoSuchElementException e) {
			return null;
		}
	}
	

	public static Plane parse(String mnemonic) {
		if(isEmptyString(mnemonic)){
			return null;
		}
		switch(mnemonic){
			case "D" : return Plane.DATA;
			case "C" : return Plane.CONTROL;
			case "M" : return Plane.MANAGEMENT;
			default: throw new NoSuchElementException(mnemonic +" cannot be mapped to control or data plane");
		}
	}
	
	public static String convert(Plane plane) {
		if(plane == null){
			return null;
		}
		return plane.name().substring(0,1);
	}

}

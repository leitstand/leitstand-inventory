/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

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
		if(mnemonic == null || mnemonic.isEmpty()){
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

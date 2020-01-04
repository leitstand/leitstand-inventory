/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.MACAddressAdapter;

@JsonbTypeAdapter(MACAddressAdapter.class)
public class MACAddress extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	public static MACAddress macAddress(String macAddress) {
		return valueOf(macAddress);
	}
	
	public static MACAddress valueOf(String macAddress){
		return fromString(macAddress,MACAddress::new);
	}
	
	private  String value;
	
	public MACAddress(String value){
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return value;
	}



}

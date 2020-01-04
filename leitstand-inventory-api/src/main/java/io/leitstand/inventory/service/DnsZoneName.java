/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.DnsZoneNameAdapter;

@JsonbTypeAdapter(DnsZoneNameAdapter.class)
public class DnsZoneName extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	public static final DnsZoneName dnsZoneName(String name) {
		return valueOf(name);
	}
	
	public static final DnsZoneName valueOf(String name) {
		return Scalar.fromString(name, DnsZoneName::new);
	}
	
	private String value;
	
	public DnsZoneName(String name) {
		this.value = name;
	}
	
	@Override
	public String getValue() {
		return value;
	}

	
	
}

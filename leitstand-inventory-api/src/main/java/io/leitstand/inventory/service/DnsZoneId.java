/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import java.util.UUID;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.DnsZoneIdAdapter;

@JsonbTypeAdapter(DnsZoneIdAdapter.class)
public class DnsZoneId extends Scalar<String> {

	private static final long serialVersionUID = 1L;

	public static DnsZoneId randomDnsZoneId() {
		return valueOf(UUID.randomUUID().toString());
	}
	
	public static  DnsZoneId dnsZoneId(String name) {
		return valueOf(name);
	}
	
	public static final DnsZoneId valueOf(String name) {
		return Scalar.fromString(name, DnsZoneId::new);
	}
	
	private String value;
	
	public DnsZoneId(String name) {
		this.value = name;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	
}

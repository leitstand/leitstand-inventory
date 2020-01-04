/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.DnsRecordTypeAdapter;

@JsonbTypeAdapter(DnsRecordTypeAdapter.class)
public class DnsRecordType extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	public static DnsRecordType dnsRecordType(String type) {
		return valueOf(type);
	}
	
	public static DnsRecordType valueOf(String type) {
		return fromString(type, DnsRecordType::new);
	}
	
	private String value;
	
	public DnsRecordType(String type) {
		this.value = type;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	
	
}

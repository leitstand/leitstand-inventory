/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.Patterns.DNS_PATTERN;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.DnsNameAdapter;

@JsonbTypeAdapter(DnsNameAdapter.class)
public class DnsName extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	public static DnsName dnsName(String name) {
		return valueOf(name);
	}
	
	public static DnsName valueOf(String name) {
		return Scalar.fromString(name, DnsName::new);
	}
	
	@Pattern(regexp=DNS_PATTERN, message="{dns_name.invalid}")
	private String value;
	
	public DnsName(String name) {
		this.value = name;
	}
	
	@Override
	public String getValue() {
		return value;
	}

	public boolean endsWidth(DnsZoneName dnsZoneName) {
		return getValue().endsWith(dnsZoneName.getValue());
	}

	
	
}

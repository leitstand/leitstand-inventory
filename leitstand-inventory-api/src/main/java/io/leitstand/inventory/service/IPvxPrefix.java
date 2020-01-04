/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.Patterns.IP_PREFIX;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.IPvxPrefixAdapter;

/** 
 * IPv4 or IPv6 address in CIDR notation.
 */
@JsonbTypeAdapter(IPvxPrefixAdapter.class)
public class IPvxPrefix extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a <code>CidrAddress</code> from the specified string
	 * @param address an IP address in CIDR notation
	 * @return the <code>CidrAddress</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static IPvxPrefix valueOf(String address) {
		return Scalar.fromString(address, IPvxPrefix::new);
	}
	
	@Pattern(regexp=IP_PREFIX)
	private String value;
	
	/**
	 * Creates a <code>CidrAddress</code>.
	 * @param address the IP address
	 */
	public IPvxPrefix(String address) {
		this.value = address;
	}

	/**
	 * Tests whether this prefix is an IPv4 prefix.
	 * @return <code>true</code> if this prefix is an IPv4 prefix, otherwise <code>false</code>
	 */
	public boolean isIPv4Prefix() {
		return this.value.matches(IP_PREFIX);
	}

	/**
	 * Tests whether this prefix is an IPv6 prefix.
	 * @return <code>true</code> if this prefix is an IPv6 prefix, otherwise <code>false</code>
	 */
	public boolean isIPv6Prefix() {
		return this.value.matches(IP_PREFIX);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return value;
	}
	
}

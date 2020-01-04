/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.IPv4PrefixAdapter;

/**
 * An IPv4 prefix.
 */
@JsonbTypeAdapter(IPv4PrefixAdapter.class)
public class IPv4Prefix extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	/**
     * Creates an <code>IPv4Prefix</code> from the specified string.
     * @param prefix the IPv4 prefix
     * @return the <code>IPv4Prefix</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static IPv4Prefix valueOf(String prefix){
		return fromString(prefix,IPv4Prefix::new);
	}
	
	//TODO Pattern
	private String value;
	
	/**
	 * Create a <code>IP4Prefix</code>
	 * @param value - the prefix value
	 */
	public IPv4Prefix(String value){
		this.value = value;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return value;
	}
	
}
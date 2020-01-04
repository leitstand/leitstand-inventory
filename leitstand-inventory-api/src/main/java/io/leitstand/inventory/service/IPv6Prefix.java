/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.IPv6PrefixAdapter;

/**
 * An IPv6 prefix.
 */
@JsonbTypeAdapter(IPv6PrefixAdapter.class)
public class IPv6Prefix extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a <code>IPv6Prefix</code> from the specified string.
	 * @param prefix the IPv6 prefix
	 * @return the <code>IPv6Prefix</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static IPv6Prefix valueOf(String prefix){
		return fromString(prefix,IPv6Prefix::new);
	}
	
	private String value;
	
	/**
	 * Creates a <code>IPv6Prefix</code>
	 * @param prefix the IPv6 prefix
	 */
	public IPv6Prefix(String prefix){
		this.value = prefix;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return value;
	}
	
}
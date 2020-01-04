/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.IPvxPrefix;

/**
 * Translates a <code>IPvxPrefix</code> to a string and vice versa.
 */
public class IPvxPrefixAdapter implements JsonbAdapter<IPvxPrefix, String>{

	/**
	 * Translates a <code>CidrAddress</code> into a string.
	 * @param address the IP address
	 * @return the address as string or <code>null</code> if the specified address is <code>null</code>.
	 */
	@Override
	public String adaptToJson(IPvxPrefix address) {
		return IPvxPrefix.toString(address);
	}

	/**
	 * Creates a <code>CidrAddress</code> from the specified string.
	 * @param address the IP address
	 * @return the <code>CidrAddress<code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	@Override
	public IPvxPrefix adaptFromJson(String address) {
		return IPvxPrefix.valueOf(address);
	}

}

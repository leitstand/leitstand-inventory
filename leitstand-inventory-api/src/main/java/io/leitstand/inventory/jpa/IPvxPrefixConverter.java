/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.IPvxPrefix;

/**
 * Converts a <code>CidrAddress</code> to a string and vice versa.
 */
@Converter(autoApply=true)
public class IPvxPrefixConverter implements AttributeConverter<IPvxPrefix, String>{

	/**
	 * Converts a <code>CidrAddress</code> into a string.
	 * @param address the IP address
	 * @return the address as string or <code>null</code> if the specified address is <code>null</code>.
	 */
	@Override
	public String convertToDatabaseColumn(IPvxPrefix address) {
		return IPvxPrefix.toString(address);
	}

	/**
	 * Creates a <code>CidrAddress</code> from the specified string.
	 * @param address the IP address
	 * @return the <code>CidrAddress<code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	@Override
	public IPvxPrefix convertToEntityAttribute(String address) {
		return IPvxPrefix.valueOf(address);
	}

}

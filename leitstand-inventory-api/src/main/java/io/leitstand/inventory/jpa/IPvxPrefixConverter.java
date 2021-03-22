/*
 * Copyright 2020 RtBrick Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.leitstand.inventory.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.commons.model.Scalar;
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
		return Scalar.toString(address);
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

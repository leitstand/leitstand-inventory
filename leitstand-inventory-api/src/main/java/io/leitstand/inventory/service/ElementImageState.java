/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.StringUtil.isEmptyString;

/**
 * Enumeration of image installation states.
 */
public enum ElementImageState {
	
	/**
	 * The image shall be pulled by the device.
	 * This state is typically used for device replacements in order to 
	 * install the same image version again, irrespective of the current release version.
	 */
	PULL,
	/**
	 * The image exists in the local cache but is not active at the moment.
	 */
	CACHED,
	
	/**
	 * The image running at the device.
	 */
	ACTIVE;
	
	/**
	 * Converts a string to the corresponding installation state and defaults to <code>PULL</code>
	 * if the string is <code>null</code> or <i>empty</i>. 
	 * @param s - the string to be mapped to an installation state
	 * @return the corresponding installation state
	 */
	public static ElementImageState toInstallationState(String s) {
		if(isEmptyString(s)) {
			return PULL;
		}
		return valueOf(s);
	}
	
	/**
	 * Returns whether the image is already locally cached.
	 * @return <code>true</code> if the image is locally cached, <code>false</code> otherwise.
	 */
	public boolean isCached() {
		return this != PULL;
	}
}

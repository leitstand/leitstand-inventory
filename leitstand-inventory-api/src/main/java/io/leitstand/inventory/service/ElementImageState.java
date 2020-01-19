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

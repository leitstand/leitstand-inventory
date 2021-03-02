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
 * Enumeration of element image lifecycle states.
 */
public enum ElementImageState {
	
	/**
	 * The image shall be pulled by the element.
	 */
	PULL,
	/**
	 * The image exists in the local element cache but is not active at the moment.
	 */
	CACHED,
	
	/**
	 * The image running on the device.
	 */
	ACTIVE;
	
	/**
	 * Converts a string to the corresponding installation state and defaults to <code>PULL</code>
	 * if the string is <code>null</code> or empty. 
	 * @param state the element image state
	 * @return the corresponding installation state
	 */
	public static ElementImageState toElementImageState(String s) {
		if(isEmptyString(s)) {
			return PULL;
		}
		return valueOf(s);
	}
	
	/**
	 * Returns whether the image is locally cached.
	 * @return <code>true</code> if the image is locally cached, <code>false</code> otherwise.
	 */
	public boolean isCached() {
		return this != PULL;
	}
}

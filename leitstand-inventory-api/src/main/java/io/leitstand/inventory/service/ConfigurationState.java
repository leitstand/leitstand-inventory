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

/**
 * Enumeration of configuration lifecycle states.
 */
public enum ConfigurationState {

    /** A candidate element configuration is a generated configuration which has not been applied to the element yet.*/
	CANDIDATE,
	/** The active element configuration.*/
	ACTIVE,
	/** Superseded configurations form the configuration history. Each superseded configuration is a former active element configuration.*/
	SUPERSEDED;

    /**
     * Creates a configuration state from the specified string.
     * <p>
     * This is an alias of the {@link #valueOf(String)} method to improve readability 
     * by avoiding static import conflicts.
     * </p>
     * @param state the configuration state as string
     * @return the configuration state.
     */
	public static ConfigurationState configurationState(String state) {
		return valueOf(state);
	}
	
}
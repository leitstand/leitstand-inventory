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
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.EnvironmentName;

/**
 * Translates an <code>EnvironmentName</code> into a string and vice versa.
 */
public class EnvironmentNameAdapter implements JsonbAdapter<EnvironmentName, String>{

	/**
	 * Translates a string into an <code>EnvironmentName</code>.
	 * @param name the environment name
	 * @return the <code>EnvironmentName</code> or <code>null</code> if the specified name is <code>null</code> or empty.
	 */
	@Override
	public EnvironmentName adaptFromJson(String name) throws Exception {
		return EnvironmentName.valueOf(name);
	}

	/** 
	 * Translates an <code>EnvironmentName</code> into a string.
	 * @param name the environment name
	 * @returns the string value or <code>null</code> if the specified name is <code>null</code>.
	 */
	@Override
	public String adaptToJson(EnvironmentName name) throws Exception {
		return EnvironmentName.toString(name);
	}

}

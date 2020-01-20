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

import io.leitstand.inventory.service.RoutingInstanceName;

/**
 * Translates a <code>RoutingInstanceName</code> into a string and vice versa.
 */
public class RoutingInstanceNameAdapter implements JsonbAdapter<RoutingInstanceName, String> {

	/**
	 * Translates a string into a <code>RoutingInstanceName</code>.
	 * @param name the routing instance name
	 * @return the <code>RoutingInstanceName</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	@Override
	public RoutingInstanceName adaptFromJson(String name) {
		return RoutingInstanceName.valueOf(name);
	}

	/**
	 * Translates a routing instance name into a string.
	 * @param name the routing instance name
	 * @return the routing instance name or <code>null</code> if the specified name is <code>null</code>.
	 */
	@Override
	public String adaptToJson(RoutingInstanceName name) {
		return RoutingInstanceName.toString(name);
	}

}

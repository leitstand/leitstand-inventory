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

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.ElementConfigNameAdapter;

@JsonbTypeAdapter(ElementConfigNameAdapter.class)
public class ElementConfigName extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	private String value;

	public static ElementConfigName elementConfigName(String name) {
		return valueOf(name);
	}

	
	public static ElementConfigName valueOf(String name) {
		return Scalar.fromString(name, ElementConfigName::new);
	}
	
	public static String toString(ElementConfigName name) {
		return Scalar.toString(name);
	}
	
	public ElementConfigName(String name) {
		this.value = name;
	}
	
	@Override
	public String getValue() {
		return value;
	}


}

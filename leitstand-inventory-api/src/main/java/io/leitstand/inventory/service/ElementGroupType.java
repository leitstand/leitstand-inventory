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
import io.leitstand.inventory.jsonb.ElementGroupTypeAdapter;

@JsonbTypeAdapter(ElementGroupTypeAdapter.class)
public class ElementGroupType extends Scalar<String> {

	private static final long serialVersionUID = 1L;

	public static ElementGroupType groupType(String type) {
		return valueOf(type);
	}

	
	public static ElementGroupType valueOf(String type) {
		return fromString(type, ElementGroupType::new);
	}
	
	private String value;
	
	public ElementGroupType(String value) {
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return value;
	}

}

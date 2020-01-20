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

import io.leitstand.inventory.service.ElementId;

public class ElementIdAdapter implements JsonbAdapter<ElementId,String> {

	@Override
	public String adaptToJson(ElementId obj) throws Exception {
		return ElementId.toString(obj);
	}

	@Override
	public ElementId adaptFromJson(String obj) throws Exception {
		return ElementId.valueOf(obj);
	}

}

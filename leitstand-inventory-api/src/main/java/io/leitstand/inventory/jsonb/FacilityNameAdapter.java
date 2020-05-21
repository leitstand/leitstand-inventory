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

import io.leitstand.inventory.service.FacilityName;

public class FacilityNameAdapter implements JsonbAdapter<FacilityName,String> {

	@Override
	public FacilityName adaptFromJson(String v) throws Exception {
		return FacilityName.valueOf(v);
	}

	@Override
	public String adaptToJson(FacilityName v) throws Exception {
		return FacilityName.toString(v);
	}

}

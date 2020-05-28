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

import io.leitstand.inventory.service.VlanID;

public class VlanIDAdapter implements JsonbAdapter<VlanID,Integer> {

	@Override
	public VlanID adaptFromJson(Integer v) throws Exception {
		if(v == null){
			return null;
		}
		return new VlanID(v);
	}

	@Override
	public Integer adaptToJson(VlanID v) throws Exception {
		if(v == null){
			return null;
		}
		return v.getValue();
	}

}

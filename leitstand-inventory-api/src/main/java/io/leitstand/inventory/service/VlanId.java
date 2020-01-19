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
import io.leitstand.inventory.jsonb.VlanIdAdapter;

@JsonbTypeAdapter(VlanIdAdapter.class)
public class VlanId extends Scalar<Integer> {

	private static final long serialVersionUID = 1L;

	private Integer value;
	
	public VlanId(int vlanId) {
		this(Integer.valueOf(vlanId));
	}
	
	public VlanId(Integer vlanId) {
		this.value = vlanId;
	}
	
	@Override
	public Integer getValue() {
		return value;
	}

}

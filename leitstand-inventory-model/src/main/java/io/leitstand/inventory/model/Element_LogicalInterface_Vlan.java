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
package io.leitstand.inventory.model;

import javax.persistence.Convert;
import javax.persistence.Embeddable;

import io.leitstand.inventory.jpa.VlanIDConverter;
import io.leitstand.inventory.jpa.VlanTPIDConverter;
import io.leitstand.inventory.service.VlanID;
import io.leitstand.inventory.service.VlanTPID;

@Embeddable
class Element_LogicalInterface_Vlan {

	@Convert(converter=VlanIDConverter.class)
	private VlanID vlanId;
	
	@Convert(converter=VlanTPIDConverter.class)
	private VlanTPID vlanTpid;

	protected Element_LogicalInterface_Vlan() {
		// JPA
	}
	
	public Element_LogicalInterface_Vlan(VlanTPID vlanTpid, VlanID vlanId) {
		this.vlanTpid = vlanTpid;
		this.vlanId = vlanId;
	}
	
	public VlanID getVlanId() {
		return vlanId;
	}

	public VlanTPID getVlanTpid() {
		return vlanTpid;
	}
	
}


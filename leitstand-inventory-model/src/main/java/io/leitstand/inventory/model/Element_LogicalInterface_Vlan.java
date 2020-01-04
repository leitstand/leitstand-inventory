/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import javax.persistence.Convert;
import javax.persistence.Embeddable;

import io.leitstand.inventory.jpa.VlanIdConverter;
import io.leitstand.inventory.service.VlanId;

@Embeddable
class Element_LogicalInterface_Vlan {

	@Convert(converter=VlanIdConverter.class)
	private VlanId vlanId;
	private int tag;

	protected Element_LogicalInterface_Vlan() {
		// JPA
	}
	
	public Element_LogicalInterface_Vlan(VlanId vlanId, int tag) {
		this.vlanId = vlanId;
		this.tag = tag;
	}
	
	public int getTag() {
		return tag;
	}
	
	public VlanId getVlanId() {
		return vlanId;
	}
	
	
}


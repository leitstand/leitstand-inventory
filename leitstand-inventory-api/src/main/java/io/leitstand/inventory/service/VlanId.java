/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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
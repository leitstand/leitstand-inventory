/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import io.leitstand.commons.model.Scalar;

public class VlanTpId extends Scalar<Integer>{

	private static final long serialVersionUID = 1L;

	public static VlanTpId valueOf(int tpid) {
		return new VlanTpId(tpid);
	}
	
	private int value;
	
	public VlanTpId(int tpid) {
		this.value = tpid;
	}
	
	@Override
	public Integer getValue() {
		return value;
	}

}

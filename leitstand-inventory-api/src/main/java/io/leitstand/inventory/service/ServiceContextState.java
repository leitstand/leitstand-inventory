/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

public enum ServiceContextState {
	ACTIVE,
	COLD_STANDBY,
	HOT_STANDBY,
	INACTIVE;

	public static ServiceContextState parse(String s) {
		if("A".equals(s)){
			return ACTIVE;
		}
		if("C".equals(s)){
			return COLD_STANDBY;
		}
		if("H".equals(s)){
			return HOT_STANDBY;
		}
		return INACTIVE;
	}
}

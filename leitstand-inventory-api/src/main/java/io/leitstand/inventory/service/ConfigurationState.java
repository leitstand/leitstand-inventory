/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

public enum ConfigurationState {

	CANDIDATE,
	ACTIVE,
	SUPERSEDED;

	public static ConfigurationState configurationState(String state) {
		return valueOf(state);
	}
	
}

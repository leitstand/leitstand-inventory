/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.OperationalStateAdapter;

/**
 * Enumeration of available operational states.
 */
@JsonbTypeAdapter(OperationalStateAdapter.class)
public class OperationalState extends Scalar<String>{
	
	
	private static final long serialVersionUID = 1L;

	public static final OperationalState UP = new OperationalState("UP");
	public static final OperationalState DOWN = new OperationalState("DOWN");
	public static final OperationalState STARTED = new OperationalState("STARTED");
	public static final OperationalState PARTIAL = new OperationalState("PARTIAL");
	public static final OperationalState STOPPED = new OperationalState("STOPPED");
	public static final OperationalState MAINTENANCE = new OperationalState("MAINTENANCE");
	public static final OperationalState OPERATIONAL = new OperationalState("OPERATIONAL");
	public static final OperationalState MALFUNCTION = new OperationalState("MALFUNCTION");
	public static final OperationalState UNKNOWN = new OperationalState("UNKNOWN");
	public static final OperationalState DETACHED = new OperationalState("DETACHED");
	
	public static OperationalState operationalState(String state) {
		return valueOf(state);
	}
	
	public static OperationalState valueOf(String state) {
		return fromString(state,OperationalState::new);
	}
	
	private String value;
	
	@JsonbCreator
	public OperationalState(String value) {
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	
	public String name() {
		return value;
	}

	public boolean is(OperationalState state) {
		return this.equals(state);
	}


	
	
}

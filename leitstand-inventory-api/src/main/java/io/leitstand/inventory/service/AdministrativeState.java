/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;
/**
 * Enumeration of all available administrative states.
 */

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.AdministrativeStateAdapter;

@JsonbTypeAdapter(AdministrativeStateAdapter.class)
public class AdministrativeState extends Scalar<String> {

	private static final long serialVersionUID = 1L;

	/** 
	 * A new artifact which is currently not active in the network.
	 * Typically the artifact is a stub record that allows to store all 
	 * necessary information to activate the artifact in the future.
	 */
	public static final AdministrativeState NEW = new AdministrativeState("NEW");
	
	/**
	 * A new element has been provisioned (typically in a staging area) and 
	 * is now ready for being installed in the network.
	 */
	public static final AdministrativeState PROVISIONED = new AdministrativeState("PROVISIONED");
	
	/**
	 * An active artifact that is in use.
	 */
	public static final AdministrativeState ACTIVE = new AdministrativeState("ACTIVE");

	/**
	 * An deactivated artifact that is currently not in used but also shall not be retired in the future.
	 */
	public static final AdministrativeState INACTIVE = new AdministrativeState("INACTIVE");
	
	/**
	 * A formally active artifact that has been disabled and about to be removed.
	 */
	public static final AdministrativeState RETIRED = new AdministrativeState("RETIRED");
	
	/**
	 * An alias for the active state. 
	 */
	public static final AdministrativeState UP = new AdministrativeState("UP");
	
	/**
	 * An alias for the inactive state.
	 */
	public static final AdministrativeState DOWN = new AdministrativeState("DOWN");
	
	/** The administrative state is unknown.*/
	public static final AdministrativeState UNKNOWN = new AdministrativeState("UNKNOWN");
	
	

	/** 
	 * Alias for {@link #valueOf(String)} to improve readability.
	 * <p>
	 * Creates an <code>AdministrativeState</code> from the specified string.
	 * @param state the administrative state
	 * @return the <code>AdministrativeState</code> or <code>null</code> if the specified value is <code>null</code> or empty.
	 */

	public static AdministrativeState administrativeState(String state) {
		return valueOf(state);
	}
	/** 
	 * Creates an <code>AdministrativeState</code> from the specified string.
	 * @param state the administrative state
	 * @return the <code>AdministrativeState</code> or <code>null</code> if the specified value is <code>null</code> or empty.
	 */
	public static final AdministrativeState valueOf(String state) {
		return fromString(state,AdministrativeState::new);
	}
	
	private String value;
	
	public AdministrativeState(String value) {
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	
	public String name() {
		return value;
	}

	public boolean is(AdministrativeState state) {
		return this.equals(state);
	}

}

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
import io.leitstand.inventory.jsonb.AdministrativeStateAdapter;

/**
 * Enumeration of administrative states.
 */
@JsonbTypeAdapter(AdministrativeStateAdapter.class)
public class AdministrativeState extends Scalar<String> {

	private static final long serialVersionUID = 1L;

	/** 
	 * A new resource which is currently not used in the network.
	 */
	public static final AdministrativeState NEW = new AdministrativeState("NEW");
	
	/**
	 * A provisioned resource which is ready for being put into production.
	 */
	public static final AdministrativeState PROVISIONED = new AdministrativeState("PROVISIONED");
	
	/**
	 * An active resource, i.e. the resource is used in the live network.
	 * @see AdministrativeState#UP
	 */
	public static final AdministrativeState ACTIVE = new AdministrativeState("ACTIVE");

	/**
	 * An inactive resource which is currently not used in production.
     * @see AdministrativeState#ACTIVE
	 * @see AdministrativeState#RETIRED
	 * @see AdministrativeState#DOWN
	 */
	public static final AdministrativeState INACTIVE = new AdministrativeState("INACTIVE");
	
	/**
	 * A retired resource which is not used in production anymore.
	 */
	public static final AdministrativeState RETIRED = new AdministrativeState("RETIRED");
	
	/**
	 * An alias for the {@link #ACTIVE} state.
	 */
	public static final AdministrativeState UP = new AdministrativeState("UP");
	
	/**
	 * An alias for the {@link #INACTIVE} state.
	 */
	public static final AdministrativeState DOWN = new AdministrativeState("DOWN");
	
	/** The administrative state is unknown.*/
	public static final AdministrativeState UNKNOWN = new AdministrativeState("UNKNOWN");
	
	/** 
	 * Creates an <code>AdministrativeState</code> from the specified string.
	 * <p>
	 * Alias for {@link #valueOf(String)} to improve readability.
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
	
	/**
	 * Creates an administrative state.
	 * @param value the name of the administrative state.
	 */
	public AdministrativeState(String value) {
		this.value = value;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return value;
	}
	
	/**
	 * Returns the name of the administrative state.
	 * @return the name of the administrative state.
	 */
	public String name() {
		return value;
	}

	/**
	 * Tests whether this administrative <i>is</i> the specified administrative state.
	 * @param state the expected administrative state
	 * @return <code>true</code> if this state is the expected state, <code>false</code> if not.
	 */
	public boolean is(AdministrativeState state) {
		return this.equals(state);
	}

}

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
	public static final OperationalState FAILED = new OperationalState("FAILED");
	public static final OperationalState STARTED = new OperationalState("STARTED");
	public static final OperationalState PARTIAL = new OperationalState("PARTIAL");
	public static final OperationalState STOPPED = new OperationalState("STOPPED");
	public static final OperationalState MAINTENANCE = new OperationalState("MAINTENANCE");
	public static final OperationalState OPERATIONAL = new OperationalState("OPERATIONAL");
	/** @deprecated use FAILED instead.*/
	@Deprecated
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

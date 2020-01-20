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

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Metric_AlertRule_DefinitionPK implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long rule;
	private Date tsmodified;
	
	public Metric_AlertRule_DefinitionPK() {
		// JPA
	}
	
	public Metric_AlertRule_DefinitionPK(Metric_AlertRule rule, Date dateModified) {
		this.rule = rule.getId();
		this.tsmodified = dateModified;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(rule,tsmodified);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		if(o == this) {
			return true;
		}
		if(o.getClass() != getClass()) {
			return false;
		}
		Metric_AlertRule_DefinitionPK pk = (Metric_AlertRule_DefinitionPK) o;
		return Objects.equals(rule, pk.rule) && Objects.equals(tsmodified, pk.tsmodified);
	}
}

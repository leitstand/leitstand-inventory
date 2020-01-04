/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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

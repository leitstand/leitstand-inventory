/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import java.io.Serializable;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.AlertRuleIdAdapter;

/**
 * Unique rule correlation ID.
 * <p>
 * The correlation ID allows to identify the rule that has fired an alert,
 * which helps to understand why an alert was fired and also simplifies to search for invalid rules.
 */
@JsonbTypeAdapter(AlertRuleIdAdapter.class)
public class AlertRuleId extends Scalar<String> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates an <code>AlertRuleId</code> from the specified string.
	 * @param ruleId the alert rule ID
	 * @return the <code>AlertRuleId</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static AlertRuleId valueOf(String ruleId) {
		return fromString(ruleId,AlertRuleId::new);
	}
	
	private String value;
	
	/**
	 * Creates an <code>AlertRuleId</code>.
	 * @param ruleId the alert rule ID.
	 */
	public AlertRuleId(String ruleId) {
		this.value = ruleId;
	}
	
	@Override
	public String getValue() {
		return value;
	}


}

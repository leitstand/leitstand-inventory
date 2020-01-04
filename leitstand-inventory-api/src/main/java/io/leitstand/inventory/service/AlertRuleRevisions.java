/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import io.leitstand.commons.model.ValueObject;

public class AlertRuleRevisions extends ValueObject {

	public static Builder newAlertRuleRevisions() {
		return new Builder();
	}
	
	public static class Builder {
		
		private AlertRuleRevisions instance = new AlertRuleRevisions();
		
		public Builder withRuleName(AlertRuleName ruleName) {
			assertNotInvalidated(getClass(), instance);
			instance.ruleName = ruleName;
			return this;
		}

		public Builder withCategory(String category) {
			assertNotInvalidated(getClass(), instance);
			instance.category = category;
			return this;
		}
		
		public Builder withDescription(String description) {
			assertNotInvalidated(getClass(), instance);
			instance.description = description;
			return this;
		}
		
		public Builder withRevisions(AlertRuleRevisionInfo.Builder... revisions) {
			return withRevisions(stream(revisions)
								 .map(AlertRuleRevisionInfo.Builder::build)
								 .collect(toList()));
		}
		
		public Builder withRevisions(List<AlertRuleRevisionInfo> revisions) {
			assertNotInvalidated(getClass(), revisions);
			this.instance.revisions = new ArrayList<>(revisions);
			return this;
		}
		
		public AlertRuleRevisions build() {
			try {
				assertNotInvalidated(getClass(), instance);
				return instance;
			} finally {
				this.instance = null;
			}
		}
		
	}
	
	private AlertRuleName ruleName;
	private String category;
	private String description;
	private List<AlertRuleRevisionInfo> revisions;
	
	
	public AlertRuleName getRuleName() {
		return ruleName;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getDescription() {
		return description;
	}
	
	public List<AlertRuleRevisionInfo> getRevisions() {
		return unmodifiableList(revisions);
	}
}

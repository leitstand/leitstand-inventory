/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

public class AlertRuleExport extends AlertRuleInfo {

	public static Builder newAlertRuleExport() {
		return new Builder();
	}
	
	public static class Builder extends BaseBuilder<AlertRuleExport, Builder>{
		
		protected Builder() {
			super(new AlertRuleExport());
		}
		
		public Builder withRevisions(AlertRuleRevision.Builder... revisions) {
			return withRevisions(stream(revisions)
								 .map(AlertRuleRevision.Builder::build)
								 .collect(toList()));
		}
		
		public Builder withRevisions(List<AlertRuleRevision> revisions) {
			assertNotInvalidated(getClass(), rule);
			rule.revisions = new ArrayList<>(revisions);
			return this;
		}
		
	}
	
	private List<AlertRuleRevision> revisions = emptyList();
	
	
	public List<AlertRuleRevision> getRevisions() {
		return unmodifiableList(revisions);
	}
	
}

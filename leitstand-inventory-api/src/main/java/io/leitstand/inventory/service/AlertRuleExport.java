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

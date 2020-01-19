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
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

public class MetricSettings extends BaseMetricEnvelope {

	public static Builder newMetricSettings() {
		return new Builder();
	}
	
	protected static class BaseSettingsBuilder<T extends MetricSettings, B extends BaseSettingsBuilder<T,B>> extends BaseBuilder<T, B>{
		
		protected BaseSettingsBuilder(T settings) {
			super(settings);
		}
		
		public B withDescription(String description) {
			assertNotInvalidated(getClass(), metric);
			((MetricSettings)metric).description = description;
			return (B) this;
		}
		
		public B withElementRoles(ElementRoleName... elementRoles) {
			return withElementRoles(asList(elementRoles));
		}
		
		public B withElementRoles(List<ElementRoleName> elementRoles) {
			assertNotInvalidated(getClass(), metric);
			((MetricSettings)metric).elementRoles = elementRoles;
			return (B) this;
		}
	}
	
	public static class Builder extends BaseSettingsBuilder<MetricSettings,Builder>{
		protected Builder() {
			super(new MetricSettings());
		}
	}
	
	private String description;
	private List<ElementRoleName> elementRoles = emptyList();
	
	public String getDescription() {
		return description;
	}
	
	public List<ElementRoleName> getElementRoles() {
		return unmodifiableList(elementRoles);
	}
}

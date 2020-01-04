/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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

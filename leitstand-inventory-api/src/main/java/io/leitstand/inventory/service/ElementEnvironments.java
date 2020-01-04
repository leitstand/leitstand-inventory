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

public class ElementEnvironments extends BaseElementEnvelope{

	public static Builder newElementEnvironments() {
		return new Builder();
	}
	
	public static class Builder extends BaseElementEnvelopeBuilder<ElementEnvironments,Builder>{
		
		protected Builder() {
			super(new ElementEnvironments());
		}
		
		public Builder withEnvironments(EnvironmentInfo.Builder... envs) {
			return withEnvironments(stream(envs)
									.map(EnvironmentInfo.Builder::build)
									.collect(toList()));
		}
		
		public Builder withEnvironments(List<EnvironmentInfo> envs) {
			assertNotInvalidated(getClass(), object);
			object.environments = new ArrayList<>(envs);
			return this;
		}
		
	}
	
	private List<EnvironmentInfo> environments = emptyList();
	
	public List<EnvironmentInfo> getEnvironments() {
		return unmodifiableList(environments);
	}
	
	
}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

public class ElementEnvironment extends BaseElementEnvelope {

	public static Builder newElementEnvironment() {
		return new Builder();
	}
	
	public static class Builder extends BaseElementEnvelopeBuilder<ElementEnvironment, Builder>{
		protected Builder() {
			super(new ElementEnvironment());
		}
		
		public Builder withEnvironment(Environment.Builder env) {
			return withEnvironment(env.build());
		}
		
		public Builder withEnvironment(Environment env) {
			assertNotInvalidated(getClass(), object);
			object.environment = env;
			return this;
		}
	}
	
	private Environment environment;
	
	public Environment getEnvironment() {
		return environment;
	}
	
}

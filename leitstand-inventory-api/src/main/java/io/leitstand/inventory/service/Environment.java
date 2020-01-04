/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import javax.json.JsonObject;

public class Environment extends EnvironmentInfo {

	public static Builder newEnvironment() {
		return new Builder();
	}
	
	public static class Builder extends BaseEnvironmentBuilder<Environment, Builder>{
		protected Builder() {
			super(new Environment());
		}
		
		public Builder withVariables(JsonObject variables) {
			assertNotInvalidated(getClass(), env);
			env.variables = variables;
			return this;
		}
	}
	
	
	private JsonObject variables;
	
	public JsonObject getVariables() {
		return variables;
	}
}

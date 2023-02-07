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

import javax.json.JsonStructure;
import javax.validation.constraints.NotNull;

/**
 * An element environment.
 * <p>
 * An element can host multiple environments. 
 * Every element environment is a JSON object that is passed to the template engine to render an element configuration.
 * Typically an environment addresses a certain transport network capability. 
 * The element role defines which environments exist on an element.
 */
public class Environment extends EnvironmentInfo {

    /**
     * Creates a builder for an immutable <code>Environment</code> value object.
     * @return a builder for an immutable <code>Environment</code> value object.
     */
	public static Builder newEnvironment() {
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>Environment</code> value object.
	 */
	public static class Builder extends BaseEnvironmentBuilder<Environment, Builder>{
		protected Builder() {
			super(new Environment());
		}

		/**
		 * Sets the environment variables.
		 * @param variables the environment variables.
		 * @return a reference to this builder to continue objectcreation.
		 */
		public Builder withVariables(JsonStructure variables) {
			assertNotInvalidated(getClass(), env);
			env.variables = variables;
			return this;
		}
	}
	
	
	@NotNull(message="{variables.required}")
	private JsonStructure variables;
	
	/**
	 * Returns the environment variables.
	 * @return the environment variables.
	 */
	public JsonStructure getVariables() {
		return variables;
	}
}

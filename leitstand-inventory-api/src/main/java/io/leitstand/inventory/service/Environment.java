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

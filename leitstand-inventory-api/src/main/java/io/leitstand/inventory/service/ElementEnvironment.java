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
import static io.leitstand.inventory.service.EnvironmentId.randomEnvironmentId;

import javax.json.JsonObject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ElementEnvironment extends BaseElementEnvelope {

	public static Builder newElementEnvironment() {
		return new Builder();
	}
	
	public static class Builder extends BaseElementEnvelopeBuilder<ElementEnvironment, Builder>{
		protected Builder() {
			super(new ElementEnvironment());
		}
		
		public Builder withEnvironmentId(EnvironmentId environmentId) {
			assertNotInvalidated(getClass(), object);
			object.environmentId = environmentId;
			return this;
		}
		
		public Builder withEnvironmentName(EnvironmentName environmentName) {
			assertNotInvalidated(getClass(), object);
			object.environmentName = environmentName;
			return this;
		}
		
		public Builder withCategory(String category) {
			assertNotInvalidated(getClass(), object);
			object.category = category;
			return this;
		}
		
		public Builder withDescription(String description) {
			assertNotInvalidated(getClass(), object);
			object.description = description;
			return this;
		}
		
		public Builder withType(String type) {
			assertNotInvalidated(getClass(), object);
			object.type = type;
			return this;
		}
		
		public Builder withVariables(JsonObject variables) {
			assertNotInvalidated(getClass(), object);
			object.variables = variables;
			return this;
		}
		
	}
	
	private EnvironmentId environmentId = randomEnvironmentId();
	@Valid
	@NotNull(message="{environment_name.required}")
	private EnvironmentName environmentName;
	private String category;
	private String type;
	private String description;
	@NotNull(message="{variables.required}")
	private JsonObject variables;
	
	public EnvironmentId getEnvironmentId() {
		return environmentId;
	}
	
	public EnvironmentName getEnvironmentName() {
		return environmentName;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getType() {
		return type;
	}
	
	public String getDescription() {
		return description;
	}
	
	public JsonObject getVariables() {
		return variables;
	}
	
}

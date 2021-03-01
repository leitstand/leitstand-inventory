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

/**
 * An element environment.
 * <p>
 * An element environment is a JSON object rendered by the template engine to create an element configuration.
 */
public class ElementEnvironment extends BaseElementEnvelope {

    /**
     * Creates a builder for an immutable <code>ElementEnvironment</code> value object.
     * @return a builder for an immutable <code>ElementEnvironment</code> value object.
     */
	public static Builder newElementEnvironment() {
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>ElementEnvironment</code> value object.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementEnvironment, Builder>{
		protected Builder() {
			super(new ElementEnvironment());
		}
		
		/**
		 * Sets the environment ID.
		 * @param environmentId the environment ID
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withEnvironmentId(EnvironmentId environmentId) {
			assertNotInvalidated(getClass(), object);
			object.environmentId = environmentId;
			return this;
		}
		
		/**
		 * Sets the environment name.
		 * @param environmentName the environment name
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withEnvironmentName(EnvironmentName environmentName) {
			assertNotInvalidated(getClass(), object);
			object.environmentName = environmentName;
			return this;
		}
		
		/**
		 * Sets the environment category
		 * @param category the environment category
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withCategory(String category) {
			assertNotInvalidated(getClass(), object);
			object.category = category;
			return this;
		}
		
		/**
		 * Sets the environment description.
		 * @param description the environment description
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withDescription(String description) {
			assertNotInvalidated(getClass(), object);
			object.description = description;
			return this;
		}
		
		/**
		 * Sets the environment data type
		 * @param type the environment type
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withType(String type) {
			assertNotInvalidated(getClass(), object);
			object.type = type;
			return this;
		}
		
		/**
		 * Sets the environment variables.
		 * @param variables the environment variables
		 * @return a reference to this builder to continue object creation
		 */
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
	
	/**
	 * Returns the environment ID.
	 * @return the environment ID.
	 */
	public EnvironmentId getEnvironmentId() {
		return environmentId;
	}
	
	/**
	 * Returns the environment name.
	 * @return the environment name.
	 */
	public EnvironmentName getEnvironmentName() {
		return environmentName;
	}
	
	/**
	 * Returns the environment category.
	 * @return the environment category.
	 */
	public String getCategory() {
		return category;
	}
	
	/**
	 * Returns the environment type.
	 * @return the environment type.
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Returns the environment description.
	 * @return the environment description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Returns the environment variables.
	 * @return the environment variables.
	 */
	public JsonObject getVariables() {
		return variables;
	}
	
}

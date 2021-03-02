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

import io.leitstand.commons.model.ValueObject;

/**
 * Element environment metadata.
 */
public class EnvironmentInfo extends ValueObject{

    /**
     * Returns a builder for an immutable <code>EnvironmentInfo</code> value object.
     * @return a builder for an immutable <code>EnvironmentInfo</code> value object.
     */
	public static Builder newEnvironmentInfo() {
		return new Builder();
	}
	
	/**
	 * A base builder for an element environment value objects.
	 * @param <T> the environment value object
	 * @param <B> the environment value object builder
	 */
	@SuppressWarnings("unchecked")
	protected static class BaseEnvironmentBuilder<T extends EnvironmentInfo, B extends BaseEnvironmentBuilder<T,B>>{
		
		protected T env;
		
		/**
		 * Creates a new environment builder.
		 * @param env the environment value object under construction
		 */
		protected BaseEnvironmentBuilder(T env) {
			this.env = env;
		}
		
		/** 
		 * Sets the environment ID
		 * @param id the environment ID
		 * @return a reference to this builder to continue object creation
		 */
		public B withEnvironmentId(EnvironmentId id) {
			assertNotInvalidated(getClass(), env);
			((EnvironmentInfo)env).environmentId = id;
			return (B) this;
		}

	    /** 
         * Sets the environment name
         * @param name the environment name
         * @return a reference to this builder to continue object creation
         */
		public B withEnvironmentName(EnvironmentName name) {
			assertNotInvalidated(getClass(), env);
			((EnvironmentInfo)env).environmentName = name;
			return (B) this;
		}

	    /** 
         * Sets the environment category
         * @param category the environment category
         * @return a reference to this builder to continue object creation
         */
		public B withCategory(String category) {
			assertNotInvalidated(getClass(), env);
			((EnvironmentInfo)env).category = category;
			return (B) this;
		}

	    /** 
         * Sets the environment type
         * @param type the environment type
         * @return a reference to this builder to continue object creation
         */
		public B withType(String type) {
			assertNotInvalidated(getClass(), env);
			((EnvironmentInfo)env).type = type;
			return (B) this;
		}

	    /** 
         * Sets the environment description
         * @param id the environment description
         * @return a reference to this builder to continue object creation
         */
		public B withDescription(String description) {
			assertNotInvalidated(getClass(), env);
			((EnvironmentInfo)env).description = description;
			return (B) this;
		}
		
		/**
		 * Builds an immutable element environment value object.
		 * @return the immutable environment value object.
		 */
		public T build() {
			try {
				assertNotInvalidated(getClass(), env);
				return env;
			} finally {
				this.env = null;
			}
		}
		
	}
	
	/**
	 * A builder for an immutable <code>EnvironmentInfo</code> value object.
	 */
	public static class Builder extends BaseEnvironmentBuilder<EnvironmentInfo, Builder>{
		protected Builder() {
			super(new EnvironmentInfo());
		}
	}
	
	private EnvironmentId environmentId = randomEnvironmentId();
	private EnvironmentName environmentName;
	private String category;
	private String type;
	private String description;
	
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
	
}

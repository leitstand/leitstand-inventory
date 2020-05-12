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

public class EnvironmentInfo extends ValueObject{

	public static Builder newEnvironmentInfo() {
		return new Builder();
	}
	
	@SuppressWarnings("unchecked")
	protected static class BaseEnvironmentBuilder<T extends EnvironmentInfo, B extends BaseEnvironmentBuilder<T,B>>{
		
		protected T env;
		
		protected BaseEnvironmentBuilder(T env) {
			this.env = env;
		}
		
		public B withEnvironmentId(EnvironmentId id) {
			assertNotInvalidated(getClass(), env);
			((EnvironmentInfo)env).environmentId = id;
			return (B) this;
		}

		public B withEnvironmentName(EnvironmentName name) {
			assertNotInvalidated(getClass(), env);
			((EnvironmentInfo)env).environmentName = name;
			return (B) this;
		}

		public B withCategory(String category) {
			assertNotInvalidated(getClass(), env);
			((EnvironmentInfo)env).category = category;
			return (B) this;
		}

		public B withType(String type) {
			assertNotInvalidated(getClass(), env);
			((EnvironmentInfo)env).type = type;
			return (B) this;
		}

		public B withDescription(String description) {
			assertNotInvalidated(getClass(), env);
			((EnvironmentInfo)env).description = description;
			return (B) this;
		}
		
		public T build() {
			try {
				assertNotInvalidated(getClass(), env);
				return env;
			} finally {
				this.env = null;
			}
		}
		
	}
	
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

	
}

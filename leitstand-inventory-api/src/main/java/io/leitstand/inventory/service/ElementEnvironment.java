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

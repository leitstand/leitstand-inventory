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
package io.leitstand.inventory.event;

import io.leitstand.commons.model.BuilderUtil;
import io.leitstand.inventory.service.EnvironmentInfo;

public class ElementEnvironmentEvent extends ElementEvent {

	
	public static class Builder<T extends ElementEnvironmentEvent> extends ElementEventBuilder<T, Builder<T>>{
		
		protected Builder(T event) {
			super(event);
		}
		
		public Builder<T> withEnvironment(EnvironmentInfo.Builder env) {
			return withEnvironment(env.build());
		}
			
		
		public Builder<T> withEnvironment(EnvironmentInfo env) {
			BuilderUtil.assertNotInvalidated(getClass(), object);
			((ElementEnvironmentEvent)object).environment = env;
			return this;
		}
		
	}
	
	
	private EnvironmentInfo environment;
	
	
	public EnvironmentInfo getEnvironment() {
		return environment;
	}
	
}

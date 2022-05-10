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

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import io.leitstand.inventory.service.EnvironmentInfo;

public class ElementEnvironmentEvent<E extends EnvironmentInfo> extends ElementEvent {

	
	public static class Builder<E extends EnvironmentInfo, T extends ElementEnvironmentEvent<E>> extends ElementEventBuilder<T, Builder<E,T>>{
		
		protected Builder(T event) {
			super(event);
		}
		
		public Builder<E,T> withEnvironment(EnvironmentInfo.Builder env) {
			return withEnvironment((E)env.build());
		}
			
		
		public Builder<E,T> withEnvironment(E env) {
			assertNotInvalidated(getClass(), object);
			((ElementEnvironmentEvent)object).environment = env;
			return this;
		}
		
	}
	
	
	private E environment;
	
	
	public E getEnvironment() {
		return environment;
	}
	
}

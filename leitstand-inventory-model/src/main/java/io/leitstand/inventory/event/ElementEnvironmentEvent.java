/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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

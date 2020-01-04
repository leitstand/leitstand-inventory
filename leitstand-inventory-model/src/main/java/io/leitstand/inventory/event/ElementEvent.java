/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

import io.leitstand.inventory.service.BaseElementEnvelope;

public abstract class ElementEvent extends BaseElementEnvelope {

	public static class ElementEventBuilder<T extends ElementEvent,B extends ElementEventBuilder<T, B>> extends BaseElementEnvelopeBuilder<T, B> {
		
		protected ElementEventBuilder(T instance) {
			super(instance);
		}
	}
	
}

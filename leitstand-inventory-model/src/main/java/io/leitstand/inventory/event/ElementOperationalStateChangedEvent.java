/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import io.leitstand.inventory.service.OperationalState;

public class ElementOperationalStateChangedEvent extends ElementEvent {

	public static Builder newElementOperationalStateChangedEvent() {
		return new Builder();
	}
	
	public static class Builder extends ElementEventBuilder<ElementOperationalStateChangedEvent, Builder> {
		protected Builder() {
			super(new ElementOperationalStateChangedEvent());
		}
		
		public Builder withPreviousState(OperationalState previousState) {
			assertNotInvalidated(getClass(), object);
			object.previousOpertionalState = previousState;
			return this;
		}
		
		public Builder withOperationalState(OperationalState operationalState) {
			assertNotInvalidated(getClass(), object);
			object.operationalState = operationalState;
			return this;
		}
	}
	
	private OperationalState operationalState;
	private OperationalState previousOpertionalState;

	public OperationalState getPreviousState() {
		return previousOpertionalState;
	}
	
	public OperationalState getOperationalState() {
		return operationalState;
	}
}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

import io.leitstand.inventory.service.AdministrativeState;

public class ElementRemovedEvent extends ElementEvent {

	public static Builder newElementRemovedEvent() {
		return new Builder();
	}
	
	public static class Builder extends ElementEventBuilder<ElementRemovedEvent, Builder>{
		public Builder() {
			super(new ElementRemovedEvent());
		}

		public Builder withAdministrativeState(AdministrativeState administrativeState) {
			object.administrativeState = administrativeState;
			return this;
		}
	}
	
	private AdministrativeState administrativeState;
	
	public AdministrativeState getAdministrativeState() {
		return administrativeState;
	}
	
}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import io.leitstand.inventory.service.ElementRoleName;

public class ElementRoleChangedEvent extends ElementEvent {

	public static Builder newElementRoleChangedEvent() {
		return new Builder();
	}
	
	public static class Builder extends ElementEventBuilder<ElementRoleChangedEvent, Builder> {
		protected Builder() {
			super(new ElementRoleChangedEvent());
		}
		
		public Builder withPreviousElementRole(ElementRoleName previousRole) {
			assertNotInvalidated(getClass(), object);
			object.previousElementRole = previousRole;
			return this;
		}
		
	}
	
	private ElementRoleName previousElementRole;
	
	public ElementRoleName getPreviousElementRole() {
		return previousElementRole;
	}
}

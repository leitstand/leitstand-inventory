/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import io.leitstand.inventory.service.ElementName;

public class ElementRenamedEvent extends ElementEvent {

	public static Builder newElementRenamedEvent() {
		return new Builder();
	}
	
	public static class Builder extends ElementEventBuilder<ElementRenamedEvent, Builder> {
		protected Builder() {
			super(new ElementRenamedEvent());
		}
		
		public Builder withPreviousName(ElementName previousName) {
			assertNotInvalidated(getClass(), object);
			object.previousName = previousName;
			return this;
		}
		
	}
	
	private ElementName previousName;

	public ElementName getPreviousName() {
		return previousName;
	}
}

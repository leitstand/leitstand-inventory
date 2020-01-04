/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

import io.leitstand.commons.model.BuilderUtil;
import io.leitstand.inventory.service.ElementGroupSettings;

public class ElementMovedEvent extends ElementEvent{

	public static Builder newElementMovedEvent() {
		return new Builder();
	}
	
	public static class Builder extends ElementEventBuilder<ElementMovedEvent, Builder>{
		Builder(){
			super(new ElementMovedEvent());
		}
		
		public Builder withFrom(ElementGroupSettings from) {
			BuilderUtil.assertNotInvalidated(getClass(), object);
			object.from = from;
			return this;
		}

		public Builder withTo(ElementGroupSettings to) {
			BuilderUtil.assertNotInvalidated(getClass(), object);
			object.to = to;
			return this;
		}
	}
	
	private ElementGroupSettings from;
	private ElementGroupSettings to;

	public ElementGroupSettings getFrom() {
		return from;
	}
	
	public ElementGroupSettings getTo() {
		return to;
	}
	
}

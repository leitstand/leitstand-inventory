/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

public class ElementAddedEvent extends ElementEvent {

	public static Builder newElementAddedEvent() {
		return new Builder();
	}
	
	public static class Builder extends ElementEventBuilder<ElementAddedEvent, Builder>{
		public Builder() {
			super(new ElementAddedEvent());
		}
	}
	
}

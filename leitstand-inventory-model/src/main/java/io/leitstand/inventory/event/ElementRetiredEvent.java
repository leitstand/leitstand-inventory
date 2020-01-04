/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

public class ElementRetiredEvent extends ElementEvent {

	public static Builder newElementRetiredEvent() {
		return new Builder();
	}
	
	public static class Builder extends ElementEventBuilder<ElementRetiredEvent, Builder>{
		public Builder() {
			super(new ElementRetiredEvent());
		}
	}
}

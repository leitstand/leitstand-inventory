/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

public class ElementActivatedEvent extends ElementEvent{

	public static Builder newElementActivatedEvent() {
		return new Builder();
	}
	
	public static class Builder extends ElementEventBuilder<ElementActivatedEvent, Builder>{
		
		public Builder() {
			super(new ElementActivatedEvent());
		}
		
	}
}	
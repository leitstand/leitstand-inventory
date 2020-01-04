/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

public class ElementSettingsUpdatedEvent extends ElementEvent{

	public static Builder newElementSettingsUpdatedEvent() {
		return new Builder();
	}
	
	public static class Builder extends ElementEventBuilder<ElementSettingsUpdatedEvent, Builder>{
		public Builder() {
			super(new ElementSettingsUpdatedEvent());
		}
	}
	
	
}

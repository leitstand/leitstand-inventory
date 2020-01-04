/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

public class ElementPhysicalInterfaceRemovedEvent extends ElementPhysicalInterfaceEvent {

	public static Builder newPhysicalInterfaceRemovedEvent() {
		return new Builder();
	}
	
	public static class Builder extends ElementPhysicalInterfaceEventBuilder<ElementPhysicalInterfaceRemovedEvent, Builder>{
		
		public Builder() {
			super(new ElementPhysicalInterfaceRemovedEvent());
		}
	}
	
}

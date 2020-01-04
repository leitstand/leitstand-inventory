/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

public class ElementPhysicalInterfaceStoredEvent extends ElementPhysicalInterfaceEvent {

	public static Builder newPhysicalInterfaceStoredEvent() {
		return new Builder();
	}
	
	public static class Builder extends ElementPhysicalInterfaceEventBuilder<ElementPhysicalInterfaceStoredEvent, Builder>{
		
		public Builder() {
			super(new ElementPhysicalInterfaceStoredEvent());
		}
	}
	
}

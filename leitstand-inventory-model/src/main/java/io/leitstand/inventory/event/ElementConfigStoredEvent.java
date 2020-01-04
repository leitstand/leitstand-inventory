/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

public class ElementConfigStoredEvent extends ElementConfigEvent {

	public static Builder newElementConfigStoredEvent() {
		return new Builder();
	}
	
	public static class Builder extends ElementConfigEventBuilder<ElementConfigStoredEvent, Builder>{
		
		Builder(){
			super(new ElementConfigStoredEvent());
		}

	}
	
}

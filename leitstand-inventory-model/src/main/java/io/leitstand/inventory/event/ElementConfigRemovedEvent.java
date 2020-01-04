/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

public class ElementConfigRemovedEvent extends ElementConfigEvent {

	public static Builder newElementConfigRemovedEvent() {
		return new Builder();
	}
	
	public static class Builder extends ElementConfigEventBuilder<ElementConfigRemovedEvent, Builder>{
		
		Builder(){
			super(new ElementConfigRemovedEvent());
		}
		
	}
	
}

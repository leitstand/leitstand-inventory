/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

public class ElementEnvironmentRemovedEvent extends ElementEnvironmentEvent {

	public static Builder<ElementEnvironmentRemovedEvent> newElementEnvironmentRemovedEvent() {
		return new Builder<>(new ElementEnvironmentRemovedEvent());
	}
	
	
}

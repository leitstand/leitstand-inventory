/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

public class ElementEnvironmentStoredEvent extends ElementEnvironmentEvent {

	public static Builder<ElementEnvironmentStoredEvent> newElementEnvironmentStoredEvent() {
		return new Builder<>(new ElementEnvironmentStoredEvent());
	}
	
	
}

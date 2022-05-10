package io.leitstand.inventory.event;

import io.leitstand.inventory.service.Environment;

public class ElementEnvironmentUploadedEvent extends ElementEnvironmentEvent<Environment> {

	public static Builder<Environment,ElementEnvironmentUploadedEvent> newElementEnvironmentUploadedEvent() {
		return new Builder<>(new ElementEnvironmentUploadedEvent());
	}
	
	
}

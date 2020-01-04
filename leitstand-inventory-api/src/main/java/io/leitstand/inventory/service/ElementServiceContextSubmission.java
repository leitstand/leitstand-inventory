/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import io.leitstand.commons.model.ValueObject;

/**
 * A submission to create a new service context.
 * <p>
 * A service context is an environment to host other services. 
 * By that, the service context is the foundation of the service hierarchy.
 * A service can specify a reference to the parent service context.
 * The service context itself is owned by another service. 
 * This service can be located on the same or on a different element.
 * </p>
 * As an example, ONL forms a service context for linux containers.
 * In turn, every linux container is a service from an ONL perspective but also forms a service context for all services that run inside the container.
 * ONL, LxC and all services are located on the same element.
 * 
 */
public class ElementServiceContextSubmission extends ValueObject {

	private ServiceContextState serviceContextState;
	
	private String serviceCluster;
	

	/**
	 * Returns the service context state.
	 * @return the service context state.
	 */
	public ServiceContextState getServiceContextState() {
		return serviceContextState;
	}

	/**
	 * Returns the service cluster name.
	 * @return the service cluster name.
	 */
	public String getServiceCluster() {
		return serviceCluster;
	}
	
}

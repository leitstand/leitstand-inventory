/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

/**
 * An enumeration of existing service types.
 */
public enum ServiceType {
	/** An operating system hosted by an x86 server*/
	OS,
	/** A virtual machine.*/
	VM,
	/** A container that hosts a service instance.*/
	CONTAINER,
	/** A daemon, i.e. a background process*/
	DAEMON,
	/** A service, i.e. a service that provides a method of interaction for the user*/
	SERVICE
}

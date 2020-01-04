/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

/**
 * An enumeration of all existing image lifecycle states.
 */
public enum ImageState {
	
	/** A new image that has been registered and waits for being promoted to CANDIDATE in order to become eligible for deplyoment.*/
	NEW,
	
	/** A candidate to become the new default image.*/
	CANDIDATE,
	
	/** The image deployed by default.*/ 
	RELEASE,
	
	/** 
	 * An image that must not be deployed anymore. 
	 * Typically, a candidate gets revoked if severe errors have been detected.
	 */
	REVOKED,
	
	/**
	 * A superseded image has been replaced by a new release. 
	 * A superseded image was a default image before and has never been revoked.
	 */
	SUPERSEDED
	
}
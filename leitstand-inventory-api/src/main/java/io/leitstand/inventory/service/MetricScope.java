/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

/**
 * Enumeration of available metric scopes.
 * <p>
 * The metric scope defines on which resource a metric is defined on. 
 * </p>
 */
public enum MetricScope {

	/** Element metric scope. */
	ELEMENT,
	
	/** Logical interface metric. */
	IFL,
	
	/** Physical interface metric.*/
	IFP,
	
	/** Service metric.*/
	SERVICE,
	
	/** All existing metric scopes.*/
	ALL
	
}

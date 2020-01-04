/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

//TODO Javadoc

public interface ElementSettingsService {

	ElementSettings getElementSettings(ElementId id);

	ElementSettings getElementSettings(ElementName name);

	boolean storeElementSettings(ElementSettings settings);

}

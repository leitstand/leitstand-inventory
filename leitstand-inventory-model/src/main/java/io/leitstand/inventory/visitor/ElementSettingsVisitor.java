/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.visitor;

import io.leitstand.inventory.service.ElementManagementInterface;
import io.leitstand.inventory.service.ElementSettings;

public interface ElementSettingsVisitor {

	void visitElementSettings(ElementSettings settings);
	default void visitElementManagementInterface(ElementManagementInterface managementInterface) {}
	
}

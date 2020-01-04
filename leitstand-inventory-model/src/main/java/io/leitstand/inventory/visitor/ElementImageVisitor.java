/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.visitor;

import io.leitstand.inventory.service.ElementInstalledImage;

public interface ElementImageVisitor {

	void visitElementImage(ElementInstalledImage image);
	
}

/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.visitor;

import io.leitstand.inventory.service.ElementConfig;
import io.leitstand.inventory.service.ElementConfigReference;

public interface ElementConfigVisitor {

	default String getConfigSelector() {
		return null;
	}
	boolean visitElementConfig(ElementConfigReference config);
	
	default void visitElementConfig(ElementConfig config) {
		
	}
	
}

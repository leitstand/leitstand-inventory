/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.visitor;

public interface ElementVisitor {
	
	default ElementSettingsVisitor visitElementSettings() {
		return null;
	}

	default ElementConfigVisitor visitElementConfigs() {
		return null;
	}
	
	default ElementImageVisitor visitElementImages() {
		return null;
	}
	
	default ImageVisitor visitDefaultImages() {
		return null;
	}
	
}
/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.visitor;

import io.leitstand.inventory.service.ImageInfo;
import io.leitstand.inventory.service.ImageState;
import io.leitstand.inventory.service.ImageType;

public interface ImageVisitor {
	void visitImage(ImageInfo image);
	
	default ImageState getImageState() {
		return null;
	}
	
	default ImageType getImageType() {
		return null;
	}
}

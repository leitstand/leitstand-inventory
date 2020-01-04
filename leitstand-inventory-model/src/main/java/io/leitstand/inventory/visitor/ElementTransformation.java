/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.visitor;

public interface ElementTransformation<T> extends ElementVisitor {

	T getResult();
	
}

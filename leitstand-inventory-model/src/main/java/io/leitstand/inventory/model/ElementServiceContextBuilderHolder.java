/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import io.leitstand.inventory.service.ElementServiceContext;

class ElementServiceContextBuilderHolder {

	Long id;
	Long parent;
	ElementServiceContext.Builder builder;
	
	public ElementServiceContextBuilderHolder(Long id, Long parent, ElementServiceContext.Builder builder) {
		this.id 	 = id;
		this.parent  = parent;
		this.builder = builder;
	}
	
	public Long getId() {
		return id;
	}
	
	public Long getParent() {
		return parent;
	}
	
	public ElementServiceContext.Builder getBuilder() {
		return builder;
	}
}

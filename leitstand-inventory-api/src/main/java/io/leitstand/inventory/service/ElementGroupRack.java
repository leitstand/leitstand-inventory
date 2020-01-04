/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Collections.unmodifiableList;

import java.util.LinkedList;
import java.util.List;

public class ElementGroupRack extends BaseElementGroupEnvelope {

	public static Builder newElementGroupRack() {
		return new Builder();
	}
	
	public static class Builder extends BaseElementGroupEnvelopeBuilder<ElementGroupRack, Builder>{
		 
		public Builder() {
			super(new ElementGroupRack());
		}
		
		public Builder withRack(RackSettings rack) {
			assertNotInvalidated(getClass(), object);
			object.rack = rack;
			return this;
		}

		public Builder withElements(List<RackItem> elements) {
			assertNotInvalidated(getClass(), object);
			object.elements = new LinkedList<>(elements);
			return this;
		}
	}
	
	private RackSettings rack;
	private List<RackItem> elements;
	
	public RackSettings getRack() {
		return rack;
	}
	
	public List<RackItem> getElements() {
		return unmodifiableList(elements);
	}
}

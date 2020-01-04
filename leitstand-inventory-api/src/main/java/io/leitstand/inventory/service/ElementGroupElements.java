/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Collections.unmodifiableList;

import java.util.LinkedList;
import java.util.List;


public class ElementGroupElements extends BaseElementGroupEnvelope {
	
	public static Builder newElementGroupElements(){
		return new Builder();
	}
	
	public static class Builder extends BaseElementGroupEnvelopeBuilder<ElementGroupElements, Builder>{
		
		protected Builder(){
			super(new ElementGroupElements());
		}
		
		public Builder withDescription(String description){
			assertNotInvalidated(getClass(), object);
			object.description = description;
			return this;
		}
		
		public Builder withElements(List<ElementSettings> elements){
			assertNotInvalidated(getClass(), object);
			object.elements = unmodifiableList(new LinkedList<>(elements));
			return this;
		}

	}
	
	private String description;
	private List<ElementSettings> elements;

	public String getDescription() {
		return description;
	}
	
	public List<ElementSettings> getElements() {
		return elements;
	}

}

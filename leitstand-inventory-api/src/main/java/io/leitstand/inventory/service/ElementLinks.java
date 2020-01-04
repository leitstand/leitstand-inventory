/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

/**
 * A summary of all lists of a certain element.
 */

public class ElementLinks extends BaseElementEnvelope {
	
	/**
	 * Returns a builder to create an immutable <code>ElementLinks</code> instance.
	 * @return a builder to create an immutable <code>ElementLinks</code> instance.
	 */
	public static Builder newElementLinks(){
		return new Builder();
	}
	
	/**
	 * The builder to create an immutable <code>ElementLinks</code> instance.
	 */
	public static final class Builder extends BaseElementEnvelopeBuilder<ElementLinks, Builder>{
		
		public Builder() {
			super(new ElementLinks());
		}
		
		/**
		 * Sets the list of element links.
		 * @param links - the links of this element
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withLinks(ElementLinkData.Builder... links){
			return withLinks(stream(links)
							 .map(ElementLinkData.Builder::build)
							 .collect(toList()));
		}
		
		/**
		 * Sets the list of element links.
		 * @param links - the links of this element
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withLinks(ElementLinkData... links){
			return withLinks(asList(links));
		}
		
		/**
		 * Sets the list of element links.
		 * @param links - the links of this element
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withLinks(List<ElementLinkData> links){
			assertNotInvalidated(getClass(), object);
			object.links = unmodifiableList(new ArrayList<ElementLinkData>(links));
			return this;
		}
		
		@Override
		public ElementLinks build(){
			try{
				assertNotInvalidated(getClass(), object);
				return object;
			} finally {
				object = null;
			}
		}
	}

	@Valid
	private List<ElementLinkData> links;
	
	/**
	 * Returns an unmodifiable list of all element links.
	 * @return the list of element links.
	 */
	public List<ElementLinkData> getLinks() {
		return unmodifiableList(links);
	}
	
}

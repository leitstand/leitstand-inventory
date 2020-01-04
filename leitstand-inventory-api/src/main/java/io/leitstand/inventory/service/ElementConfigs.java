/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Collections.unmodifiableList;

import java.util.LinkedList;
import java.util.List;

public class ElementConfigs extends BaseElementEnvelope {

	public static Builder newElementConfigs() {
		return new Builder();
	}
	
	public static class Builder extends BaseElementEnvelopeBuilder<ElementConfigs, Builder>{
		
		protected Builder() {
			super(new ElementConfigs());
		}
		
		public Builder withConfigs(List<ElementConfigReference> configs) {
			assertNotInvalidated(getClass(), object);
			object.configs = new LinkedList<>(configs);
			return this;
		}
		
	}
	
	
	private List<ElementConfigReference> configs;
	
	public List<ElementConfigReference> getConfigs() {
		return unmodifiableList(configs);
	}
	
}

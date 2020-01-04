/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.List;

public class ElementConfigRevisions extends BaseElementEnvelope{
	
	public static Builder newElementConfigRevisions() {
		return new Builder();
	}
	
	public static class Builder extends BaseElementEnvelopeBuilder<ElementConfigRevisions,Builder>{
		
		protected Builder() {
			super(new ElementConfigRevisions());
		}
		
		public Builder withElementConfigRevisions(ElementConfigReference.Builder... revisions) {
			return withElementConfigRevisions(stream(revisions)
											  .map(ElementConfigReference.Builder::build)
											  .collect(toList()));
		}
		
		public Builder withElementConfigRevisions(List<ElementConfigReference> revisions) {
			assertNotInvalidated(getClass(), object);
			object.revisions = revisions;
			return this;
		}

		public Builder withElementConfigName(ElementConfigName configName) {
			assertNotInvalidated(getClass(), object);
			object.configName = configName;
			return this;
		}
		
	}
	

	private ElementConfigName configName;
	private List<ElementConfigReference> revisions;
	
	public ElementConfigName getConfigName() {
		return configName;
	}
	
	public List<ElementConfigReference> getRevisions() {
		return unmodifiableList(revisions);
	}
	
}

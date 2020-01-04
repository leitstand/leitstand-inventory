/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.List;

public class ElementGroupRacks extends BaseElementGroupEnvelope {

	public static Builder newElementGroupRacks() {
		return new Builder();
	}
	
	public static class Builder extends BaseElementGroupEnvelopeBuilder<ElementGroupRacks, Builder>{
		 
		public Builder() {
			super(new ElementGroupRacks());
		}
		
		public Builder withRacks(RackSettings.Builder... racks) {
			return withRacks(stream(racks)
							 .map(RackSettings.Builder::build)
							 .collect(toList()));
		}
		
		public Builder withRacks(List<RackSettings> racks) {
			assertNotInvalidated(getClass(), object);
			object.racks = racks;
			return this;
		}
	}
	
	private List<RackSettings> racks;
	
	public List<RackSettings> getRacks() {
		return unmodifiableList(racks);
	}
}

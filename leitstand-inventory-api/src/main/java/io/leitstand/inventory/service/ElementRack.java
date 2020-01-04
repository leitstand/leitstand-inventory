/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.LinkedList;
import java.util.List;

public class ElementRack extends BaseElementEnvelope{

	public static Builder newElementRack() {
		return new Builder();
	}
	
	public static class Builder  extends BaseElementEnvelopeBuilder<ElementRack,Builder> {
	
		public Builder () {
			super(new ElementRack());
		}
		
		public Builder withRackName(RackName rackName) {
			assertNotInvalidated(getClass(), object);
			((ElementRack)object).rackName = rackName;
			return (Builder) this;
		}
		
		public Builder withRackUnits(int units) {
			assertNotInvalidated(getClass(), object);
			((ElementRack)object).units = units;
			return (Builder) this;
		}

		public Builder withDescription(String description) {
			assertNotInvalidated(getClass(), object);
			((ElementRack)object).description = description;
			return (Builder) this;
		}

		public Builder withLocation(String location) {
			assertNotInvalidated(getClass(), object);
			((ElementRack)object).location = location;
			return (Builder) this;
		}

		public Builder withRackItems(RackItem.Builder... items) {
			return withRackItems(stream(items)
								 .map(RackItem.Builder::build)
								 .collect(toList()));
		}

		public Builder withRackItems(List<RackItem> items) {
			assertNotInvalidated(getClass(), object);
			object.elements = new LinkedList<>(items);
			return this;
		}
		
	}

	private RackName rackName;
	private int units;
	private String location;
	private String description;
	private List<RackItem> elements;

	public RackName getRackName() {
		return rackName;
	}
	
	public String getLocation() {
		return location;
	}
	
	public String getDescription() {
		return description;
	}

	public List<RackItem> getElements() {
		return unmodifiableList(elements);
	}
	
	public int getUnits() {
		return units;
	}
}

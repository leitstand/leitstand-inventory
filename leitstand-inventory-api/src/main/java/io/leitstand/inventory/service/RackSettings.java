/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import io.leitstand.commons.model.CompositeValue;

public class RackSettings extends CompositeValue{
	
	public static Builder newRackSettings() {
		return new Builder();
	}
	
	public static class Builder {
		
		private RackSettings rack = new RackSettings();
		
		public Builder withRackName(RackName rackName) {
			assertNotInvalidated(getClass(), rack);
			rack.rackName = rackName;
			return this;
		}

		public Builder withDescription(String description) {
			assertNotInvalidated(getClass(), rack);
			rack.description = description;
			return this;
		}

		public Builder withLocation(String location) {
			assertNotInvalidated(getClass(), rack);
			rack.location = location;
			return this;
		}
		
		public Builder withUnits(int slots) {
			assertNotInvalidated(getClass(), rack);
			rack.units = slots;
			return this;
		}

		public RackSettings build() {
			try {
				assertNotInvalidated(getClass(), rack);
				return rack;
			} finally {
				this.rack = null;
			}
		}
		
	}

	private RackName rackName;
	private String location;
	private String description;
	private int units;

	
	public RackName getRackName() {
		return rackName;
	}
	
	public String getLocation() {
		return location;
	}
	
	public String getDescription() {
		return description;
	}

	public int getUnits() {
		return units;
	}
	
	
}

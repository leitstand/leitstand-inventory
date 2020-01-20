/*
 * Copyright 2020 RtBrick Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
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

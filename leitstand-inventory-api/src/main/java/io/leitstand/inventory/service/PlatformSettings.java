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
import static io.leitstand.inventory.service.PlatformId.randomPlatformId;

import javax.json.bind.annotation.JsonbProperty;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.leitstand.commons.model.ValueObject;


public class PlatformSettings extends ValueObject {

	public static Builder newPlatformSettings() {
		return new Builder();
	}
	
	public static class Builder {
		
		private PlatformSettings settings = new PlatformSettings();
		
		public Builder withPlatformId(PlatformId platformId) {
			assertNotInvalidated(getClass(),settings);
			settings.platformId = platformId;
			return this;
		}
		
		public Builder withVendorName(String vendorName) {
			assertNotInvalidated(getClass(), settings);
			settings.vendorName = vendorName;
			return this;
		}
		
		public Builder withModelName(String modelName) {
			assertNotInvalidated(getClass(), settings);
			settings.modelName = modelName;
			return this;
		}
		
		public Builder withDescription(String description) {
			assertNotInvalidated(getClass(), settings);
			settings.description = description;
			return this;
		}
		
		public Builder withRackUnits(int units) {
			assertNotInvalidated(getClass(), settings);
			settings.rackUnits = units;
			return this;
		}
		
		public Builder withHalfRackSize(boolean halfRackSize) {
			assertNotInvalidated(getClass(), settings);
			settings.halfRackSize = halfRackSize;
			return this;
		}
		
		public PlatformSettings build() {
			try {
				return settings;
			} finally {
				this.settings = null;
			}
		}
	}
	
	private PlatformId platformId = randomPlatformId();
	
	@NotNull(message="{vendor_name.required}")
	private String vendorName;
	
	@NotNull(message="{model_name.required}")
	private String modelName;
	
	private String description;
	
	@Min(value=1, message="{rack_units.must_be_greate_than_zero}")
	private int rackUnits;
	
	@JsonbProperty("half_rack")
	private boolean halfRackSize;
	
	public PlatformId getPlatformId() {
		return platformId;
	}
	
	public String getVendorName() {
		return vendorName;
	}
	
	public String getModelName() {
		return modelName;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getRackUnits() {
		return rackUnits;
	}
	
	public boolean isHalfRackSize() {
		return halfRackSize;
	}
	
}


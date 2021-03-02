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

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.leitstand.commons.model.ValueObject;


/**
 * Platform settings.
 */
public class PlatformSettings extends ValueObject {

    /**
     * Creates a builder for an immutable <code>PlatformSettings</code> value object.
     * @return a builder for an immutable <code>PlatformSettings</code> value object.
     */
	public static Builder newPlatformSettings() {
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>PlatformSettings</code> value object.
	 */
	public static class Builder {
		
		private PlatformSettings settings = new PlatformSettings();

		/**
		 * Sets the platform ID.
		 * @param platformId the platform ID.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withPlatformId(PlatformId platformId) {
			assertNotInvalidated(getClass(),settings);
			settings.platformId = platformId;
			return this;
		}
	   
		/**
         * Sets the platform name.
         * @param platformName the platform name.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withPlatformName(PlatformName platformName) {
			assertNotInvalidated(getClass(),settings);
			settings.platformName = platformName;
			return this;
		}
		
        /**
         * Sets the platform chipset name.
         * @param platformChipset the platform chipset name.
         * @return a reference to this builder to continue object creation.
         */
		
		public Builder withPlatformChipset(PlatformChipsetName platformChipset) {
			assertNotInvalidated(getClass(),settings);
			settings.platformChipset = platformChipset;
			return this;
		}
		
	    /**
         * Sets the vendor name.
         * @param vendorName the vendor name.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withVendorName(String vendorName) {
			assertNotInvalidated(getClass(), settings);
			settings.vendorName = vendorName;
			return this;
		}
		
        /**
         * Sets the model name.
         * @param modelName the model name.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withModelName(String modelName) {
			assertNotInvalidated(getClass(), settings);
			settings.modelName = modelName;
			return this;
		}
		
        /**
         * Sets the platform description.
         * @param description the platform description.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withDescription(String description) {
			assertNotInvalidated(getClass(), settings);
			settings.description = description;
			return this;
		}
		
        /**
         * Sets the platform height in rack units.
         * @param units the platform height in rack units.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withRackUnits(int units) {
			assertNotInvalidated(getClass(), settings);
			settings.rackUnits = units;
			return this;
		}
		
		/**
		 * Creates an immutable <code>PlatformSettings</code> value object and invalidates this builder.
		 * Subsequent invocations of the <code>build()</code> method raise an exception.
		 * @return an immutable <code>PlatformSettings</code> value object.
		 */
		public PlatformSettings build() {
			try {
				return settings;
			} finally {
				this.settings = null;
			}
		}

	}
	
	private PlatformId platformId = randomPlatformId();
	@NotNull(message="{platform_name.required}")
	private PlatformName platformName;
	
	@NotNull(message="{platform_chipset.required}")
	private PlatformChipsetName platformChipset;
	
	@NotNull(message="{vendor_name.required}")
	private String vendorName;
	
	@NotNull(message="{model_name.required}")
	private String modelName;
	
	private String description;
	
	@Min(value=1, message="{rack_units.must_be_greater_than_zero}")
	private int rackUnits;
	
	/**
	 * Returns the platform ID.
	 * @return the platform ID.
	 */
	public PlatformId getPlatformId() {
		return platformId;
	}
	
	/**
	 * Returns the platform name.
	 * @return the platform name.
	 */
	public PlatformName getPlatformName() {
		return platformName;
	}
	
	/**
	 * Returns the platform chipset name.
	 * @return the platform chipset name.
	 */
	public PlatformChipsetName getPlatformChipset() {
		return platformChipset;
	}
	
	/**
	 * Returns the vendor name.
	 * @return the vendor name.
	 */
	public String getVendorName() {
		return vendorName;
	}
	
	/**
	 * Returns the model name.
	 * @return the model name.
	 */
	public String getModelName() {
		return modelName;
	}
	
	/**
	 * Returns the platform description.
	 * @return the platform description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the platform height in rack units.
	 * @return the platform height in rack units.
	 */
	public int getRackUnits() {
		return rackUnits;
	}
	
}


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

/**
 * General rack settings.
 */
public class RackSettings extends BaseRackEnvelope {
	
    /**
     * Creates a builder for an immutable <code>RackSettings</code> value object.
     * @return a builder for an immutable <code>RackSettings</code> value object.
     */
    public static Builder newRackSettings() {
		return new Builder();
	}
	
    /**
     * A builder for an immutable <code>RackSettings</code> value object.
     */
	public static class Builder extends BaseRackEnvelopeBuilder<RackSettings, Builder> {
		
	    /**
	     * Creates a builder for an immutable <code>RackSettings</code> value object.
	     */
		protected Builder() {
			super(new RackSettings());
		}
		
		/**
		 * Sets the rack serial number.
		 * @param serialNumber the serial number
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withSerialNumber(String serialNumber) {
			assertNotInvalidated(getClass(), rack);
			rack.serialNumber = serialNumber;
			return this;
		}

		/**
		 * Sets the rack asset ID
		 * @param assetId the asset ID
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withAssetId(String assetId) {
			assertNotInvalidated(getClass(), rack);
			rack.assetId = assetId;
			return this;
		}
		
		/**
		 * Sets the rack location.
		 * @param location the rack location.
		 * @return a reference of this builder to continue object creation.
		 */
		public Builder withLocation(String location) {
			assertNotInvalidated(getClass(), rack);
			rack.location = location;
			return this;
		}
		
	}

	private String location;
	private String serialNumber;
	private String assetId;
	
	/**
	 * Returns the rack serial number.
	 * @return the rack serial number.
	 */
	public String getSerialNumber() {
		return serialNumber;
	}
	
	/**
	 * Returns the rack asset ID.
	 * @return the rack asset ID.
	 */
	public String getAssetId() {
		return assetId;
	}
	
	/**
	 * Returns the rack location.
	 * @return the rack location.
	 */
	public String getLocation() {
		return location;
	}
	
}

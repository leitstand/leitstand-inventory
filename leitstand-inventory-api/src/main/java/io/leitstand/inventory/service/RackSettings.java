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

public class RackSettings extends BaseRackEnvelope {
	
	public static Builder newRackSettings() {
		return new Builder();
	}
	
	public static class Builder extends BaseRackEnvelopeBuilder<RackSettings, Builder> {
		
		protected Builder() {
			super(new RackSettings());
		}
		
		public Builder withSerialNumber(String serialNumber) {
			assertNotInvalidated(getClass(), rack);
			rack.serialNumber = serialNumber;
			return this;
		}

		public Builder withAssetId(String assetId) {
			assertNotInvalidated(getClass(), rack);
			rack.assetId = assetId;
			return this;
		}
		
		public Builder withLocation(String location) {
			assertNotInvalidated(getClass(), rack);
			rack.location = location;
			return this;
		}
		
	}

	private String location;
	private String serialNumber;
	private String assetId;
	
	public String getSerialNumber() {
		return serialNumber;
	}
	
	public String getAssetId() {
		return assetId;
	}
	
	public String getLocation() {
		return location;
	}
	
}

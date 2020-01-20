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

import java.io.Serializable;

import javax.persistence.Embeddable;

import io.leitstand.commons.model.ValueObject;
@Embeddable
public class Geolocation extends ValueObject implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public static Builder newGeolocation() {
		return new Builder();
	}
	
	public static class Builder {
		private Geolocation instance = new Geolocation();
		
		public Builder withLongitude(double lon) {
			assertNotInvalidated(getClass(), instance);
			instance.longitude = lon;
			return this;
		}
		
		public Builder withLatitude(double lat) {
			assertNotInvalidated(getClass(), instance);
			instance.latitude = lat;
			return this;
		}
		
		public Geolocation build() {
			try {
				assertNotInvalidated(getClass(),instance);
				return instance;
			} finally {
				this.instance = null;
			}
		}
	}
	
	private double longitude;
	private double latitude;
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
}

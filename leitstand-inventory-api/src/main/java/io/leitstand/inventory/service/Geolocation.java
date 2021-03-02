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

/**
 * Geo-location information as longitude and latitude coordinates.
 */
public class Geolocation extends ValueObject implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a builder for an immutable <code>Geolocation</code> value object.
	 * @return a builder for an immutable <code>Geolocation</code> value object.
	 */
	public static Builder newGeolocation() {
		return new Builder();
	}
	
	/**
     * A builder for an immutable <code>Geolocation</code> value object.
	 */
	public static class Builder {
		private Geolocation instance = new Geolocation();
		
		/**
		 * Sets the longtitude coordinate.
		 * @param lon the longtitude coordinate.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withLongitude(double lon) {
			assertNotInvalidated(getClass(), instance);
			instance.longitude = lon;
			return this;
		}

	    /**
         * Sets the latitude coordinate.
         * @param lon the latitude coordinate.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withLatitude(double lat) {
			assertNotInvalidated(getClass(), instance);
			instance.latitude = lat;
			return this;
		}
		
		/**
		 * Creates an immutable <code>Geolocation</code> value object and invalidates this builder.
		 * Subsequent invocations of the <code>build()</code> method raise an exception.
		 * @return an immutable <code>Geolocation</code> value object.
		 */
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
	
	/**
	 * Returns the latitude coordinate.
	 * @return the latitude coordinate.
	 */
	public double getLatitude() {
		return latitude;
	}
	
	/**
     * Returns the longitude coordinate.
     * @return the longitude coordinate.
     */
	public double getLongitude() {
		return longitude;
	}
}

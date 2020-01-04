/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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

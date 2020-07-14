package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static io.leitstand.inventory.service.FacilityId.randomFacilityId;

import io.leitstand.commons.model.ValueObject;

public class FacilitySettings extends ValueObject {

	public static Builder newFacilitySettings() {
		return new Builder();
	}
	
	public static class Builder {
		private FacilitySettings facility = new FacilitySettings();
		
		public Builder withFacilityId(FacilityId facilityId) {
			assertNotInvalidated(getClass(), facility);
			facility.facilityId = facilityId;
			return this;
		}
		
		public Builder withFacilityName(FacilityName facilityName) {
			assertNotInvalidated(getClass(), facility);
			facility.facilityName = facilityName;
			return this;
		}
		
		public Builder withFacilityType(FacilityType type) {
			assertNotInvalidated(getClass(), facility);
			facility.facilityType = type;
			return this;
		}
		
		public Builder withCategory(String category) {
			assertNotInvalidated(getClass(), facility);
			facility.category = category;
			return this;
		}
		
		public Builder withLocation(String location) {
			assertNotInvalidated(getClass(), facility);
			facility.location = location;
			return this;
		}
		
		public Builder withGeolocation(Geolocation.Builder location) {
			return withGeolocation(location.build());
		}
		
		public Builder withGeolocation(Geolocation location) {
			assertNotInvalidated(getClass(), facility);
			facility.geolocation = location;
			return this;
		}
		
		public Builder withDescription(String description) {
			assertNotInvalidated(getClass(), facility);
			facility.description = description;
			return this;
		}
		
		public FacilitySettings build() {
			try {
				assertNotInvalidated(getClass(), facility);
				return facility;
			} finally {
				this.facility = null;
			}
		}
	}
	
	
	private FacilityId facilityId = randomFacilityId();
	private FacilityName facilityName;
	private FacilityType facilityType;
	private String category;
	private String description;
	private String location;
	private Geolocation geolocation;	
	public FacilityId getFacilityId() {
		return facilityId;
	}
	
	public FacilityName getFacilityName() {
		return facilityName;
	}
	
	public FacilityType getFacilityType() {
		return facilityType;
	}

	public String getCategory() {
		return category;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getLocation() {
		return location;
	}
	
	public Geolocation getGeolocation() {
		return geolocation;
	}
}

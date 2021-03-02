package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static io.leitstand.inventory.service.FacilityId.randomFacilityId;

import io.leitstand.commons.model.ValueObject;

/**
 * Network facility settings.
 */
public class FacilitySettings extends ValueObject {

    /**
     * Creates a builder for an immutable <code>FacilitySettings</code> value object.
     * @return a builder for an immutable <code>FacilitySettings</code> value object.
     */
	public static Builder newFacilitySettings() {
		return new Builder();
	}
	
	/**
	 * A builder for immutable <code>FacilitySettings</code> value object.
	 */
	public static class Builder {
		private FacilitySettings facility = new FacilitySettings();
		
		/**
		 * Sets the facility ID.
		 * @param facilityId the facility ID.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withFacilityId(FacilityId facilityId) {
			assertNotInvalidated(getClass(), facility);
			facility.facilityId = facilityId;
			return this;
		}

	    /**
         * Sets the facility name.
         * @param facilityId the facility name.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withFacilityName(FacilityName facilityName) {
			assertNotInvalidated(getClass(), facility);
			facility.facilityName = facilityName;
			return this;
		}
		
	    /**
         * Sets the facility type.
         * @param facilityId the facility type.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withFacilityType(FacilityType type) {
			assertNotInvalidated(getClass(), facility);
			facility.facilityType = type;
			return this;
		}
		
	    /**
         * Sets the facility category.
         * @param facilityId the facility category.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withCategory(String category) {
			assertNotInvalidated(getClass(), facility);
			facility.category = category;
			return this;
		}
		
		
	    /**
         * Sets the facility location.
         * @param facilityId the facility location.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withLocation(String location) {
			assertNotInvalidated(getClass(), facility);
			facility.location = location;
			return this;
		}
		
		
	    /**
         * Sets the facility geo-location.
         * @param facilityId the facility geo-location.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withGeolocation(Geolocation.Builder location) {
			return withGeolocation(location.build());
		}

	    /**
         * Sets the facility geo-location.
         * @param facilityId the facility geo-location.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withGeolocation(Geolocation location) {
			assertNotInvalidated(getClass(), facility);
			facility.geolocation = location;
			return this;
		}

	    /**
         * Sets the facility description.
         * @param facilityId the facility description.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withDescription(String description) {
			assertNotInvalidated(getClass(), facility);
			facility.description = description;
			return this;
		}
		
		/**
		 * Returns an immutable <code>FacilitySettings</code> value object and invalidates this builder.
		 * Subsequent invocations of the <code>build()</code> method raises an exception.
		 * @return an immutable <code>FacilitySettings</code> value object.
		 */
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
	
	/**
	 * Returns the facility ID.
	 * @return the facility ID.
	 */
	public FacilityId getFacilityId() {
		return facilityId;
	}
	
	/**
	 * Returns the facility name.
	 * @return the facility name.
	 */
	public FacilityName getFacilityName() {
		return facilityName;
	}
	
	/**
	 * Returns the facility type.
	 * @return the facility type.
	 */
	public FacilityType getFacilityType() {
		return facilityType;
	}

	/**
	 * Returns the facility category.
	 * @return the facility category.
	 */
	public String getCategory() {
		return category;
	}
	
	/**
	 * Returns the facility description. 
	 * @return the facility description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Returns the facility location.
	 * @return the facility location.
	 */
	public String getLocation() {
		return location;
	}
	
	/**
	 * Returns the facility geo-location.
	 * @return the facility geo-location.
	 */
	public Geolocation getGeolocation() {
		return geolocation;
	}
}

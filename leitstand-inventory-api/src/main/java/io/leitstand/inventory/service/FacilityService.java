package io.leitstand.inventory.service;

import java.util.List;

/**
 * A transactional service to manage network facility settings.
 */
public interface FacilityService {

    /**
     * Returns all matching network facilities ordered by the facility name.
     * @param filter a regular expression to filter facilities by their name.
     * @return the list of matching network facilities.
     */
	List<FacilitySettings> findFacilities(String filter);
	
	/**
	 * Returns the facility settings.
	 * @param facilityId the facility ID.
	 * @return the facility settings
	 */
	FacilitySettings getFacility(FacilityId facilityId);
	
	/**
	 * Returns the facility settings.
	 * @param facilityName the facility name.
	 * @return the facility settings.
	 */
	FacilitySettings getFacility(FacilityName facilityName);
	
	/**
	 * Stores the facility settings.
	 * @param settings the facility settings
	 * @return <code>true</code> if a new facility was created and <code>false</code> if an existing facility got updated.
	 */
	boolean storeFacility(FacilitySettings settings);
	
	/**
	 * Removes a network facility.
	 * @param facilityId the facility ID
	 */
	void removeFacility(FacilityId facilityId);
	
	/**
	 * Removes a network facility.
	 * @param facilityName the facility name
	 */
	void removeFacility(FacilityName facilityName);
	
}
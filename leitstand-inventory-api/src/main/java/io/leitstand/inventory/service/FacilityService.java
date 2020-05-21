package io.leitstand.inventory.service;

import java.util.List;

public interface FacilityService {

	List<FacilitySettings> findFacilities(String filter);
	FacilitySettings getFacility(FacilityId facilityId);
	FacilitySettings getFacility(FacilityName facilityName);
	boolean storeFacility(FacilitySettings settings);
	void removeFacility(FacilityId facilityId);
	void removeFacility(FacilityName facilityName);
	
	
}

package io.leitstand.inventory.service;

import static io.leitstand.inventory.service.FacilityId.randomFacilityId;
import static io.leitstand.inventory.service.FacilityName.facilityName;
import static io.leitstand.inventory.service.FacilitySettings.newFacilitySettings;
import static io.leitstand.inventory.service.FacilityType.facilityType;
import static io.leitstand.inventory.service.Geolocation.newGeolocation;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class FacilityValueObjectTest {
    
    private static FacilityId FACILITY_ID = randomFacilityId();
    private static FacilityName FACILITY_NAME = facilityName("facility");
    private static FacilityType FACILITY_TYPE = facilityType("type");
    
    @Test
    public void test_facility_settings() {
        Geolocation geoloaction = newGeolocation()
                                  .withLatitude(12.9117891)
                                  .withLongitude(77.6356586)
                                  .build();
        
        FacilitySettings facility = newFacilitySettings()
                                    .withCategory("category")
                                    .withDescription("description")
                                    .withFacilityId(FACILITY_ID)
                                    .withFacilityName(FACILITY_NAME)
                                    .withFacilityType(FACILITY_TYPE)
                                    .withLocation("location")
                                    .withGeolocation(geoloaction)
                                    .build();
        
        assertThat(facility.getCategory(), is("category"));
        assertThat(facility.getDescription(),is("description"));
        assertThat(facility.getFacilityId(),is(FACILITY_ID));
        assertThat(facility.getFacilityName(),is(FACILITY_NAME));
        assertThat(facility.getFacilityType(),is(FACILITY_TYPE));
        assertThat(facility.getLocation(),is("location"));
        assertEquals(geoloaction.getLatitude(), facility.getGeolocation().getLatitude(),0.00005d);
        assertEquals(geoloaction.getLongitude(), facility.getGeolocation().getLongitude(),0.00005d);

    }

}

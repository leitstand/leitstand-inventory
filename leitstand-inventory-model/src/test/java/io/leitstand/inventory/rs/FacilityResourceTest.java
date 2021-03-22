package io.leitstand.inventory.rs;

import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.inventory.service.FacilityId.randomFacilityId;
import static io.leitstand.inventory.service.FacilityName.facilityName;
import static io.leitstand.inventory.service.FacilitySettings.newFacilitySettings;
import static io.leitstand.inventory.service.ReasonCode.IVT0603E_FACILITY_NAME_ALREADY_IN_USE;
import static io.leitstand.testing.ut.Answers.ROLLBACK;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.UniqueKeyConstraintViolationException;
import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.FacilityId;
import io.leitstand.inventory.service.FacilityName;
import io.leitstand.inventory.service.FacilityService;
import io.leitstand.inventory.service.FacilitySettings;

@RunWith(MockitoJUnitRunner.class)
public class FacilityResourceTest {
	
	private static final FacilityId   FACILITY_ID   = randomFacilityId();
	private static final FacilityName FACILITY_NAME = facilityName("test");

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private FacilityService service;
	
	@Mock
	private Messages messages;
	
	@InjectMocks
	private FacilityResource resource = new FacilityResource();
	
	@Test
	public void find_facilities() {
		resource.getFacilities("filter");
		verify(service).findFacilities("filter");
	}
	
	@Test
	public void find_facility_by_id() {
		resource.getFacility(FACILITY_ID);
		verify(service).getFacility(FACILITY_ID);
	}
	
	@Test
	public void find_facility_by_name() {
		resource.getFacility(FACILITY_NAME);
		verify(service).getFacility(FACILITY_NAME);
	}
	
	@Test
	public void remove_facility_by_id() {
		resource.removeFacility(FACILITY_ID);
		verify(service).removeFacility(FACILITY_ID);
	}
	
	@Test
	public void remove_facility_by_name() {
		resource.removeFacility(FACILITY_NAME);
		verify(service).removeFacility(FACILITY_NAME);
	}
	
	@Test
	public void add_facility() {
		FacilitySettings facility = newFacilitySettings()
									.withFacilityId(FACILITY_ID)
									.withFacilityName(FACILITY_NAME)
									.build();
		when(service.storeFacility(facility)).thenReturn(true);

		Response response = resource.storeFacility(facility);
		assertThat(response.getStatus(),is(201));
	}
	
    @Test
    public void store_facility() {
        FacilitySettings facility = newFacilitySettings()
                                    .withFacilityId(FACILITY_ID)
                                    .withFacilityName(FACILITY_NAME)
                                    .build();

        Response response = resource.storeFacility(facility);
        assertThat(response.getStatus(),is(200));
    }

	@Test
	public void cannot_update_facility_uuid() {
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));
		
		FacilitySettings facility = newFacilitySettings()
									.withFacilityId(FACILITY_ID)
									.withFacilityName(FACILITY_NAME)
									.build();

		resource.storeFacility(randomFacilityId(), facility);
	}
	
	@Test
	public void report_facility_name_unique_key_constraint_violation() {
	    exception.expect(UniqueKeyConstraintViolationException.class);
	    exception.expect(reason(IVT0603E_FACILITY_NAME_ALREADY_IN_USE));
	    
	    FacilitySettings facility = newFacilitySettings()
                                    .withFacilityId(FACILITY_ID)
                                    .withFacilityName(FACILITY_NAME)
                                    .build();
	    
	    when(service.storeFacility(facility)).then(ROLLBACK);
	    when(service.getFacility(FACILITY_NAME)).thenReturn(facility);
	    
	    resource.storeFacility(facility);
	}
	
}

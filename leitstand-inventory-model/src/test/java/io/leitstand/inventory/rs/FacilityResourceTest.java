package io.leitstand.inventory.rs;

import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.inventory.service.FacilityId.randomFacilityId;
import static io.leitstand.inventory.service.FacilityName.facilityName;
import static io.leitstand.inventory.service.FacilitySettings.newFacilitySettings;
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

import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.inventory.service.FacilityId;
import io.leitstand.inventory.service.FacilityName;
import io.leitstand.inventory.service.FacilityService;
import io.leitstand.inventory.service.FacilitySettings;

@RunWith(MockitoJUnitRunner.class)
public class FacilityResourceTest {
	
	private static final FacilityId FACILITY_ID = randomFacilityId();
	private static final FacilityName FACILITY_NAME = facilityName("test");

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private FacilityService service;
	
	@InjectMocks
	private FacilityResource resource = new FacilityResource();
	
	@Test
	public void find_facilities() {
		resource.getFacilities("foo");
		verify(service).findFacilities("foo");
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
	public void create_new_facility_via_http_post() {
		FacilitySettings facility = newFacilitySettings()
									.withFacilityId(FACILITY_ID)
									.withFacilityName(FACILITY_NAME)
									.build();
		when(service.storeFacility(facility)).thenReturn(true);

		Response response = resource.storeFacility(facility);
		assertThat(response.getStatus(),is(201));
	}
	
	@Test
	public void create_facility_via_http_put_and_id() {
		FacilitySettings facility = newFacilitySettings()
									.withFacilityId(FACILITY_ID)
									.withFacilityName(FACILITY_NAME)
									.build();
		when(service.storeFacility(facility)).thenReturn(true);

		Response response = resource.storeFacility(FACILITY_ID, facility);
		assertThat(response.getStatus(),is(201));
	}
	
	@Test
	public void update_facility_via_http_put_and_id() {
		FacilitySettings facility = newFacilitySettings()
									.withFacilityId(FACILITY_ID)
									.withFacilityName(FACILITY_NAME)
									.build();

		Response response = resource.storeFacility(FACILITY_ID, facility);
		assertThat(response.getStatus(),is(204));
	}
	
	@Test
	public void create_facility_via_http_put_and_name() {
		FacilitySettings facility = newFacilitySettings()
									.withFacilityId(FACILITY_ID)
									.withFacilityName(FACILITY_NAME)
									.build();
		when(service.storeFacility(facility)).thenReturn(true);

		Response response = resource.storeFacility(FACILITY_NAME, facility);
		assertThat(response.getStatus(),is(201));
	}
	
	@Test
	public void update_facility_via_http_put_and_name() {
		FacilitySettings facility = newFacilitySettings()
									.withFacilityId(FACILITY_ID)
									.withFacilityName(FACILITY_NAME)
									.build();

		Response response = resource.storeFacility(FACILITY_NAME, facility);
		assertThat(response.getStatus(),is(204));
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
	
}

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
package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.FacilityId.randomFacilityId;
import static io.leitstand.inventory.service.FacilityName.facilityName;
import static io.leitstand.inventory.service.FacilityType.facilityType;
import static io.leitstand.inventory.service.ReasonCode.IVT0600E_FACILITY_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0604W_FACILITY_NAME_MISMATCH;
import static io.leitstand.inventory.service.ReasonCode.IVT0606W_FACILITY_TYPE_MISMATCH;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Message;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.FacilityId;
import io.leitstand.inventory.service.FacilityName;
import io.leitstand.inventory.service.FacilityType;

@RunWith(MockitoJUnitRunner.class)
public class FacilityProviderTest {
	
    private static final FacilityId FACILITY_ID = randomFacilityId();
    private static final FacilityName FACILITY_NAME = facilityName("facility");
    private static final FacilityType FACILITY_TYPE = facilityType("type");
    
    
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private Repository repository;
	
	@Mock
	private Messages messages;
	
	@InjectMocks
	private FacilityProvider provider = new FacilityProvider();

	@Test
	public void try_fetch_returns_null_if_facility_id_is_unknown() {
		assertNull(provider.tryFetchFacility(FACILITY_ID));
	}

	@Test
	public void try_fetch_returns_null_if_facility_name_is_unknown() {
		assertNull(provider.tryFetchFacility(FACILITY_NAME));
	}

	
	@Test
	public void fetch_throws_EntityNotFoundException_if_plaform_id_is_unknown() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0600E_FACILITY_NOT_FOUND));
		
		provider.fetchFacility(FACILITY_ID);
	}
	
	@Test
	public void fetch_throws_EntityNotFoundException_if_facility_name_is_unknon() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0600E_FACILITY_NOT_FOUND));
		
		provider.fetchFacility(FACILITY_NAME);
	}

	@Test
	public void do_not_report_facility_name_mismatch_if_no_name_is_specified() {
	    provider.reportNameMismatch(null, mock(Facility.class));
	    verifyZeroInteractions(messages);
	}

    @Test
    public void do_not_report_facility_type_mismatch_if_no_type_is_specified() {
        provider.reportTypeMismatch(null, mock(Facility.class));
        verifyZeroInteractions(messages);
    }

    @Test
    public void do_not_report_facility_name_mismatch_if_name_matches() {
        Facility facility = mock(Facility.class);
        when(facility.getFacilityName()).thenReturn(FACILITY_NAME);
        provider.reportNameMismatch(FACILITY_NAME,facility);
        verifyZeroInteractions(messages);
    }

    @Test
    public void do_not_report_facility_type_mismatch_if_type_matches() {
        Facility facility = mock(Facility.class);
        when(facility.getFacilityType()).thenReturn(FACILITY_TYPE);
        provider.reportTypeMismatch(FACILITY_TYPE, facility);
        verifyZeroInteractions(messages);
    }
    
    @Test
    public void report_facility_name_mismatch_if_name_matches() {
        Facility facility = mock(Facility.class);
        when(facility.getFacilityName()).thenReturn(FACILITY_NAME);
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        doNothing().when(messages).add(messageCaptor.capture());
        provider.reportNameMismatch(facilityName("other"),facility);
        assertEquals(IVT0604W_FACILITY_NAME_MISMATCH.getReasonCode(),messageCaptor.getValue().getReason());
    }

    @Test
    public void report_facility_type_mismatch_if_no_type_matches() {
        Facility facility = mock(Facility.class);
        when(facility.getFacilityType()).thenReturn(FACILITY_TYPE);
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        doNothing().when(messages).add(messageCaptor.capture());
        provider.reportTypeMismatch(facilityType("other"), facility);
        assertEquals(IVT0606W_FACILITY_TYPE_MISMATCH.getReasonCode(),messageCaptor.getValue().getReason());
    }
    
	@Test
	public void fetch_facility_by_id_when_id_is_specified() {
        Facility facility = mock(Facility.class);
        when(repository.execute(any(Query.class))).thenReturn(facility);

        FacilityProvider delegate = spy(provider);
        delegate.fetchFacility(FACILITY_ID, FACILITY_TYPE, null);
        verify(delegate).fetchFacility(FACILITY_ID);
	}
	
	@Test
	public void fetch_facility_by_name_when_name_is_specified() {
	    Facility facility = mock(Facility.class);
	    when(repository.execute(any(Query.class))).thenReturn(facility);
	    
        FacilityProvider delegate = spy(provider);
        delegate.fetchFacility(null, FACILITY_TYPE, FACILITY_NAME);
        verify(delegate).fetchFacility(FACILITY_NAME);
	}
	
	@Test
    public void fetch_facility_by_id_when_id_and_name_is_specified() {
	    Facility facility = mock(Facility.class);
        when(repository.execute(any(Query.class))).thenReturn(facility);

	    FacilityProvider delegate = spy(provider);
        delegate.fetchFacility(FACILITY_ID, FACILITY_TYPE, FACILITY_NAME);
        verify(delegate).fetchFacility(FACILITY_ID);
	}
	
	@Test
	public void do_nothing_when_neither_name_nor_id_is_specified() {
	    assertNull(provider.fetchFacility(null, null, null));
	}
	
	
	
}
